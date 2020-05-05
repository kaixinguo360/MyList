insert into images(
    id,
    user,
    ctime,
    mtime,
    page_title,
    page_url,
    image_title,
    image_url
) select
      nodes.id,
      node_user,
      node_ctime,
      node_mtime,
      node_title,
      node_source,
      node_description,
      image_url
from old_images join nodes on nodes.id = old_images.id;

insert into tags(
    id,
    user,
    ctime,
    mtime,
    name,
    description
) select
      nodes.id,
      node_user,
      node_ctime,
      node_mtime,
      node_title,
      node_description
from nodes
where node_type = "tag";

insert into image_tags(
    image_id,
    tag_id
) select
      part_content_id,
      part_parent_id
from parts
         join nodes parent on parent.id = part_parent_id
         join nodes content on content.id = part_content_id
where parent.node_type = "tag"
  and content.node_type = "image";

insert ignore into image_tags(
    image_id,
    tag_id
) select
      content.id,
      tag.id
from nodes tag
         join parts p1 on tag.id = p1.part_parent_id
         join nodes list on list.id = p1.part_content_id
         join parts p2 on list.id = p2.part_parent_id
         join nodes content on content.id = p2.part_content_id
where tag.node_type = "tag"
  and content.node_type = "image";
