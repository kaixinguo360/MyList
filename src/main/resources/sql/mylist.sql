SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';
SET NAMES utf8mb4;

-- Users --
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

-- Resources --
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources` (

                             `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                             `type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `data` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `title` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `description` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `source` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             PRIMARY KEY (`id`),

                             `user` bigint(20) unsigned NOT NULL, KEY `user` (`user`),
                             `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP, KEY `ctime` (`ctime`),
                             `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, KEY `mtime` (`mtime`),
                             CONSTRAINT `resource_user` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Pages --
DROP TABLE IF EXISTS `pages`;
CREATE TABLE `pages` (
                         `url` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `title` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                         `description` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                         PRIMARY KEY (`url`),

                         `user` bigint(20) unsigned NOT NULL, KEY `user` (`user`),
                         `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP, KEY `ctime` (`ctime`),
                         `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, KEY `mtime` (`mtime`),
                         CONSTRAINT `page_user` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tags --
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
                        `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                        `name` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `description` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                        PRIMARY KEY (`id`),

                        `user` bigint(20) unsigned NOT NULL, KEY `user` (`user`),
                        `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP, KEY `ctime` (`ctime`),
                        `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, KEY `mtime` (`mtime`),
                        CONSTRAINT `tag_user` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `resource_tags`;
CREATE TABLE `resource_tags` (
                                 `resource_id` bigint(20) unsigned NOT NULL,
                                 `tag_id` bigint(20) unsigned NOT NULL,
                                 PRIMARY KEY (`resource_id`, `tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Option --
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
                           `option_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                           `option_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `option_value` longtext COLLATE utf8mb4_unicode_ci,
                           PRIMARY KEY (`option_id`),
                           UNIQUE KEY `option_name` (`option_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Groups --
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
                          `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                          `user` bigint(20) unsigned NOT NULL,

                          `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
                          `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Modify Time',

                          `name` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `description` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,

                          `type` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `value` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,

                          PRIMARY KEY (`id`),
                          KEY `ctime` (`ctime`),
                          KEY `mtime` (`mtime`),
                          KEY `user` (`user`),
                          KEY `type` (`type`),

                          CONSTRAINT `groups_users` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


