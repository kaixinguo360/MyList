
DROP TABLE IF EXISTS "dlists";
DROP TABLE IF EXISTS "images";
DROP TABLE IF EXISTS "musics";
DROP TABLE IF EXISTS "texts";
DROP TABLE IF EXISTS "videos";
DROP TABLE IF EXISTS links;
DROP TABLE IF EXISTS "nodes";
DROP TABLE IF EXISTS "users";
DROP TABLE IF EXISTS "options";

CREATE TABLE "users" (
                         "id" bigserial,
                         "user_name" varchar(60) NOT NULL DEFAULT '',
                         "user_pass" varchar(255) NOT NULL DEFAULT '',
                         "user_email" varchar(100) DEFAULT NULL,
                         "user_status" varchar(20) NOT NULL DEFAULT 'activated',
                         PRIMARY KEY ("id")
);
CREATE UNIQUE INDEX "user_name" ON "users" ("user_name");
CREATE INDEX "user_name_2" ON "users" ("user_name","user_pass");

CREATE TABLE "options" (
                           "option_id" bigserial,
                           "option_name" varchar(255) NOT NULL,
                           "option_value" text,
                           PRIMARY KEY ("option_id")
);
CREATE UNIQUE INDEX "option_name" ON "options" ("option_name");

CREATE TABLE "nodes" (
                         "id" bigserial,
                         "node_user" bigint NOT NULL,
                         "node_type" varchar(20) NOT NULL DEFAULT 'list',
                         "node_ctime" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         "node_mtime" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         "node_title" text,
                         "node_excerpt" text,
                         "node_part" boolean NOT NULL DEFAULT '0',
                         "node_collection" boolean NOT NULL DEFAULT '0',
                         "node_permission" varchar(20) NOT NULL DEFAULT 'private',
                         "node_nsfw" boolean NOT NULL DEFAULT '0',
                         "node_like" boolean NOT NULL DEFAULT '0',
                         "node_hide" boolean NOT NULL DEFAULT '0',
                         "node_source" varchar(1024) DEFAULT NULL,
                         "node_description" text,
                         "node_comment" text,
                         PRIMARY KEY ("id"),
                         CONSTRAINT "nodes_ibfk_1" FOREIGN KEY ("node_user") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE INDEX "node_ctime" ON "nodes" ("node_ctime");
CREATE INDEX "node_mtime" ON "nodes" ("node_mtime");
CREATE INDEX "node_user" ON "nodes" (node_user);
COMMENT ON COLUMN "nodes"."node_ctime" IS 'Create Time';
COMMENT ON COLUMN "nodes"."node_mtime" IS 'Modify Time';
COMMENT ON COLUMN "nodes"."node_part" IS 'Is a part';
COMMENT ON COLUMN "nodes"."node_collection" IS 'Is a collection';

CREATE TABLE "links" (
                         "id" bigserial,
                         "parent_id" bigint NOT NULL,
                         "content_id" bigint NOT NULL,
                         "content_index" integer NOT NULL DEFAULT '0',
                         PRIMARY KEY ("id"),
                         CONSTRAINT "links_ibfk_2" FOREIGN KEY ("parent_id") REFERENCES "nodes" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
                         CONSTRAINT "links_ibfk_3" FOREIGN KEY ("content_id") REFERENCES "nodes" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE INDEX "link_parent_id" ON links (parent_id);
CREATE INDEX "link_content_id" ON links (content_id);
COMMENT ON COLUMN links.parent_id IS 'ID of parent';
COMMENT ON COLUMN links.content_id IS 'ID of content';
COMMENT ON COLUMN links.content_index IS 'Order of content';

CREATE TABLE "images" (
                          "id" bigint NOT NULL,
                          "image_url" varchar(1024) NOT NULL,
                          "image_type" varchar(20) DEFAULT NULL,
                          "image_author" text DEFAULT NULL,
                          "image_gallery" text DEFAULT NULL,
                          "image_source" varchar(1024) DEFAULT NULL,
                          PRIMARY KEY ("id"),
                          CONSTRAINT "images_ibfk_3" FOREIGN KEY ("id") REFERENCES "nodes" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE "musics" (
                          "id" bigint NOT NULL,
                          "music_url" varchar(1024) NOT NULL,
                          "music_format" varchar(20) NOT NULL,
                          PRIMARY KEY ("id"),
                          CONSTRAINT "musics_ibfk_2" FOREIGN KEY ("id") REFERENCES "nodes" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE "texts" (
                         "id" bigint NOT NULL,
                         "text_content" text NOT NULL,
                         PRIMARY KEY ("id"),
                         CONSTRAINT "texts_ibfk_4" FOREIGN KEY ("id") REFERENCES "nodes" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE "videos" (
                          "id" bigint NOT NULL,
                          "video_url" varchar(1024) NOT NULL,
                          "video_format" varchar(20) NOT NULL,
                          PRIMARY KEY ("id"),
                          CONSTRAINT "videos_ibfk_2" FOREIGN KEY ("id") REFERENCES "nodes" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE "dlists" (
                          "id" bigint NOT NULL,
                          "dlist_filter" text NOT NULL,
                          "dlist_config" text NOT NULL,
                          PRIMARY KEY ("id"),
                          CONSTRAINT "dlists_ibfk_2" FOREIGN KEY ("id") REFERENCES "nodes" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE OR REPLACE PROCEDURE "clean_all"() AS $body$
BEGIN
    DELETE FROM "dlists" WHERE true;
    DELETE FROM "images" WHERE true;
    DELETE FROM "musics" WHERE true;
    DELETE FROM "texts" WHERE true;
    DELETE FROM "videos" WHERE true;
    DELETE FROM links WHERE true;
    DELETE FROM "nodes" WHERE true;
    DELETE FROM "users" WHERE true;
    DELETE FROM "options" WHERE true;
END $body$
LANGUAGE 'plpgsql';

-- INSERT INTO "users" VALUES (1,'test','test','','activated');

-- 2021-05-19 15:54:48
