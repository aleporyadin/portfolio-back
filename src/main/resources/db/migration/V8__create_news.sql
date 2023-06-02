CREATE TABLE news
(
    title       VARCHAR(75)  NOT NULL PRIMARY KEY,
    description VARCHAR(194) NOT NULL,
    url         VARCHAR(136) NOT NULL,
    image       VARCHAR(126) NOT NULL,
    publishedAt VARCHAR(20)  NOT NULL
);
