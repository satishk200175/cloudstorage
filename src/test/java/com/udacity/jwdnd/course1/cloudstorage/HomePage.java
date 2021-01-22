package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    private long DELAY_TIME = 1000;

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "upload-button")
    private WebElement uploadButton;

    //@FindBy(xpath = "//*[@id=\"nav-notes\"]/button[contains(text(),'+ Add a New Note')]")
    @FindBy(id = "add-new-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputNoteDescription;

    @FindBy(id = "note-submit")
    private WebElement noteSubmitButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "edit-note-button")
    private WebElement editNoteButton;

    @FindBy(xpath = "//*[@id=\"userTable\"]/tbody/tr/td[1]/a")
    private WebElement deleteNoteButton;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "add-cred-button")
    private WebElement addCredButton;

    @FindBy(id = "credential-url")
    private WebElement inputCredUrl;

    @FindBy(id = "credential-username")
    private WebElement inputCredUsername;

    @FindBy(id = "credential-password")
    private WebElement inputCredPassword;

    @FindBy(xpath = "//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]")
    private WebElement saveCredButton;

    @FindBy(id = "edit-credential")
    private WebElement editCredButton;

    @FindBy(xpath = "//*[@id=\"credentialModal\"]/div/div/div[3]/button[1]")
    private WebElement closeCredModelView;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[1]/a")
    private WebElement deleteCredButton;

    public HomePage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void addNewCredential(String url, String username, String password){
        delayLoad(DELAY_TIME);
        credentialsTab.click();
        delayLoad(DELAY_TIME);
        addCredButton.click();
        delayLoad(DELAY_TIME);
        inputCredUrl.sendKeys(url);
        inputCredUsername.sendKeys(username);
        inputCredPassword.sendKeys(password);
        saveCredButton.click();
        delayLoad(DELAY_TIME);
        credentialsTab.click();
        delayLoad(DELAY_TIME);
    }

    public void viewCredential(){
        editCredButton.click();
        delayLoad(DELAY_TIME);
    }

    public void closeCredModelView(){
        closeCredModelView.click();
        delayLoad(DELAY_TIME);
    }

    public void deleteCredential(){
        delayLoad(DELAY_TIME);
        deleteCredButton.click();
        delayLoad(DELAY_TIME);
        credentialsTab.click();
        delayLoad(DELAY_TIME);

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

    public void editNote(String title, String description){
        delayLoad(DELAY_TIME);
        editNoteButton.click();
        delayLoad(DELAY_TIME);
        inputNoteTitle.clear();
        inputNoteTitle.sendKeys(title);
        inputNoteDescription.clear();
        inputNoteDescription.sendKeys(description);
        noteSubmitButton.click();
        delayLoad(DELAY_TIME);
        notesTab.click();
        delayLoad(DELAY_TIME);
    }

    public void deleteNote(){
        delayLoad(DELAY_TIME);
        notesTab.click();
        delayLoad(DELAY_TIME);
        deleteNoteButton.click();
        delayLoad(DELAY_TIME);
        notesTab.click();
        delayLoad(DELAY_TIME);
    }

    private void delayLoad(long millsec){
        try{
            Thread.sleep(millsec);
        } catch(Exception e){
            System.out.println("Error sleeping");
            e.printStackTrace();
        }

    }
}
