package dev.mukti.book.server.service.impl;

import dev.mukti.book.server.entity.Book;
import dev.mukti.book.server.repository.BookRepository;
import dev.mukti.book.server.service.BookService;
import dev.mukti.grpc.book.lib.CreateBookRequest;
import dev.mukti.grpc.book.lib.FindBookByRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Book save(CreateBookRequest createBookRequest) {
        return bookRepository.save(createBookRequest);
    }

    @Override
    public List<Book> findBook(FindBookByRequest findBookByRequest) {
        return bookRepository.findBook(findBookByRequest);
    }

    @Override
    public List<Book> showAllBook() {
        return bookRepository.showAllBook();
    }
}
