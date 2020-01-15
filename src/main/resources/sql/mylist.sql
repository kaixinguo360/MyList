-- Adminer 4.6.2 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DELIMITER ;;

DROP PROCEDURE IF EXISTS `add_image`;;
CREATE PROCEDURE `add_image`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `lstatus` varchar(20) CHARACTER SET 'utf8mb4', IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4', IN `url` varchar(511) CHARACTER SET 'utf8mb4', IN `description` text CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'image',
            title,
            excerpt,
            lstatus,
            permissions,
            is_nsfw,
            is_like,
            source_url,
            comment
        );

    INSERT INTO images(
        image_node_id,
        image_url,
        image_description
    ) VALUE (
             @node_id,
             url,
             description
        );

    SET id = @node_id;


END;;

DROP PROCEDURE IF EXISTS `add_list`;;
CREATE PROCEDURE `add_list`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `lstatus` varchar(20) CHARACTER SET 'utf8mb4', IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'list',
            title,
            excerpt,
            lstatus,
            permissions,
            is_nsfw,
            is_like,
            source_url,
            comment
        );

    SET id = @node_id;

END;;

DROP PROCEDURE IF EXISTS `add_music`;;
CREATE PROCEDURE `add_music`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `lstatus` varchar(20) CHARACTER SET 'utf8mb4', IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4', IN `url` varchar(511) CHARACTER SET 'utf8mb4', IN `format` varchar(20) CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'music',
            title,
            excerpt,
            lstatus,
            permissions,
            is_nsfw,
            is_like,
            source_url,
            comment
        );

    INSERT INTO musics(
        music_node_id,
        music_url,
        music_format
    ) VALUE (
             @node_id,
             url,
             format
        );

    SET id = @node_id;

END;;

DROP PROCEDURE IF EXISTS `add_node`;;
CREATE PROCEDURE `add_node`(OUT `id` bigint(20) unsigned, IN `user` bigint(10) unsigned, IN `type` varchar(20) CHARACTER SET 'utf8mb4', IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `lstatus` varchar(20) CHARACTER SET 'utf8mb4', IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4')
BEGIN

    INSERT INTO nodes(
        node_user,
        node_type,
        node_title,
        node_excerpt,
        node_lstatus,
        node_lcount,
        node_permissions,
        node_nsfw,
        node_like,
        node_source_url,
        node_comment
    ) VALUE (
             user,
             type,
             title,
             excerpt,
             lstatus,
             0,
             permissions,
             is_nsfw,
             is_like,
             source_url,
             comment
        );

    SET id = LAST_INSERT_ID();

END;;

DROP PROCEDURE IF EXISTS `add_part`;;
CREATE PROCEDURE `add_part`(IN `parent` bigint(20) unsigned, IN `part` bigint(20) unsigned)
BEGIN

    SET @order = 0;

    SELECT MAX(part_content_order) INTO @order FROM parts
    WHERE
            part_parent_id = parent
    GROUP BY
        part_parent_id;

    SELECT node_type INTO @type FROM nodes
    WHERE
            id = part;

    INSERT INTO parts(
        part_parent_id,
        part_content_order,
        part_content_type,
        part_content_id
    ) VALUE (
             parent,
             @order + 1,
             @type,
             part
        );

    CALL update_lcount(part);

END;;

DROP PROCEDURE IF EXISTS `add_text`;;
CREATE PROCEDURE `add_text`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `lstatus` varchar(20) CHARACTER SET 'utf8mb4', IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4', IN `content` text CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'text',
            title,
            excerpt,
            lstatus,
            permissions,
            is_nsfw,
            is_like,
            source_url,
            comment
        );

    INSERT INTO texts(
        text_node_id,
        text_content
    ) VALUE (
             @node_id,
             content
        );

    SET id = @node_id;

END;;

DROP PROCEDURE IF EXISTS `add_user`;;
CREATE PROCEDURE `add_user`(OUT `id` bigint(20) unsigned, IN `name` varchar(60) CHARACTER SET 'utf8mb4', IN `pass` varchar(255) CHARACTER SET 'utf8mb4', IN `email` varchar(100) CHARACTER SET 'utf8mb4', IN `status` varchar(20) CHARACTER SET 'utf8mb4')
BEGIN

    INSERT INTO users(
        user_name,
        user_pass,
        user_email,
        user_status
    ) VALUE (
             name,
             PASSWORD(pass),
             email,
             status
        );

    SET id = LAST_INSERT_ID();

END;;

DROP PROCEDURE IF EXISTS `add_video`;;
CREATE PROCEDURE `add_video`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `lstatus` varchar(20) CHARACTER SET 'utf8mb4', IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4', IN `url` varchar(511) CHARACTER SET 'utf8mb4', IN `format` varchar(20) CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'video',
            title,
            excerpt,
            lstatus,
            permissions,
            is_nsfw,
            is_like,
            source_url,
            comment
        );

    INSERT INTO videos(
        video_node_id,
        video_url,
        video_format
    ) VALUE (
             @node_id,
             url,
             format
        );

    SET id = @node_id;

END;;

DROP PROCEDURE IF EXISTS `check_user`;;
CREATE PROCEDURE `check_user`(IN `name` varchar(60), IN `pass` varchar(255))
SELECT * FROM users
WHERE
        user_name = name
  AND
        user_pass = PASSWORD(pass);;

DROP PROCEDURE IF EXISTS `clean_all`;;
CREATE PROCEDURE `clean_all`()
BEGIN

    DELETE FROM users;
    DELETE FROM nodes;
    DELETE FROM texts;
    DELETE FROM images;
    DELETE FROM musics;
    DELETE FROM videos;
    DELETE FROM parts;

END;;

DROP PROCEDURE IF EXISTS `clean_list`;;
CREATE PROCEDURE `clean_list`(IN `list` bigint(20) unsigned)
BEGIN

    DECLARE node_id bigint(20);
    DECLARE done INT DEFAULT FALSE;
    DECLARE My_Cursor CURSOR FOR (
        SELECT part_content_id FROM parts
        WHERE part_parent_id = list
    );
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN My_Cursor;
    myLoop: LOOP
        FETCH My_Cursor into node_id;
        IF done THEN LEAVE myLoop; END IF;
        CALL delete_part(list, node_id);
    END LOOP myLoop;
    CLOSE My_Cursor;

END;;

DROP PROCEDURE IF EXISTS `clean_nodes`;;
CREATE PROCEDURE `clean_nodes`()
DELETE FROM nodes
WHERE
        node_lcount = 0
  AND
        node_lstatus = 'attachment';;

DROP PROCEDURE IF EXISTS `delete_list`;;
CREATE PROCEDURE `delete_list`(IN `list` bigint(20) unsigned)
BEGIN

    CALL clean_list(list);
    CALL clean_nodes();
    DELETE FROM nodes WHERE id = list;

END;;

DROP PROCEDURE IF EXISTS `delete_part`;;
CREATE PROCEDURE `delete_part`(IN `parent` bigint(20) unsigned, IN `part` bigint(20) unsigned)
BEGIN

    DELETE FROM parts
    WHERE
            part_parent_id = parent
      AND
            part_content_id = part
    ;

    CALL update_lcount(part);

END;;

DROP PROCEDURE IF EXISTS `get_list`;;
CREATE PROCEDURE `get_list`(IN `list` bigint(20) unsigned)
BEGIN

    SELECT n.* FROM nodes n, parts
    WHERE
            part_content_id = n.id
      AND
            part_parent_id = list
    ORDER BY
        part_content_order
    ;

END;;

DROP PROCEDURE IF EXISTS `show_all`;;
CREATE PROCEDURE `show_all`()
BEGIN

    SELECT * FROM users;
    SELECT * FROM nodes;
    SELECT * FROM texts;
    SELECT * FROM images;
    SELECT * FROM musics;
    SELECT * FROM videos;
    SELECT * FROM parts;

END;;

DROP PROCEDURE IF EXISTS `test`;;
CREATE PROCEDURE `test`()
BEGIN

    -- Delete All --
    CALL clean_all();

-- Add User --
    CALL add_user(
            @user_id,
            'TestUser',
            '1234567',
            'test@example.com',
            'activated'
        );

-- Check User --
    CALL check_user(
            'TestUser',
            '1234567'
        );

-- Add List --
    CALL add_list(
            @list_id,
            @user_id,
            'Test List',
            'excerpt',
            'alone',
            'private',
            0,
            0,
            'http://list.example.com',
            'This is comment of list.'
        );

-- Add Text --
    CALL add_text(
            @text_id,
            @user_id,
            'Test List',
            'excerpt',
            'attachment',
            'private',
            0,
            0,
            'http://text.example.com',
            'This is comment of text.',
            'This is text content.'
        );

-- Add Image --
    CALL add_image(
            @image_id,
            @user_id,
            'Test List',
            'excerpt',
            'alone',
            'private',
            0,
            0,
            'http://image.example.com',
            'This is comment of image.',
            'http://example.com/image.png',
            'This is image description.'
        );

-- Add Music --
    CALL add_music(
            @music_id,
            @user_id,
            'Test List',
            'excerpt',
            'alone',
            'private',
            0,
            0,
            'http://music.example.com',
            'This is comment of music.',
            'http://example.com/music.mp3',
            'mp3'
        );

-- Add Video --
    CALL add_video(
            @video_id,
            @user_id,
            'Test List',
            'excerpt',
            'alone',
            'private',
            0,
            0,
            'http://video.example.com',
            'This is comment of video.',
            'http://example.com/video.avi',
            'avi'
        );

-- Show IDs --
    SELECT @user_id, @list_id, @text_id, @image_id, @music_id, @video_id;

-- Add Parts To List --
    CALL add_part(@list_id, @text_id);
    CALL add_part(@list_id, @image_id);
    CALL add_part(@list_id, @music_id);
    CALL add_part(@list_id, @video_id);
    CALL add_part(@list_id, @video_id);

-- Get List --
    CALL get_list(@list_id);

-- Delete List --
    CALL delete_list(
            @list_id
        );

-- Show All --
    CALL show_all();

END;;

DROP PROCEDURE IF EXISTS `update_lcount`;;
CREATE PROCEDURE `update_lcount`(IN `node_id` bigint(20) unsigned)
BEGIN

    SET @lcount = 0;

    SELECT COUNT(*) INTO @lcount FROM parts
    WHERE
            part_content_id = node_id
    GROUP BY
        part_content_id;

    UPDATE nodes SET
        node_lcount = @lcount
    WHERE
            id = node_id;

END;;

DELIMITER ;

DROP TABLE IF EXISTS `images`;
CREATE TABLE `images` (
                          `image_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                          `image_node_id` bigint(20) unsigned NOT NULL,
                          `image_url` varchar(511) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `image_description` text COLLATE utf8mb4_unicode_ci,
                          PRIMARY KEY (`image_id`),
                          UNIQUE KEY `image_node_id` (`image_node_id`),
                          CONSTRAINT `images_ibfk_3` FOREIGN KEY (`image_node_id`) REFERENCES `nodes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `images` (`image_id`, `image_node_id`, `image_url`, `image_description`) VALUES
(145,	714,	'http://example.com/image.png',	'This is image description.');

DROP TABLE IF EXISTS `musics`;
CREATE TABLE `musics` (
                          `music_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                          `music_node_id` bigint(20) unsigned NOT NULL,
                          `music_url` varchar(511) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `music_format` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
                          PRIMARY KEY (`music_id`),
                          KEY `music_node_id` (`music_node_id`),
                          CONSTRAINT `musics_ibfk_2` FOREIGN KEY (`music_node_id`) REFERENCES `nodes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `musics` (`music_id`, `music_node_id`, `music_url`, `music_format`) VALUES
(137,	715,	'http://example.com/music.mp3',	'mp3');

DROP TABLE IF EXISTS `nodemeta`;
CREATE TABLE `nodemeta` (
                            `meta_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                            `node_id` bigint(20) unsigned NOT NULL DEFAULT '0',
                            `meta_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            `meta_value` longtext COLLATE utf8mb4_unicode_ci,
                            PRIMARY KEY (`meta_id`),
                            KEY `node_id` (`node_id`),
                            KEY `meta_key` (`meta_key`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `nodes`;
CREATE TABLE `nodes` (
                         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                         `node_user` bigint(20) unsigned NOT NULL,
                         `node_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'list',
                         `node_ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
                         `node_mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Modify Time',
                         `node_title` text COLLATE utf8mb4_unicode_ci,
                         `node_excerpt` text COLLATE utf8mb4_unicode_ci,
                         `node_lstatus` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'alone' COMMENT 'Link status',
                         `node_lcount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'Link Count',
                         `node_permissions` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'private',
                         `node_nsfw` tinyint(1) NOT NULL DEFAULT '0',
                         `node_like` tinyint(1) NOT NULL DEFAULT '0',
                         `node_source_url` varchar(511) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                         `node_comment` text COLLATE utf8mb4_unicode_ci,
                         PRIMARY KEY (`id`),
                         KEY `node_ctime` (`node_ctime`),
                         KEY `node_mtime` (`node_mtime`),
                         KEY `node_user` (`node_user`),
                         CONSTRAINT `nodes_ibfk_1` FOREIGN KEY (`node_user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `nodes` (`id`, `node_user`, `node_type`, `node_ctime`, `node_mtime`, `node_title`, `node_excerpt`, `node_lstatus`, `node_lcount`, `node_permissions`, `node_nsfw`, `node_like`, `node_source_url`, `node_comment`) VALUES
(714,	154,	'image',	'2020-01-15 00:54:54',	'2020-01-15 00:54:54',	'Test List',	'excerpt',	'alone',	0,	'private',	0,	0,	'http://image.example.com',	'This is comment of image.'),
(715,	154,	'music',	'2020-01-15 00:54:54',	'2020-01-15 00:54:54',	'Test List',	'excerpt',	'alone',	0,	'private',	0,	0,	'http://music.example.com',	'This is comment of music.');

DROP TABLE IF EXISTS `parts`;
CREATE TABLE `parts` (
                         `part_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                         `part_parent_id` bigint(20) unsigned NOT NULL,
                         `part_content_id` bigint(20) unsigned NOT NULL COMMENT 'ID of part node',
                         `part_content_order` int(15) unsigned NOT NULL DEFAULT '0' COMMENT 'Order of part node',
                         `part_content_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'image' COMMENT 'Type of part node',
                         PRIMARY KEY (`part_id`),
                         KEY `part_node_id` (`part_parent_id`),
                         KEY `part_content_id` (`part_content_id`),
                         CONSTRAINT `parts_ibfk_2` FOREIGN KEY (`part_parent_id`) REFERENCES `nodes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                         CONSTRAINT `parts_ibfk_3` FOREIGN KEY (`part_content_id`) REFERENCES `nodes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `texts`;
CREATE TABLE `texts` (
                         `text_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                         `text_node_id` bigint(20) unsigned NOT NULL,
                         `text_content` text COLLATE utf8mb4_unicode_ci NOT NULL,
                         PRIMARY KEY (`text_id`),
                         UNIQUE KEY `text_node_id` (`text_node_id`),
                         CONSTRAINT `texts_ibfk_2` FOREIGN KEY (`text_node_id`) REFERENCES `nodes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


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

INSERT INTO `users` (`id`, `user_name`, `user_pass`, `user_email`, `user_status`) VALUES
(154,	'TestUser',	'*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5',	'test@example.com',	'activated');

DROP TABLE IF EXISTS `videos`;
CREATE TABLE `videos` (
                          `video_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                          `video_node_id` bigint(20) unsigned NOT NULL,
                          `video_url` varchar(511) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `video_format` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
                          PRIMARY KEY (`video_id`),
                          UNIQUE KEY `video_node_id` (`video_node_id`),
                          CONSTRAINT `videos_ibfk_2` FOREIGN KEY (`video_node_id`) REFERENCES `nodes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 2020-01-15 08:03:45
