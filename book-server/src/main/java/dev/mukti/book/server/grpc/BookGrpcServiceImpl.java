package dev.mukti.book.server.grpc;

import dev.mukti.base.entity.Book;
import dev.mukti.base.service.BookService;
import dev.mukti.grpc.book.lib.BookResponse;
import dev.mukti.grpc.book.lib.BookServiceGrpc;
import dev.mukti.grpc.book.lib.CreateBookRequest;
import dev.mukti.grpc.book.lib.FindBookByRequest;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

import java.time.format.DateTimeFormatter;

@GRpcService
@AllArgsConstructor
public class BookGrpcServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    private final BookService bookService;

    @Override
    public void save(CreateBookRequest request, StreamObserver<BookResponse> responseObserver) {
        Book book = bookService.save(request);

        responseObserver.onNext(buildBookResponse(book));
        responseObserver.onCompleted();
    }

    @Override
    public void findBook(FindBookByRequest request, StreamObserver<BookResponse> responseObserver) {
        bookService.findBook(request).forEach(book -> responseObserver.onNext(buildBookResponse(book)));

        responseObserver.onCompleted();
    }

    private BookResponse buildBookResponse(Book book) {
        return BookResponse.newBuilder()
                .setId(book.id())
                .setTitle(book.title())
                .setAuthor(book.author())
                .setDescription(book.description())
                .addAllGenres(book.genres())
                .setCreatedAt(book.createdAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")))
                .setUpdatedAt(book.updatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")))
                .build();
    }
}
