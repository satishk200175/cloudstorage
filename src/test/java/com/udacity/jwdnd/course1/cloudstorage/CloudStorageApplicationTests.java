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

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		signupPage.doSignup("noteme", "noteme", "noteme", "noteme");
		WebElement error = null;
		try{
			error = driver.findElement(By.id("error-msg"));
		}catch(Exception e){
			System.out.println("error mssage element not found");
		}
		Assertions.assertNull(error);

		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		loginPage.performLogin("noteme", "noteme");

		Assertions.assertEquals("Home", driver.getTitle());


		//driver.findElement(By.xpath("//a[text()='Notes']")).click();
		driver.findElement(By.xpath("//*[@id=\"nav-notes-tab\"]")).click();

		homePage.addNote();

		homePage.submitNote("title1", "description 1");

		Assertions.assertEquals("title1", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th/span")).getText());

		Assertions.assertEquals("description 1", driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]/span")).getText());

	}

}
