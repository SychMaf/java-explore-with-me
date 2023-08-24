    DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF NOT EXISTS users (
  user_id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name                  VARCHAR(250) NOT NULL,
  email                 VARCHAR(254) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (user_id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

    DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE IF NOT EXISTS categories (
    category_id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name                VARCHAR(50) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (category_id),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

    DROP TABLE IF EXISTS events CASCADE;
CREATE TABLE IF NOT EXISTS events (
    event_id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation          VARCHAR(2000) NOT NULL,
    category            BIGINT,
    confirmed_request   BIGINT,
    created_on          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description         VARCHAR(7000) NOT NULL,
    event_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    users               BIGINT NOT NULL,
    lat                 REAL,
    lon                 REAL,
    paid                BOOLEAN NOT NULL,
    participant_limit   INTEGER NOT NULL,
    published_on        TIMESTAMP WITHOUT TIME ZONE,
    request_moderation  BOOLEAN NOT NULL,
    states              VARCHAR,
    title               VARCHAR(7000) NOT NULL,
    views               BIGINT,
    CONSTRAINT pk_events PRIMARY KEY (event_id),
    CONSTRAINT fk__events__user FOREIGN KEY (users) REFERENCES users (user_id),
    CONSTRAINT fk__events__category FOREIGN KEY (category) REFERENCES categories (category_id)
);

CREATE INDEX IF NOT EXISTS events_user_id_idx on events (users);
CREATE INDEX IF NOT EXISTS events_categoty_id_idx on events (category);

    DROP TABLE IF EXISTS requests CASCADE;
CREATE TABLE IF NOT EXISTS requests (
    request_id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    request_created     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event               BIGINT NOT NULL,
    requester           BIGINT NOT NULL,
    request_status      VARCHAR NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (request_id),
    CONSTRAINT fk__requests__requester FOREIGN KEY (requester) REFERENCES users (user_id),
    CONSTRAINT fk__requests__event FOREIGN KEY (event) REFERENCES events (event_id)
);

CREATE INDEX IF NOT EXISTS request_event_id_idx on requests (event);
CREATE INDEX IF NOT EXISTS request_user_id_idx on requests (requester);

    DROP TABLE IF EXISTS compilation CASCADE;
CREATE TABLE IF NOT EXISTS compilation (
    compilation_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title               VARCHAR(50) NOT NULL,
    pinned              BOOLEAN NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (compilation_id)
);

    DROP TABLE IF EXISTS compilation_events CASCADE;
CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id      BIGINT NOT NULL,
    event_id            BIGINT NOT NULL,
    PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk__compilation FOREIGN KEY (compilation_id) REFERENCES compilation (compilation_id),
    CONSTRAINT fk__event FOREIGN KEY (event_id) REFERENCES events (event_id)
);

    DROP TABLE IF EXISTS rates CASCADE;
CREATE TABLE IF NOT EXISTS rates (
    user_id      BIGINT NOT NULL,
    event_id     BIGINT NOT NULL,
    rate         SMALLINT NOT NULL,
    PRIMARY KEY (user_id, event_id),
    CONSTRAINT fk__user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk__event FOREIGN KEY (event_id) REFERENCES events (event_id)
);