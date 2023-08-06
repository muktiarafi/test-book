-- migrate:up
INSERT INTO genres (book_id, genre)
VALUES
    (1, 'fiction'),
    (1, 'comedy');

-- migrate:down
DELETE FROM genres;
