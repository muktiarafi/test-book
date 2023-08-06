package dev.mukti.book.server.repository;

import dev.mukti.book.server.entity.Book;
import dev.mukti.grpc.book.lib.CreateBookRequest;
import dev.mukti.grpc.book.lib.FindBookByRequest;

import java.util.List;

public interface BookRepository {
    Book save(CreateBookRequest createBookRequest);
    List<Book> findBook(FindBookByRequest findBookByRequest);
    List<Book> showAllBook();
}
