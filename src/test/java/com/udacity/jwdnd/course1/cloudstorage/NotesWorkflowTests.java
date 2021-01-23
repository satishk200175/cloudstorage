package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.HashService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotesWorkflowTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;

    private SignupPage signupPage;
    private LoginPage loginPage;
    private HomePage homePage;
    private static Connection conn;

    @BeforeAll
    static void beforeAll() throws Exception{
        System.out.println("Before All");
        WebDriverManager.chromedriver().setup();
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
    }

    @BeforeEach
    public void beforeEach() throws Exception{
        System.out.println("Before Each");
        this.driver = new ChromeDriver();

        signupPage = new SignupPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        setupData();

    }

    @AfterEach
    public void afterEach() throws Exception {
        System.out.println("After Each");
        if (this.driver != null) {
            driver.quit();
        }
        clearData();
    }

    @Test
    public void testAddNotes() throws Exception{


        /**
         * 	Perform Login
         */
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        loginPage.performLogin("noteme", "noteme");
        Assertions.assertEquals("Home", driver.getTitle());

        /**
         *  Create a New Note
         */
        homePage.getNotesTab();
        homePage.submitNote("title 2", "description 2");
        homePage.getNotesTab();
        Assertions.assertEquals("title 2", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[2]/th/span")).getText());
        Assertions.assertEquals("description 2", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[2]/td[2]/span")).getText());
    }

    @Test
    public void testEditNotes(){
        /**
         * 	Perform Login
         */
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        loginPage.performLogin("noteme", "noteme");
        Assertions.assertEquals("Home", driver.getTitle());

        /**
         * 	View the just created Note
         */
        homePage.editNote("title 2", "description 2");
        Assertions.assertEquals("title 2", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th/span")).getText());
        Assertions.assertEquals("description 2", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]/span")).getText());

    }

    @Test
    public void testDeleteNotes(){
        /**
         * 	Perform Login
         */
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        loginPage.performLogin("noteme", "noteme");
        Assertions.assertEquals("Home", driver.getTitle());

        /**
         * 	Delete the just edited Note
         */
        homePage.deleteNote();
        WebElement titleElement = null;
        try{
            titleElement = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th/span"));
        } catch(NoSuchElementException e){
            System.out.println("Element not found - - And its good");
        }
        Assertions.assertNull(titleElement);
    }

    private void setupData() throws SQLException {
        HashService hashService = new HashService();
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue("noteme", encodedSalt);
        conn.createStatement().executeUpdate("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES('noteme', '"+encodedSalt+"', '"+hashedPassword+"', 'noteme', 'noteme')");
        ResultSet rs = conn.createStatement().executeQuery("SELECT userid from USERS where username='noteme'");
        int userid = 0;
        if(rs.next()){
            userid=rs.getInt("userid");
        }
        conn.createStatement().executeUpdate("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES ('title 1', 'description 1', "+userid+")");
    }

    private void clearData() throws SQLException {
        conn.createStatement().executeUpdate("DELETE FROM NOTES ");
        conn.createStatement().executeUpdate("DELETE FROM USERS WHERE username='noteme'");
        conn.commit();
    }


}
