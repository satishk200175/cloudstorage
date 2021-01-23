package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper){
        this.fileMapper = fileMapper;
    }

    public int saveFile(CloudFile file){
        return fileMapper.insert(file);
    }

    public CloudFile readFile(Integer userId, String fileName){
        return fileMapper.getFile(userId, fileName);
    }

    public List<CloudFile> listFiles(Integer userId){
        return fileMapper.getAllFiles(userId);
    }

    public int deleteFile(Integer userId, String fileName) {
        return fileMapper.deleteFile(userId, fileName);
    }
}
