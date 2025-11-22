CREATE TABLE invoice
(
    id     BIGSERIAL NOT NULL,
    number VARCHAR(255),
    CONSTRAINT pk_invoice PRIMARY KEY (id)
);

CREATE TABLE invoice_contract
(
    id         BIGSERIAL NOT NULL,
    number     VARCHAR(255),
    invoice_id BIGINT    NOT NULL,
    CONSTRAINT pk__invoice_contract PRIMARY KEY (id),
    CONSTRAINT fk__invoice_contract__invoice
        FOREIGN KEY (invoice_id)
            REFERENCES invoice (id)
);

CREATE TABLE invoice_line
(
    id         BIGSERIAL NOT NULL,
    name       VARCHAR(255),
    invoice_id BIGINT    NOT NULL,
    CONSTRAINT pk__invoice_line PRIMARY KEY (id),
    CONSTRAINT fk__invoice_line__invoice
        FOREIGN KEY (invoice_id)
            REFERENCES invoice (id)
);

CREATE TABLE invoice_line_description
(
    id              BIGSERIAL NOT NULL,
    description     VARCHAR(255),
    invoice_line_id BIGINT    NOT NULL,
    CONSTRAINT pk__invoice_line_description PRIMARY KEY (id),
    CONSTRAINT fk__invoice_line_description__invoice_line
        FOREIGN KEY (invoice_line_id)
            REFERENCES invoice_line (id)
);

CREATE TABLE invoice_payment
(
    id          BIGINT NOT NULL,
    total_net   DECIMAL(16, 2),
    total_gross DECIMAL(16, 2),
    CONSTRAINT pk__invoice_payment PRIMARY KEY (id),
    CONSTRAINT fk__invoice_payment__invoice
        FOREIGN KEY (id)
            REFERENCES invoice (id)
);

CREATE TABLE invoice_details
(
    id          BIGINT NOT NULL,
    issue_date  TIMESTAMP,
    issue_place VARCHAR(255),
    CONSTRAINT pk__invoice_details PRIMARY KEY (id),
    CONSTRAINT fk__invoice_details__invoice
        FOREIGN KEY (id)
            REFERENCES invoice (id)
);