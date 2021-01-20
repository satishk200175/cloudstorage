package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class CredentialController {

    private UserService userService;
    private CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping("/savecred")
    public ModelAndView saveNote(
            @RequestParam("credentialId")Integer credentialId,
            @RequestParam("url")String url,
            @RequestParam("username")String userName,
            @RequestParam("password")String password,
            ModelMap attributes, Authentication authentication) throws IOException {


        User user = userService.getUser(authentication.getName());
        if(credentialId == null || credentialId.toString().length()==0){
            Credential credential = new Credential(null, url, userName, null, password, user.getUserId());
            credentialService.saveCredential(credential);
        } else {
            Credential credential = new Credential(credentialId, url, userName, null, password, user.getUserId());
            credentialService.updateCredential(credential);
        }

        return new ModelAndView("forward:/home", attributes);
    }

    @GetMapping("/deletecred")
    public ModelAndView deleteCredential(@RequestParam("credentialId")Integer credentialId, ModelMap attributes, Authentication authentication){

        User user = userService.getUser(authentication.getName());
        credentialService.deleteCredential(credentialId, user.getUserId());
        return new ModelAndView("forward:/home", attributes);
    }
}
