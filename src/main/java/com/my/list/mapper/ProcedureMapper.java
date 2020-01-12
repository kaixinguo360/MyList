package com.my.list.mapper;

import com.my.list.bean.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

public interface ProcedureMapper {

    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_image(" +
        "#{node.id,mode=OUT,jdbcType=BIGINT}," +
        "#{node.user,mode=IN}," +
        "#{node.title,mode=IN}," +
        "#{node.excerpt,mode=IN}," +
        "#{node.lstatus,mode=IN}," +
        "#{node.permissions,mode=IN}," +
        "#{node.nsfw,mode=IN}," +
        "#{node.like,mode=IN}," +
        "#{node.source_url,mode=IN}," +
        "#{node.comment,mode=IN}," +
        "#{image.url,mode=IN}," +
        "#{image.description,mode=IN}" +
        ")")
    void add_image(Node node, Image image);
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_list(" +
        "#{id,mode=OUT,jdbcType=BIGINT}," +
        "#{user,mode=IN}," +
        "#{type,mode=IN}," +
        "#{title,mode=IN}," +
        "#{excerpt,mode=IN}," +
        "#{lstatus,mode=IN}," +
        "#{permissions,mode=IN}," +
        "#{nsfw,mode=IN}," +
        "#{like,mode=IN}," +
        "#{source_url,mode=IN}," +
        "#{comment,mode=IN}" +
        ")")
    void add_list(Node node);
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_music(" +
        "#{node.id,mode=OUT,jdbcType=BIGINT}," +
        "#{node.user,mode=IN}," +
        "#{node.title,mode=IN}," +
        "#{node.excerpt,mode=IN}," +
        "#{node.lstatus,mode=IN}," +
        "#{node.permissions,mode=IN}," +
        "#{node.nsfw,mode=IN}," +
        "#{node.like,mode=IN}," +
        "#{node.source_url,mode=IN}," +
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
        "#{lstatus,mode=IN}," +
        "#{permissions,mode=IN}," +
        "#{nsfw,mode=IN}," +
        "#{like,mode=IN}," +
        "#{source_url,mode=IN}," +
        "#{comment,mode=IN}" +
        ")")
    void add_node(Node node);
    
//    void add_part(Node node);
    
    @Options(statementType= StatementType.CALLABLE)
    @Select("call add_text(" +
        "#{node.id,mode=OUT,jdbcType=BIGINT}," +
        "#{node.user,mode=IN}," +
        "#{node.title,mode=IN}," +
        "#{node.excerpt,mode=IN}," +
        "#{node.lstatus,mode=IN}," +
        "#{node.permissions,mode=IN}," +
        "#{node.nsfw,mode=IN}," +
        "#{node.like,mode=IN}," +
        "#{node.source_url,mode=IN}," +
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
        "#{node.lstatus,mode=IN}," +
        "#{node.permissions,mode=IN}," +
        "#{node.nsfw,mode=IN}," +
        "#{node.like,mode=IN}," +
        "#{node.source_url,mode=IN}," +
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
    @Select("call clean_list(#1)")
    void clean_list(Long list_id);
    @Options(statementType= StatementType.CALLABLE)
    @Select("call clean_nodes()")
    void clean_nodes();
    @Options(statementType= StatementType.CALLABLE)
    @Select("call delete_list(#1)")
    void delete_list(Long list_id);
    @Options(statementType= StatementType.CALLABLE)
    @Select("call delete_part(#1)")
    void delete_part(Long part_id);
    @Options(statementType= StatementType.CALLABLE)
    @Select("call update_lcount(#1)")
    void update_lcount(Long node_id);
}
