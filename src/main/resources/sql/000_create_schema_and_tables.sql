CREATE SCHEMA IF NOT EXISTS application;

SET SEARCH_PATH TO application;

CREATE TABLE IF NOT EXISTS posts(
    id UUID PRIMARY KEY,
    url_name VARCHAR(105), -- index
    title VARCHAR(100),
    content VARCHAR(5000),
    created_date DATE
);

CREATE TABLE IF NOT EXISTS post_updates(
    id UUID PRIMARY KEY,
    post_id UUID,
    updated_date DATE,
    FOREIGN KEY(post_id) REFERENCES posts(id)
);

CREATE TABLE IF NOT EXISTS tags(
    id UUID PRIMARY KEY,
    name VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS posts_to_tags(
    post_id UUID PRIMARY KEY,
    tag_id UUID,
    FOREIGN KEY(tag_id) REFERENCES tags(id),
    FOREIGN KEY(post_id) REFERENCES posts(id)
);