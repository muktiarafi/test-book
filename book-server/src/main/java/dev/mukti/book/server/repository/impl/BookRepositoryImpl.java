package dev.mukti.book.server.repository.impl;

import dev.mukti.book.server.entity.Book;
import dev.mukti.book.server.repository.BookRepository;
import dev.mukti.grpc.book.lib.CreateBookRequest;
import dev.mukti.grpc.book.lib.FindBookByRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;

@Repository
@AllArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Book save(CreateBookRequest createBookRequest) {
        var genres = StringUtils.chop(createBookRequest.getGenresList()
                .stream()
                .map(s -> "((SELECT id FROM book_insert), ?),")
                .collect(Collectors.joining()));

        var sql = String.format("""
                WITH book_insert AS (
                    INSERT INTO books (title, author, description)
                    VALUES (?, ?, ?)
                    RETURNING *
                ),
                genres_insert AS (
                    INSERT INTO genres (book_id, genre)
                    VALUES %s
                    RETURNING *
                )
                SELECT id, title, author, description, created_at, updated_at,
                COALESCE(ARRAY((SELECT genre FROM genres_insert)), '{}'::text[]) AS genres
                FROM book_insert;
                """, genres);

        var args = new ArrayList<>(Arrays.asList(
                createBookRequest.getTitle(),
                createBookRequest.getAuthor(),
                createBookRequest.getDescription()
        ));
        args.addAll(createBookRequest.getGenresList().stream().toList());

        return jdbcTemplate.queryForObject(
                sql,
                bookRowMapper(),
                args.toArray()
        );
    }

    @Override
    public List<Book> findBook(FindBookByRequest findBookByRequest) {
        Condition condition = DSL.noCondition();
        if (!StringUtils.isBlank(findBookByRequest.getTitle())) {
            condition = condition.and(field("title").eq(findBookByRequest.getTitle()));
        }

        if (!StringUtils.isBlank(findBookByRequest.getAuthor())) {
            condition = condition.and(field("author").eq(findBookByRequest.getAuthor()));
        }

        if (!CollectionUtils.isEmpty(findBookByRequest.getGenresList())) {
            condition = condition.and(field("id").in(
                    select(field("book_id"))
                            .from(table("genres"))
                            .where(field("genre").in(findBookByRequest.getGenresList().toArray()))
            ));
        }

        var sql = """
                SELECT id, title, author, description, 
                COALESCE(ARRAY((SELECT genre FROM genres WHERE book_id = b.id)), '{}'::text[]) AS genres, 
                created_at, updated_at
                FROM books AS b
                WHERE 
                """ + condition;

        return jdbcTemplate.query(
                sql,
                bookRowMapper()
        );
    }

    @Override
    public List<Book> showAllBook() {
        var sql = """
                SELECT id, title, author, description, 
                COALESCE(ARRAY((SELECT genre FROM genres WHERE book_id = b.id)), '{}'::text[]) AS genres, 
                created_at, updated_at
                FROM books AS b
                """;

        return jdbcTemplate.query(
                sql,
                bookRowMapper()
        );
    }

    private RowMapper<Book> bookRowMapper() {
        return (rs, rowNUm) -> new Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("description"),
                List.of((String[]) rs.getArray("genres").getArray()),
                rs.getObject("created_at", OffsetDateTime.class),
                rs.getObject("updated_at", OffsetDateTime.class)
        );
    }
}
