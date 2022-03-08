package addCustomer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Add_Customer_TestNg {

	WebDriver driver;
	String browser;
	String url;

	// by class lib
	By usernameField = By.xpath("//input[@id='username']");
	By passwordield = By.xpath("//input[@id='password']");
	By dashBoardfield = By.xpath(" //h2[contains(text(),'Dashboard')]");
	By customerField = By.xpath("//span[contains(text(),'Customers')]");
	By addcustomerField = By.xpath("//a[contains(text(),'Add Customer')]");
	By addContactPageField = By.xpath("//h5[contains(text(),'Add Contact')]");
	By fullnameField = By.xpath("//input[@id='account']");
	By companyField = By.xpath("//select[@id='cid']");
	By emailField = By.xpath("//input[@id='email']");
	By phoneField = By.xpath("//input[@id='phone']");
	By addressField = By.xpath("//input[@id='address']");
	By cityField = By.xpath("//input[@id='city']");
	By stateField = By.xpath("//input[@id='state']");
	By zipField = By.xpath("//input[@id='zip']");
	By countryField = By.xpath("//select[@id='country']");
	By tagField = By.xpath("//select[@id='tags']");
	By currencyField = By.xpath("//select[@id='currency']");
	By groupfield = By.xpath("//select[@id='group']");
	By welcomeMailField = By.xpath("//input[@id='send_client_signup_email']/following::div[1]");
	By saveButtonField = By.xpath("//button[@id='submit']");
	By savePageField = By.xpath("//a[contains(text(),'Summary')]");
	By contactsPageField = By.xpath("//h2[contains(text(),'Contacts')]");
	By searchfield = By.xpath("//input[@id='foo_filter']");

	// sigin data
	String userID = "demo@techfios.com";
	String password = "abc123";

	@BeforeTest
	public void readConfig() {
		String path = "src/main/java/config/config.properties";
		Properties prop = new Properties();
		try {
			InputStream input = new FileInputStream(path);
			prop.load(input);
			browser = prop.getProperty("browser");
			url = prop.getProperty("url");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
			driver = new FirefoxDriver();
		}
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority = 1)
	public void loginTest() {

		String dashboard = "Dashboard";
		driver.findElement(usernameField).sendKeys(userID);
		driver.findElement(passwordield).sendKeys(password + Keys.ENTER);
		String dashboardActual = driver.findElement(dashBoardfield).getText();
		Assert.assertEquals(dashboardActual, dashboard, "Not on DashBoard page");
	}

	@Test(priority = 2)
	public void addCustomer() throws InterruptedException {
		String dashboard = "Dashboard";
		driver.findElement(usernameField).sendKeys(userID);
		driver.findElement(passwordield).sendKeys(password + Keys.ENTER);
		String dashboardActual = driver.findElement(dashBoardfield).getText();
		Assert.assertEquals(dashboardActual, dashboard, "Not on DashBoard page");

		driver.findElement(customerField).click();
		driver.findElement(addcustomerField).click();

		// Checking if we are on the contact page
		waitForElement(driver, 6, addContactPageField);
		String addContactPageExpected = "Add Contact";
		String addContactActualPg = driver.findElement(addContactPageField).getText();
		Assert.assertEquals(addContactActualPg, addContactPageExpected, "Not on add contact page");

		// generating random number so that everytime we add contact its a unique one
		// add contact details
		int randomNum = randomGeneratedNumber(999);
		String fullName = "Sep Batch" + randomNum;
		driver.findElement(fullnameField).sendKeys(fullName);
		// selecting option from company drop down
		selectDropDown(companyField, "Techfios");
		driver.findElement(emailField).sendKeys("Sep_Batch_" + randomNum + "@test.com");
		driver.findElement(phoneField).sendKeys(randomNum + "4567888");
		driver.findElement(addressField).sendKeys("88, carlton ave");
		driver.findElement(cityField).sendKeys("Frisco");
		driver.findElement(stateField).sendKeys("Texas");
		driver.findElement(zipField).sendKeys("08854");
		selectDropDown(countryField, "United States");
		selectDropDown(tagField, "My tags");
		selectDropDown(currencyField, "USD");
		selectDropDown(groupfield, "September_QA_2021");
		driver.findElement(welcomeMailField).click();
		driver.findElement(saveButtonField).click();

		// check if we are on the save page
		String expectedSavePage = "Summary";
		String actualSavePage = driver.findElement(savePageField).getText();
		Assert.assertEquals(actualSavePage, expectedSavePage, "Not on Right Page");

		By customerField1 = By.xpath("//span[contains(text(),'Customers')]");
		WebElement customersElement = driver.findElement(customerField1);
		customersElement.click();

		By listCustomerField1 = By.xpath("//a[contains(text(),'List Customers')]");
		WebElement listElement = driver.findElement(listCustomerField1);
		// waitForElement(driver, 10, listCustomerField1);
		listElement.click();

		// contact page verification
		String expectedContactPage = "Contacts";
		String actualListPage = driver.findElement(contactsPageField).getText();
		Assert.assertEquals(actualListPage, expectedContactPage, "Not on Right Page");

		By addedContactdetailsField = By
				.xpath("//a[normalize-space()=" + "\'" + fullName + "\'" + "]/following::td[4]/a[1]");
		waitForElement(driver, 6, addedContactdetailsField);

		WebElement webTableElement = driver.findElement(addedContactdetailsField);
		webTableElement.click();
		driver.navigate().back();

		waitForElement(driver, 5, searchfield);
		driver.findElement(searchfield).sendKeys(fullName);
		

	}

	private void selectDropDown(By locator, String visibleText) {
		Select sel = new Select(driver.findElement(locator));
		sel.selectByVisibleText(visibleText);
	}

	public void waitForElement(WebDriver driver, int waitTime, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

	}

	public int randomGeneratedNumber(int randomnumber) {
		Random rand = new Random();
		int num = rand.nextInt(999);
		return num;
	}

	@AfterMethod
	public void teardown() {
		driver.close();
		driver.quit();
	}
}
