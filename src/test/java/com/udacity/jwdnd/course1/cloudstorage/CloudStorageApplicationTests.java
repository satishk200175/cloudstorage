package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import org.openqa.selenium.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();

		signupPage = new SignupPage(driver);
		loginPage = new LoginPage(driver);
		homePage = new HomePage(driver);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testApplicationAccess() {
		//Login Page
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		//access signup page
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		//Try to access home page
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	public void testAuthentication(){

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		signupPage.doSignup("me", "me", "me", "me");
		WebElement error = null;
		try{
			error = driver.findElement(By.id("error-msg"));
		}catch(Exception e){
			System.out.println("error mssage element not found");
		}
		Assertions.assertNull(error);

		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		loginPage.performLogin("me", "me");

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Home", driver.getTitle());

		homePage.logout();
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());

	}

	@Test
	public void testNotes() throws Exception{

		/**
		 *	Perform Sign-up
		 */

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		signupPage.doSignup("noteme", "noteme", "noteme", "noteme");
		WebElement error = null;
		try{
			error = driver.findElement(By.id("error-msg"));
		}catch(Exception e){
			System.out.println("User sign-up complete");
		}
		Assertions.assertNull(error);

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
		delayLoad(1000);
		driver.findElement(By.xpath("//*[@id=\"nav-notes-tab\"]")).click();
		delayLoad(1000);
		homePage.addNote();
		delayLoad(1000);
		homePage.submitNote("title1", "description 1");
		delayLoad(1000);
		driver.findElement(By.xpath("//*[@id=\"nav-notes-tab\"]")).click();
		delayLoad(1000);
		Assertions.assertEquals("title1", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th/span")).getText());
		Assertions.assertEquals("description 1", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]/span")).getText());

		/**
		 * 	View the just created Note
		 */
		homePage.editNote("title2", "description 2");
		Assertions.assertEquals("title2", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th/span")).getText());
		Assertions.assertEquals("description 2", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]/span")).getText());

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

	@Test
	public void testCredentials(){
		/**
		 *	Perform Sign-up
		 */

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		signupPage.doSignup("credme", "credme", "credme", "credme");
		WebElement error = null;
		try{
			error = driver.findElement(By.id("error-msg"));
		}catch(Exception e){
			System.out.println("User sign-up complete");
		}
		Assertions.assertNull(error);

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
		Assertions.assertEquals("www.google.com", driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/th/span")).getText());
		Assertions.assertEquals("credme", driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[2]/span")).getText());
		Assertions.assertNotNull(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[3]/span")).getText());

		/**
		 * 	verify Credentials
		 */
		homePage.viewCredential();
		Assertions.assertEquals("www.google.com", driver.findElement(By.xpath("//*[@id=\"credential-url\"]")).getAttribute("value"));
		Assertions.assertEquals("credme", driver.findElement(By.id("credential-username")).getAttribute("value"));
		Assertions.assertEquals("credme", driver.findElement(By.id("credential-password")).getAttribute("value"));
		homePage.closeCredModelView();

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

	private void delayLoad(long millsec) throws Exception{
		Thread.sleep(millsec);
	}

}
