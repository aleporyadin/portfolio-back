CREATE TABLE `users`
(
    `id`         bigint NOT NULL,
    `avatar`     mediumblob,
    `birthdate`  date         DEFAULT NULL,
    `email`      varchar(50)  DEFAULT NULL,
    `first_name` varchar(20)  DEFAULT NULL,
    `last_name`  varchar(20)  DEFAULT NULL,
    `password`   varchar(120) DEFAULT NULL,
    `user_name`  varchar(20)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`user_name`),
    UNIQUE KEY (`email`)
)

