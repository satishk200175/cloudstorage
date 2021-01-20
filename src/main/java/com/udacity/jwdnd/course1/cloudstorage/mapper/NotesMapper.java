package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId} and noteid = #{noteId}")
    Note getNote(Integer userId, Integer noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{title}, #{description}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE NOTES SET notetitle=#{title}, notedescription=#{description} WHERE noteid=#{noteId} AND userid=#{userId}")
    int update(Note note);

    @Select("SELECT * FROM NOTES where userid=#{userId}")
    List<Note> getAllNotes(Integer userId);

    @Delete("DELETE FROM NOTES WHERE userid=#{userId} AND noteid=#{noteId}")
    void deleteNote(Integer userId, Integer noteId);

}
