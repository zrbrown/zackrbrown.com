CREATE SCHEMA IF NOT EXISTS zackrbrown_com;

SET SEARCH_PATH TO zackrbrown_com;

CREATE TABLE IF NOT EXISTS posts(
    id UUID PRIMARY KEY,
    url_name VARCHAR(105), -- index
    title VARCHAR(100),
    content VARCHAR(5000)
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