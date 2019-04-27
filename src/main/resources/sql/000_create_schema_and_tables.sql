CREATE SCHEMA IF NOT EXISTS application;

SET SEARCH_PATH TO application;

CREATE TABLE IF NOT EXISTS posts
(
    id                UUID PRIMARY KEY,
    url_name          VARCHAR(105),
    title             VARCHAR(100),
    content           VARCHAR(5000),
    created_date_time TIMESTAMP
);

CREATE INDEX IF NOT EXISTS url_name_index
    ON posts (url_name);

CREATE TABLE IF NOT EXISTS post_updates
(
    id                UUID PRIMARY KEY,
    post_id           UUID,
    content           VARCHAR(5000),
    updated_date_time TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE IF NOT EXISTS tags
(
    id   UUID PRIMARY KEY,
    name VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS posts_to_tags
(
    post_id UUID,
    tag_id  UUID,
    FOREIGN KEY (tag_id) REFERENCES tags (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE INDEX IF NOT EXISTS posts_to_tags_post_index
    ON posts_to_tags (post_id);

CREATE INDEX IF NOT EXISTS posts_to_tags_tag_index
    ON posts_to_tags (tag_id);
