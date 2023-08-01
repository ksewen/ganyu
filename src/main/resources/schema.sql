DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          bigint (20) NOT NULL AUTO_INCREMENT,
    `create_by`   varchar(64) DEFAULT NULL,
    `create_time` datetime    DEFAULT NULL,
    `deleted`     tinyint (1) NOT NULL DEFAULT '0',
    `modify_by`   varchar(64) DEFAULT NULL,
    `modify_time` datetime    DEFAULT NULL,
    `name`        varchar(64) NOT NULL,
    PRIMARY KEY
        (`id`),
    UNIQUE KEY `UK8sewwnpamngi6b1dwaa88askk`
        (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;