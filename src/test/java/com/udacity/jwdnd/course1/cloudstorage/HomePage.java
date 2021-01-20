package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "upload-button")
    private WebElement uploadButton;

    @FindBy(xpath = "//*[@id=\"nav-notes\"]/button[contains(text(),'+ Add a New Note')]")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputNoteDescription;

    @FindBy(id = "note-submit")
    private WebElement noteSubmitButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    public HomePage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }


    public void logout(){
        logoutButton.click();
    }

    public void getNotesTab(){
        notesTab.click();
    }

    public void addNote(){

        addNoteButton.click();
    }

    public void submitNote(String title, String description){

        inputNoteDescription.sendKeys(description);
        inputNoteTitle.sendKeys(title);
        noteSubmitButton.click();
    }
}
