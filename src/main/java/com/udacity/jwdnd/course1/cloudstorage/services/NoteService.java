package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NotesMapper notesMapper;

    public NoteService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public int saveNote(Note note){
        return notesMapper.insert(note);
    }

    public int updateNote(Note note){
        return notesMapper.update(note);
    }

    public Note readNote(Integer userId, Integer noteId){
        return notesMapper.getNote(userId, noteId);
    }

    public List<Note> listNotes(Integer userId){
        return notesMapper.getAllNotes(userId);
    }

    public int deleteNote(Integer userId, Integer noteId) {
        return notesMapper.deleteNote(userId, noteId);
    }
}
