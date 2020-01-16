-- Adminer 4.6.2 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DELIMITER ;;

DROP PROCEDURE IF EXISTS `add_image`;;
CREATE PROCEDURE `add_image`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `link_delete` tinyint(1), IN `link_virtual` tinyint(1), IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4', IN `url` varchar(511) CHARACTER SET 'utf8mb4', IN `description` text CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'image',
            title,
            excerpt,
            link_delete,
            link_virtual,
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
CREATE PROCEDURE `add_list`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `link_delete` tinyint(1), IN `link_virtual` tinyint(1), IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'list',
            title,
            excerpt,
            link_delete,
            link_virtual,
            permissions,
            is_nsfw,
            is_like,
            source_url,
            comment
        );

    SET id = @node_id;

END;;

DROP PROCEDURE IF EXISTS `add_music`;;
CREATE PROCEDURE `add_music`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `link_delete` tinyint(1), IN `link_virtual` tinyint(1), IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4', IN `url` varchar(511) CHARACTER SET 'utf8mb4', IN `format` varchar(20) CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'music',
            title,
            excerpt,
            link_delete,
            link_virtual,
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
CREATE PROCEDURE `add_node`(OUT `id` bigint(20) unsigned, IN `user` bigint(10) unsigned, IN `type` varchar(20) CHARACTER SET 'utf8mb4', IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `link_delete` tinyint(1), IN `link_virtual` tinyint(1), IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4')
BEGIN

    INSERT INTO nodes(
        node_user,
        node_type,
        node_title,
        node_excerpt,
        node_link_delete,
        node_link_virtual,
        node_link_back,
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
             link_delete,
             link_virtual,
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

    CALL update_link_forward(parent);
    CALL update_link_back(part);

END;;

DROP PROCEDURE IF EXISTS `add_text`;;
CREATE PROCEDURE `add_text`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `link_delete` tinyint(1), IN `link_virtual` tinyint(1), IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4', IN `content` text CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'text',
            title,
            excerpt,
            link_delete,
            link_virtual,
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
CREATE PROCEDURE `add_video`(OUT `id` bigint(20) unsigned, IN `user` bigint(20) unsigned, IN `title` text CHARACTER SET 'utf8mb4', IN `excerpt` text CHARACTER SET 'utf8mb4', IN `link_delete` tinyint(1), IN `link_virtual` tinyint(1), IN `permissions` varchar(20) CHARACTER SET 'utf8mb4', IN `is_nsfw` tinyint(1), IN `is_like` tinyint(1), IN `source_url` varchar(511) CHARACTER SET 'utf8mb4', IN `comment` text CHARACTER SET 'utf8mb4', IN `url` varchar(511) CHARACTER SET 'utf8mb4', IN `format` varchar(20) CHARACTER SET 'utf8mb4')
BEGIN

    CALL add_node(
            @node_id,
            user,
            'video',
            title,
            excerpt,
            link_delete,
            link_virtual,
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

    DELETE FROM users WHERE true;
    DELETE FROM nodes WHERE true;
    DELETE FROM texts WHERE true;
    DELETE FROM images WHERE true;
    DELETE FROM musics WHERE true;
    DELETE FROM videos WHERE true;
    DELETE FROM parts WHERE true;

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
        node_link_back = 0
  AND
        node_link_delete = 1;;

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

    CALL update_link_back(part);

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

-- Add User2 --
    CALL add_user(
            @user2_id,
            'TestUser2',
            '1234567',
            'test2@example.com',
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
            0,
            0,
            'private',
            0,
            0,
            'http://list.example.com',
            'This is comment of list.'
        );

-- Add List2 --
    CALL add_list(
            @list2_id,
            @user_id,
            'Test List2',
            'excerpt',
            0,
            1,
            'private',
            0,
            0,
            'http://list.example.com',
            'This is comment of list2.'
        );

-- Add List3 --
    CALL add_list(
            @list3_id,
            @user2_id,
            'Test List3',
            'excerpt',
            0,
            0,
            'private',
            0,
            0,
            'http://list.example.com',
            'This is comment of list3.'
        );

-- Add Text --
    CALL add_text(
            @text_id,
            @user_id,
            'Test List',
            'excerpt',
            1,
            0,
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
            1,
            0,
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
            1,
            0,
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
            1,
            0,
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


    -- -- -- -- -- -- -- -- -- --
-- -- Add Parts To List -- --
    CALL add_part(@list_id, @text_id);
    CALL add_part(@list_id, @image_id);
    CALL add_part(@list_id, @music_id);
    CALL add_part(@list_id, @video_id);
    CALL add_part(@list_id, @video_id);

    -- Get List --
--  CALL get_list(@list_id);
    SELECT * FROM nodes;


    -- -- -- -- -- -- -- -- -- --
-- -- Add Parts To List2 -- --
    CALL add_part(@list2_id, @text_id);
    CALL add_part(@list2_id, @image_id);
    CALL add_part(@list2_id, @music_id);
    CALL add_part(@list2_id, @video_id);
    CALL add_part(@list2_id, @video_id);

    -- Get List2 --
--  CALL get_list(@list2_id);
    SELECT * FROM nodes;


    -- -- -- -- -- -- -- -- -- --
-- -- Add Parts To List3 -- --
    CALL add_part(@list3_id, @text_id);
    CALL add_part(@list3_id, @image_id);
    CALL add_part(@list3_id, @music_id);
    CALL add_part(@list3_id, @video_id);
    CALL add_part(@list3_id, @video_id);

    -- Get List3 --
--  CALL get_list(@list3_id);
    SELECT * FROM nodes;


    -- -- -- -- -- -- -- -- -- --
-- Delete List --
    CALL delete_list(
            @list_id
        );

-- Show All --
    CALL show_all();

END;;

DROP PROCEDURE IF EXISTS `update_link_back`;;
CREATE PROCEDURE `update_link_back`(IN `node_id` bigint(20) unsigned)
BEGIN

    SET @link_back = 0;

    SELECT COUNT(part_id) INTO @link_back
    FROM nodes parent
             JOIN parts ON part_parent_id = parent.id
             JOIN nodes content ON part_content_id = content.id
    WHERE
            content.id = node_id
      AND
            parent.node_user = content.node_user
      AND
            parent.node_link_virtual = 0
    GROUP BY
        part_content_id;

    UPDATE nodes SET
        node_link_back = @link_back
    WHERE
            id = node_id;

END;;

DROP PROCEDURE IF EXISTS `update_link_forward`;;
CREATE PROCEDURE `update_link_forward`(IN `node_id` bigint(20) unsigned)
BEGIN

    SET @link_forward = 0;

    SELECT COUNT(part_id) INTO @link_forward FROM parts
    WHERE
            part_parent_id = node_id
    GROUP BY
        part_parent_id;

    UPDATE nodes SET
        node_link_forward = @link_forward
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
                         `node_link_forward` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'Forward link count',
                         `node_link_back` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'Back link count',
                         `node_link_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT 'Auto delete when link_back = 0',
                         `node_link_virtual` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT 'Don''t influence link_back in linked node',
                         `node_permissions` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'private',
                         `node_nsfw` tinyint(1) NOT NULL DEFAULT '0',
                         `node_like` tinyint(1) NOT NULL DEFAULT '0',
                         `node_hide` tinyint(1) NOT NULL DEFAULT '0',
                         `node_source_url` varchar(511) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                         `node_comment` text COLLATE utf8mb4_unicode_ci,
                         PRIMARY KEY (`id`),
                         KEY `node_ctime` (`node_ctime`),
                         KEY `node_mtime` (`node_mtime`),
                         KEY `node_user` (`node_user`),
                         CONSTRAINT `nodes_ibfk_1` FOREIGN KEY (`node_user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `nodes` (`id`, `node_user`, `node_type`, `node_ctime`, `node_mtime`, `node_title`, `node_excerpt`, `node_link_forward`, `node_link_back`, `node_link_delete`, `node_link_virtual`, `node_permissions`, `node_nsfw`, `node_like`, `node_hide`, `node_source_url`, `node_comment`) VALUES
(138,	27,	'list',	'2020-01-16 23:33:23',	'2020-01-16 23:33:24',	'Test List2',	'excerpt',	5,	0,	0,	1,	'private',	0,	0,	0,	'http://list.example.com',	'This is comment of list2.'),
(139,	28,	'list',	'2020-01-16 23:33:23',	'2020-01-16 23:33:24',	'Test List3',	'excerpt',	5,	0,	0,	0,	'private',	0,	0,	0,	'http://list.example.com',	'This is comment of list3.');

DROP TABLE IF EXISTS `parts`;
CREATE TABLE `parts` (
                         `part_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                         `part_parent_id` bigint(20) unsigned NOT NULL COMMENT 'ID of parent node',
                         `part_content_id` bigint(20) unsigned NOT NULL COMMENT 'ID of content node',
                         `part_content_order` int(15) unsigned NOT NULL DEFAULT '0' COMMENT 'Order of content node',
                         `part_content_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'image' COMMENT 'Type of content node',
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
(27,	'TestUser',	'*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5',	'test@example.com',	'activated'),
(28,	'TestUser2',	'*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5',	'test2@example.com',	'activated');

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


-- 2020-01-16 15:34:40
