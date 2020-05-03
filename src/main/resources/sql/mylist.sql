SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DELIMITER ;;

DROP PROCEDURE IF EXISTS `clean_all`;;
CREATE PROCEDURE `clean_all`()
BEGIN

    DELETE FROM users WHERE true;
    DELETE FROM options WHERE true;
    DELETE FROM images WHERE true;

END;;

DELIMITER ;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                         `user_name` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                         `user_pass` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                         `user_email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                         `user_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'activated',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `user_name` (`user_name`),
                         KEY `user_name_2` (`user_name`,`user_pass`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

insert into users(user_name, user_pass) value ('TestUser', '*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5');

DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
                           `option_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                           `option_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `option_value` longtext COLLATE utf8mb4_unicode_ci,
                           PRIMARY KEY (`option_id`),
                           UNIQUE KEY `option_name` (`option_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
                          `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                          `user` bigint(20) unsigned NOT NULL,

                          `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
                          `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Modify Time',

                          `title` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `description` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,

                          PRIMARY KEY (`id`),
                          UNIQUE KEY `tag_title` (`title`),
                          KEY `ctime` (`ctime`),
                          KEY `mtime` (`mtime`),
                          KEY `user` (`user`),

                          CONSTRAINT `tags_users` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `images`;
CREATE TABLE `images` (
                          `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                          `user` bigint(20) unsigned NOT NULL,

                          `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
                          `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Modify Time',

                          `page_title` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                          `page_url` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

                          `image_title` text COLLATE utf8mb4_unicode_ci,
                          `image_url` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL,

                          PRIMARY KEY (`id`),
                          KEY `ctime` (`ctime`),
                          KEY `mtime` (`mtime`),
                          KEY `user` (`user`),

                          CONSTRAINT `images_users` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

