package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.HashService;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialWorkflowTests {

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
    public void testAddCredentials() throws Exception{


        /**
         * 	Perform Login
         */
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        loginPage.performLogin("credme", "credme");
        Assertions.assertEquals("Home", driver.getTitle());

        /**
         * 	Create new Credential
         */
        homePage.addNewCredential("www.google.com", "credme", "credme");
        Assertions.assertEquals("www.google.com", driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[2]/th/span")).getText());
        Assertions.assertEquals("credme", driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[2]/td[2]/span")).getText());
        Assertions.assertNotNull(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[2]/td[3]/span")).getText());

    }

    @Test
    public void testEditNotes(){

        /**
         * 	Perform Login
         */
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        loginPage.performLogin("credme", "credme");
        Assertions.assertEquals("Home", driver.getTitle());

        /**
         * 	verify Credentials
         */
        homePage.viewCredential();
        Assertions.assertEquals("www.udacity.com", driver.findElement(By.xpath("//*[@id=\"credential-url\"]")).getAttribute("value"));
        Assertions.assertEquals("satish", driver.findElement(By.id("credential-username")).getAttribute("value"));
        Assertions.assertEquals("udacity", driver.findElement(By.id("credential-password")).getAttribute("value"));
        homePage.closeCredModelView();

    }

    @Test
    public void testDeleteNotes(){
        /**
         * 	Perform Login
         */
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        loginPage.performLogin("credme", "credme");
        Assertions.assertEquals("Home", driver.getTitle());

        /**
         * 	Delete the just viewed Credential
         */
        homePage.deleteCredential();
        WebElement urlElement = null;
        try{
            urlElement = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/th/span"));
        } catch(NoSuchElementException e){
            System.out.println("Element not found - - And its good");
        }
        Assertions.assertNull(urlElement);
    }

    private void clearData() throws Exception{
        conn.createStatement().executeUpdate("DELETE FROM CREDENTIALS ");
        conn.createStatement().executeUpdate("DELETE FROM USERS WHERE username='credme'");
        conn.commit();
    }

    private void setupData() throws Exception{

        HashService hashService = new HashService();
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue("credme", encodedSalt);

        byte[] key = new byte[16];
        EncryptionService encryptionService = new EncryptionService();
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String password = "udacity";
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        conn.createStatement().executeUpdate("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES('credme', '"+encodedSalt+"', '"+hashedPassword+"', 'credme', 'credme')");
        ResultSet rs = conn.createStatement().executeQuery("SELECT userid from USERS where username='credme'");
        int userid = 0;
        if(rs.next()){
            userid=rs.getInt("userid");
        }
        conn.createStatement().executeUpdate("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES ('www.udacity.com', 'satish','"+encodedKey+"','"+encryptedPassword+"','"+userid+"')");
    }
}
