package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userId} and filename = #{fileName}")
    CloudFile getFile(Integer userId, String fileName);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(CloudFile file);

    @Select("SELECT * FROM FILES where userid=#{userId}")
    List<CloudFile> getAllFiles(Integer userId);

    @Delete("DELETE FROM FILES WHERE userid=#{userId} AND filename=#{fileName}")
    void deleteFile(Integer userId, String fileName);
}
