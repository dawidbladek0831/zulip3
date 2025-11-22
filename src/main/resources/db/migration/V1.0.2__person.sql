CREATE TABLE person
(
    id   BIGSERIAL NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_person PRIMARY KEY (id)
);

CREATE TABLE person_address
(
    id     BIGINT NOT NULL,
    street VARCHAR(255),
    CONSTRAINT pk_person_address PRIMARY KEY (id),
    CONSTRAINT fk__person_address__person
        FOREIGN KEY (id)
            REFERENCES person (id)
);

CREATE TABLE person_profile
(
    id       BIGINT NOT NULL,
    nickname VARCHAR(255),
    CONSTRAINT pk_person_profile PRIMARY KEY (id),
    CONSTRAINT fk__person_profile__person
        FOREIGN KEY (id)
            REFERENCES person (id)
);