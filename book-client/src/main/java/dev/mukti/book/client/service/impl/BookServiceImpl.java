package dev.mukti.book.client.service.impl;

import dev.mukti.base.entity.Book;
import dev.mukti.base.service.BookService;
import dev.mukti.grpc.book.lib.BookResponse;
import dev.mukti.grpc.book.lib.BookServiceGrpc;
import dev.mukti.grpc.book.lib.CreateBookRequest;
import dev.mukti.grpc.book.lib.FindBookByRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookServiceGrpc.BookServiceBlockingStub stub;

    @Override
    public Book save(CreateBookRequest createBookRequest) {
        return toBook(stub.save(createBookRequest));
    }

    @Override
    public List<Book> findBook(FindBookByRequest findBookByRequest) {
        List<Book> books = new ArrayList<>();
        Iterator<BookResponse> bookResponseIterator = stub.findBook(findBookByRequest);
        while (bookResponseIterator.hasNext()) {
            BookResponse bookResponse = bookResponseIterator.next();
            books.add(toBook(bookResponse));
        }

        return books;
    }

    private Book toBook(BookResponse bookResponse) {
        return new Book(
                bookResponse.getId(),
                bookResponse.getTitle(),
                bookResponse.getAuthor(),
                bookResponse.getDescription(),
                bookResponse.getGenresList().stream().toList(),
                OffsetDateTime.parse(bookResponse.getCreatedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                OffsetDateTime.parse(bookResponse.getUpdatedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        );
    }
}
