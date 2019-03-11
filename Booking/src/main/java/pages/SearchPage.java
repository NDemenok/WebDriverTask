package pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class SearchPage extends BasePage {

	@FindBy(xpath = "//*[@id='ss']")
	private WebElement fieldSearch;

	@FindBy(xpath = "//*[@id='frm']//div[@data-visible='accommodation,flights,rentalcars']/descendant::div[contains(@class,'checkin')]")
	private WebElement fieldCalendar;

	@FindBy(xpath = "//label[@id='xp__guests__toggle']")
	public WebElement guestsDropDown;

	// Guests and rooms with + / - buttons.

	@FindBy(xpath = "//div[contains(@class, 'sb-group__field-rooms')]//button[1]")
	public WebElement roomsMinusButton;

	@FindBy(xpath = "//div[contains(@class, 'sb-group__field-rooms')]//button[2]")
	public WebElement roomsPlusButton;

	@FindBy(xpath = "//div[contains(@class, 'sb-group__field-rooms')]//span[contains(@class, 'display')]")
	public WebElement roomsCount;

	@FindBy(xpath = "//div[contains(@class, 'sb-group__field-adults')]//button[1]")
	public WebElement adultsMinusButton;

	@FindBy(xpath = "//div[contains(@class, 'sb-group__field-adults')]//button[2]")
	public WebElement adultsPlusButton;

	@FindBy(xpath = "//div[contains(@class, 'sb-group__field-adults')]//span[contains(@class, 'display')]")
	public WebElement adultsCount;

	@FindBy(xpath = "//div[contains(@class, 'sb-group-children')]//button[1]")
	public WebElement childrenMinusButton;

	@FindBy(xpath = "//div[contains(@class, 'sb-group-children')]//button[2]")
	public WebElement childrenPlusButton;

	@FindBy(xpath = "//div[contains(@class, 'sb-group-children')]//span[contains(@class, 'display')]")
	public WebElement childrenCount;

	// Guests and rooms with selects.

	@FindBy(xpath = "//select[@id='no_rooms']")
	public WebElement roomsSelect;

	@FindBy(xpath = "//select[@id='no_rooms']")
	public List<WebElement> roomsSelects;

	@FindBy(xpath = "//select[@id='group_adults']")
	public WebElement adultsSelect;

	@FindBy(xpath = "//select[@id='group_children']")
	public WebElement childrenSelect;

	@FindBy(xpath = "//button/span[text()='Проверить цены']")
	private WebElement buttonSearch;

	public SearchPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void inputCity(String city) {
		fluentWait.until(ExpectedConditions.visibilityOf(fieldSearch));
		fieldSearch.clear();
		fieldSearch.sendKeys(city);
	}

	public void setDate(int delta) {
		fieldCalendar.click();
		String inDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		driver.findElement(By.xpath("//td[@data-date='" + inDate + "']")).click();
		String outDate = LocalDate.now().plusDays(1 + delta).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		driver.findElement(By.xpath("//td[@data-date='" + outDate + "']")).click();
	}

	public void getInformationAboutGuests(int rooms, int adults, int children) {
		boolean pageWithSelect = roomsSelects.size() > 0 ? true : false;
		guestsDropDown.click();
		if (pageWithSelect) {
			setValueBySelects(roomsSelect, rooms);
			setValueBySelects(adultsSelect, adults);
			setValueBySelects(childrenSelect, children);
		} else {
			setValueByButtons(roomsCount, roomsPlusButton, roomsMinusButton, rooms);
			setValueByButtons(adultsCount, adultsPlusButton, adultsMinusButton, adults);
			setValueByButtons(childrenCount, childrenPlusButton, childrenMinusButton, children);
		}
	}

	public void setValueByButtons(WebElement counter, WebElement plusButton, WebElement minusButton, int value) {
		int currentValue = Integer.parseInt(counter.getText());
		int desiredValue = value;
		while (currentValue != desiredValue) {
			if (currentValue > desiredValue) {
				minusButton.click();
			} else {
				plusButton.click();
			}
			currentValue = Integer.parseInt(counter.getText());
		}
	}

	public void setValueBySelects(WebElement select, int value) {
		new Select(select).selectByVisibleText(String.valueOf(value));
	}

	public void pushSearchButton() {
		buttonSearch.click();
	}
}
