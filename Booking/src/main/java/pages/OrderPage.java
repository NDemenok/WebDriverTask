package pages;

import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class OrderPage extends BasePage{

	@FindBy(xpath = "(//button[@id='hp_book_now_button'])[1]")
	public WebElement reserveButton;
		
	@FindBy(xpath = "(//td//select)[1]")
	public WebElement roomsSelect;

	@FindBy(xpath = ".//button[contains(text(),'бронирую')]")
	public WebElement bookingButton;
	
	@FindBy(xpath = "//input[@id='lastname']")
	public WebElement lastNameInput;

	@FindBy(xpath = "//input[@id='email']")
	public WebElement emailInput;

	@FindBy(xpath = "//input[@id='email_confirm']")
	public WebElement confirmEmailInput;

	@FindBy(xpath = "//button[@name='book']")
	public WebElement finalDetailsButton;

	@FindBy(xpath = "//*[@id=\"cc1\"]")
	public WebElement сountrySelect;
	
	@FindBy(xpath = "//*[@id=\"booker_title\"]")
	private WebElement bookerTitleSelect;
	
	@FindBy(xpath = "//input[@id='phone']")
	public WebElement phoneInput;

	@FindBy(xpath = "//*[@id=\"cc_name\"]")
	public WebElement ccName;
	
	@FindBy(xpath = "//select[@id='cc_type']")
	public WebElement ccTypeSelect;

	@FindBy(xpath = "//input[@id='cc_number']")
	public WebElement ccNumberInput;

	@FindBy(xpath = "//select[@id='cc_month']")
	public WebElement expMonthSelect;

	@FindBy(xpath = "//select[@id='ccYear']")
	public WebElement expYearSelect;

	@FindBy(xpath = "//input[@id='cc_cvc']")
	public WebElement cvcCodeInput;

	@FindBy(xpath = "//button[contains(@class, 'bp-overview-buttons-submit')]")
	public WebElement compliteBookingButton;
	
	@FindBy(xpath = "//div[@class='mltt__legs--current mltt__legs js__mltt__scroll_here']")
	private WebElement bookingConfirmation;
	
	public OrderPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public WebElement waitForAlert() {
		WebElement waitingAlert = fluentWait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.xpath("//p[text()='Please try using a different credit card.']"));
			}
		});
		return waitingAlert;
	}
	
	public void fillFormPersonalInformation(String appeal, String lastName, String login, 
			String country,	String phoneNumber) {
		driver.switchTo();
		driver.switchTo().defaultContent();
		reserveButton.click();
		new Select(roomsSelect).selectByValue("1");
		bookingButton.click();
		new Select(bookerTitleSelect).selectByValue(appeal);
		lastNameInput.clear();
		lastNameInput.sendKeys(lastName);
		emailInput.clear();
		emailInput.sendKeys(login);
		confirmEmailInput.clear();
		confirmEmailInput.sendKeys(login);
		finalDetailsButton.click();
		new Select(сountrySelect).selectByVisibleText(country);
		phoneInput.clear();
		phoneInput.sendKeys(phoneNumber);
	}
	
	public void fillFormCardData(String lastName, String ccType, String cardNumber,
			String expMonth, String expYear, String cvcCode) {
		ccName.clear();
		ccName.sendKeys(lastName);
		new Select(ccTypeSelect).selectByVisibleText(ccType);
		ccNumberInput.clear();
		ccNumberInput.sendKeys(cardNumber);
		new Select(expMonthSelect).selectByValue(expMonth);
		new Select(expYearSelect).selectByValue(expYear);
		cvcCodeInput.sendKeys(cvcCode);
	}

	public boolean verifyInvalidCardAlert() {
		return waitForAlert().isDisplayed();
	}
	
	public void bookRooms() {
		compliteBookingButton.click();
	}
	
	public boolean confirmationPresents() {
		return bookingConfirmation.isDisplayed();
	}
}
