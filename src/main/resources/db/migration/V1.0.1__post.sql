CREATE TABLE post
(
    id   BIGINT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_post PRIMARY KEY (id)
);

CREATE TABLE post_tag
(
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT pk_post_tag PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE post_comment
(
    id      BIGINT NOT NULL,
    content VARCHAR(255),
    post_id BIGINT NOT NULL,
    CONSTRAINT pk_post_comment PRIMARY KEY (id)
);

ALTER TABLE post_tag
    ADD CONSTRAINT fk_post_tag_on_post FOREIGN KEY (post_id) REFERENCES post (id);

ALTER TABLE post_tag
    ADD CONSTRAINT fk_post_tag_on_tag FOREIGN KEY (tag_id) REFERENCES tag (id);