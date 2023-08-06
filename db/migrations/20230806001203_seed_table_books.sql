-- migrate:up
INSERT INTO books (title, author, description)
VALUES
    ('ini buku', 'budi', 'ditulis oleh budi');

-- migrate:down
DELETE FROM books;
