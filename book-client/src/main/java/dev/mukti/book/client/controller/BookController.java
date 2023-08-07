package dev.mukti.book.client.controller;

import dev.mukti.base.entity.Book;
import dev.mukti.base.service.BookService;
import dev.mukti.book.client.dto.CreateBookDto;
import dev.mukti.grpc.book.lib.FindBookByRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<Book> findBook(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) List<String> genres) {
        return bookService.findBook(
                FindBookByRequest.newBuilder()
                        .setTitle(Optional.ofNullable(title).orElse(""))
                        .setAuthor(Optional.ofNullable(author).orElse(""))
                        .addAllGenres(Optional.ofNullable(genres).orElse(Collections.emptyList()))
                        .build()
        );
    }

    @PostMapping
    public Book save(@Valid @RequestBody CreateBookDto createBookDto) {
        return bookService.save(createBookDto.toCreateBookRequest());
    }
}
