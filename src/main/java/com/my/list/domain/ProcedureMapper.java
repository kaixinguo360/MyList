package com.my.list.domain;

import com.my.list.type.image.Image;
import com.my.list.type.music.Music;
import com.my.list.type.text.Text;
import com.my.list.type.video.Video;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

@Mapper
public interface ProcedureMapper {

    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_image(" +
        "#{node.id,mode=OUT,jdbcType=BIGINT}," +
        "#{node.user,mode=IN}," +
        "#{node.title,mode=IN}," +
        "#{node.excerpt,mode=IN}," +
        "#{node.linkDelete,mode=IN}," +
        "#{node.linkVirtual,mode=IN}," +
        "#{node.permissions,mode=IN}," +
        "#{node.nsfw,mode=IN}," +
        "#{node.like,mode=IN}," +
        "#{node.sourceUrl,mode=IN}," +
        "#{node.comment,mode=IN}," +
        "#{image.url,mode=IN}," +
        "#{image.description,mode=IN}" +
        ")")
    void add_image(Node node, Image image);
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_list(" +
        "#{id,mode=OUT,jdbcType=BIGINT}," +
        "#{user,mode=IN}," +
        "#{title,mode=IN}," +
        "#{excerpt,mode=IN}," +
        "#{linkDelete,mode=IN}," +
        "#{linkVirtual,mode=IN}," +
        "#{permissions,mode=IN}," +
        "#{nsfw,mode=IN}," +
        "#{like,mode=IN}," +
        "#{sourceUrl,mode=IN}," +
        "#{comment,mode=IN}" +
        ")")
    void add_list(Node node);
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_music(" +
        "#{node.id,mode=OUT,jdbcType=BIGINT}," +
        "#{node.user,mode=IN}," +
        "#{node.title,mode=IN}," +
        "#{node.excerpt,mode=IN}," +
        "#{node.linkDelete,mode=IN}," +
        "#{node.linkVirtual,mode=IN}," +
        "#{node.permissions,mode=IN}," +
        "#{node.nsfw,mode=IN}," +
        "#{node.like,mode=IN}," +
        "#{node.sourceUrl,mode=IN}," +
        "#{node.comment,mode=IN}," +
        "#{music.url,mode=IN}," +
        "#{music.format,mode=IN}" +
        ")")
    void add_music(Node node, Music music);
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_node(" +
        "#{id,mode=OUT,jdbcType=BIGINT}," +
        "#{user,mode=IN}," +
        "#{type,mode=IN}," +
        "#{title,mode=IN}," +
        "#{excerpt,mode=IN}," +
        "#{linkDelete,mode=IN}," +
        "#{linkVirtual,mode=IN}," +
        "#{permissions,mode=IN}," +
        "#{nsfw,mode=IN}," +
        "#{like,mode=IN}," +
        "#{sourceUrl,mode=IN}," +
        "#{comment,mode=IN}" +
        ")")
    void add_node(Node node);

    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_part(" +
        "#{parent_id,mode=IN}," +
        "#{part_id,mode=IN}" +
        ")")
    void add_part(Long parent_id, Long part_id); // Auto call update_lcount().
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_text(" +
        "#{node.id,mode=OUT,jdbcType=BIGINT}," +
        "#{node.user,mode=IN}," +
        "#{node.title,mode=IN}," +
        "#{node.excerpt,mode=IN}," +
        "#{node.linkDelete,mode=IN}," +
        "#{node.linkVirtual,mode=IN}," +
        "#{node.permissions,mode=IN}," +
        "#{node.nsfw,mode=IN}," +
        "#{node.like,mode=IN}," +
        "#{node.sourceUrl,mode=IN}," +
        "#{node.comment,mode=IN}," +
        "#{text.content,mode=IN}" +
        ")")
    void add_text(Node node, Text text);
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_user(" +
        "#{id,mode=OUT,jdbcType=BIGINT}," +
        "#{name,mode=IN}," +
        "#{pass,mode=IN}," +
        "#{email,mode=IN}," +
        "#{status,mode=IN}" +
        ")")
    void add_user(User node);
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("" +
        "call add_video(" +
        "#{node.id,mode=OUT,jdbcType=BIGINT}," +
        "#{node.user,mode=IN}," +
        "#{node.title,mode=IN}," +
        "#{node.excerpt,mode=IN}," +
        "#{node.linkDelete,mode=IN}," +
        "#{node.linkVirtual,mode=IN}," +
        "#{node.permissions,mode=IN}," +
        "#{node.nsfw,mode=IN}," +
        "#{node.like,mode=IN}," +
        "#{node.sourceUrl,mode=IN}," +
        "#{node.comment,mode=IN}," +
        "#{video.url,mode=IN}," +
        "#{video.format,mode=IN}" +
        ")")
    void add_video(Node node, Video video);

    @Options(statementType= StatementType.CALLABLE)
    @Results(value = {
        @Result(property = "name", column = "user_name"),
        @Result(property = "pass", column = "user_pass"),
        @Result(property = "email", column = "user_email"),
        @Result(property = "status", column = "user_status")
    })
    @Select("call check_user(#{name,mode=IN}, #{pass,mode=IN})")
    User check_user(String name, String pass);
    @Options(statementType= StatementType.CALLABLE)
    @Select("call clean_all()")
    void clean_all();
    @Options(statementType= StatementType.CALLABLE)
    @Select("call clean_list(#{id})")
    void clean_list(Long list_id); // Call delete_part() for each part, don't call clean_nodes().
    @Options(statementType= StatementType.CALLABLE)
    @Select("call clean_nodes()")
    void clean_nodes();
    @Options(statementType= StatementType.CALLABLE)
    @Select("call delete_list(#{id})")
    void delete_list(Long list_id); // Call clean_list() and clean_nodes(), then delete the list node.
    @Options(statementType= StatementType.CALLABLE)
    @Select("call delete_part(#{id})")
    void delete_part(Long part_id); // Auto call update_lcount().
    @Options(statementType= StatementType.CALLABLE)
    @Select("call update_lcount(#{id})")
    void update_lcount(Long node_id);
}
