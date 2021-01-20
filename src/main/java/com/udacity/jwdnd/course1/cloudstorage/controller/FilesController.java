package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class FilesController {

    private UserService userService;
    private FileService fileService;

    public FilesController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ModelAndView fileUpload(@RequestParam("fileUpload")MultipartFile file, ModelMap attributes, Authentication authentication) throws IOException {

        String uploadError = null;

        User user = userService.getUser(authentication.getName());

        if(file.isEmpty()){
            attributes.addAttribute("uploadError", "Please select a file to upload.");
        } else if(fileService.readFile(user.getUserId(), file.getOriginalFilename())!=null) {
            attributes.addAttribute("uploadError", "Uploaded file already exists. Please upload another file");
        } else{
            CloudFile cloudFile = new CloudFile(null, file.getOriginalFilename(), file.getContentType(), file.getSize(), user.getUserId(), file.getBytes());
            fileService.saveFile(cloudFile);
        }
        return new ModelAndView("forward:/home", attributes);
    }

    @GetMapping("/delete")
    public ModelAndView fileDelete(@RequestParam("fileName")String fileName, ModelMap attributes, Authentication authentication){

        User user = userService.getUser(authentication.getName());
        fileService.deleteFile(user.getUserId(), fileName);
        return new ModelAndView("forward:/home", attributes);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> fileDownload(@RequestParam("fileName")String fileName, Authentication authentication){

        User user = userService.getUser(authentication.getName());
        CloudFile file = fileService.readFile(user.getUserId(), fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFileName()+"\"")
                .body(new ByteArrayResource(file.getFileData()));
    }
}
