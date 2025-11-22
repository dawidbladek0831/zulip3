CREATE TABLE post
(
    id   BIGSERIAL NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_post PRIMARY KEY (id)
);

CREATE TABLE post_tag
(
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT pk__post_tag PRIMARY KEY (post_id, tag_id),
    CONSTRAINT fk__post_tag__post
        FOREIGN KEY (post_id)
            REFERENCES post (id),
    CONSTRAINT fk__post_tag__tag
        FOREIGN KEY (tag_id)
            REFERENCES tag (id)
);

CREATE TABLE post_comment
(
    id      BIGSERIAL NOT NULL,
    content VARCHAR(255),
    post_id BIGINT    NOT NULL,
    CONSTRAINT pk__post_comment PRIMARY KEY (id),
    CONSTRAINT fk__post_comment__post
        FOREIGN KEY (post_id)
            REFERENCES post (id)
);

CREATE TABLE post_details
(
    id   BIGINT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk__post_details PRIMARY KEY (id),
    CONSTRAINT fk__post_details__post
        FOREIGN KEY (id)
            REFERENCES post (id)
);
