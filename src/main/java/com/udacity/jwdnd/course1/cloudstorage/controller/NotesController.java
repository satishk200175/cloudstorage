package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
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
public class NotesController {

    private UserService userService;
    private NoteService noteService;

    public NotesController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/savenote")
    public ModelAndView saveNote(
            @RequestParam("noteId")Integer noteId,
            @RequestParam("noteTitle")String noteTitle, @RequestParam("noteDescription")String noteDesc,
            ModelMap attributes, Authentication authentication) throws IOException {

        String uploadError = null;

        User user = userService.getUser(authentication.getName());
        if(noteId == null || noteId.toString().length()==0){
            Note note = new Note(null, noteTitle, noteDesc, user.getUserId());
            noteService.saveNote(note);
        } else {
            System.out.println("Note ID: "+noteId);
            Note note = new Note(noteId, noteTitle, noteDesc, user.getUserId());
            noteService.updateNote(note);
        }

        return new ModelAndView("forward:/home", attributes);
    }

    @GetMapping("/deletenote")
    public ModelAndView deleteNote(@RequestParam("noteId")Integer noteId, ModelMap attributes, Authentication authentication){

        User user = userService.getUser(authentication.getName());
        noteService.deleteNote(user.getUserId(), noteId);
        return new ModelAndView("forward:/home", attributes);
    }

}
