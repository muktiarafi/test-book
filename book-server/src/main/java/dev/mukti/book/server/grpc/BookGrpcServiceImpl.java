package dev.mukti.book.server.grpc;

import dev.mukti.book.server.entity.Book;
import dev.mukti.book.server.service.BookService;
import dev.mukti.grpc.book.lib.BookResponse;
import dev.mukti.grpc.book.lib.BookServiceGrpc;
import dev.mukti.grpc.book.lib.CreateBookRequest;
import dev.mukti.grpc.book.lib.FindBookByRequest;
import dev.mukti.grpc.common.lib.Empty;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
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

        responseObserver.onNext(BookResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void showAllBook(Empty request, StreamObserver<BookResponse> responseObserver) {
        bookService.showAllBook().forEach(book -> responseObserver.onNext(buildBookResponse(book)));

        responseObserver.onCompleted();
    }

    private BookResponse buildBookResponse(Book book) {
        return BookResponse.newBuilder()
                .setId(book.id())
                .setTitle(book.title())
                .setAuthor(book.author())
                .setDescription(book.description())
                .addAllGenres(book.genres())
                .setCreatedAt(book.createdAt().toString())
                .setUpdatedAt(book.updatedAt().toString())
                .build();
    }
}
