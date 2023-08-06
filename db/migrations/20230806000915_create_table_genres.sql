-- migrate:up
CREATE TABLE IF NOT EXISTS genres (
    id SERIAL PRIMARY KEY,
    book_id INT REFERENCES books (id) ON DELETE CASCADE,
    genre VARCHAR(255) NOT NULL,
    UNIQUE(book_id, genre)
);

-- migrate:down
DROP IF EXISTS genres;
