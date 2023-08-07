package dev.mukti.base.service;

import dev.mukti.base.entity.Book;
import dev.mukti.grpc.book.lib.CreateBookRequest;
import dev.mukti.grpc.book.lib.FindBookByRequest;

import java.util.List;

public interface BookService {
    Book save(CreateBookRequest createBookRequest);
    List<Book> findBook(FindBookByRequest findBookByRequest);
}
