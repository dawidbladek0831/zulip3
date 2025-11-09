CREATE TABLE post
(
    id      BIGSERIAL NOT NULL,
    name    VARCHAR(255),
    deleted BOOLEAN   NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_post PRIMARY KEY (id)
);

CREATE TABLE post_tag
(
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT pk_post_tag PRIMARY KEY (post_id, tag_id)
);

ALTER TABLE post_tag
    ADD CONSTRAINT fk_post_tag_on_post FOREIGN KEY (post_id) REFERENCES post (id);

ALTER TABLE post_tag
    ADD CONSTRAINT fk_post_tag_on_tag FOREIGN KEY (tag_id) REFERENCES tag (id);

CREATE TABLE post_comment
(
    id      BIGSERIAL NOT NULL,
    content VARCHAR(255),
    post_id BIGINT    NOT NULL,
    deleted BOOLEAN   NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_post_comment PRIMARY KEY (id)
);

ALTER TABLE post_comment
    ADD CONSTRAINT fk_post_comment_on_post FOREIGN KEY (post_id) REFERENCES post (id);

CREATE TABLE post_details
(
    id         BIGINT  NOT NULL,
    name       VARCHAR(255),
    deleted_id UUID    NOT NULL DEFAULT gen_random_uuid(),
    deleted    BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_post_details PRIMARY KEY (id, deleted_id)
);

ALTER TABLE post_details
    ADD CONSTRAINT fk_post_details_on_post FOREIGN KEY (id) REFERENCES post (id);
