package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class CloudHomeController {

    private UserService userService;
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;

    public CloudHomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String cloudHomeView(Authentication authentication, Model model){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("fileList", fileService.listFiles(user.getUserId()));
        model.addAttribute("noteList", noteService.listNotes(user.getUserId()));
        model.addAttribute("credList", credentialService.getAllCredentials(user.getUserId()));
        return "home";
    }

    @PostMapping()
    public String getCloudHome(Authentication authentication, Model model){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("fileList", fileService.listFiles(user.getUserId()));
        model.addAttribute("noteList", noteService.listNotes(user.getUserId()));
        model.addAttribute("credList", credentialService.getAllCredentials(user.getUserId()));
        return "home";
    }

    /*
    @PostMapping()
    public String fileUpload(@RequestParam("fileUpload") MultipartFile file, Model attributes, Authentication authentication) throws IOException {

        String uploadError = null;
        if(file.isEmpty()){
            uploadError = "Please select a file to upload.";
        } else {

            User user = userService.getUser(authentication.getName());

            CloudFile cloudFile = new CloudFile(null, file.getName(), file.getContentType(), file.getSize(), user.getUserId(), file.getBytes());
            fileService.saveFile(cloudFile);
            if (uploadError == null) {
                attributes.addAttribute("uploadSuccess", true);
            } else {
                attributes.addAttribute("uploadError", uploadError);
            }
        }
        return "home";
    }*/
}
