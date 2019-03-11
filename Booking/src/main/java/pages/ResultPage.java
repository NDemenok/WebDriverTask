package pages;

import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage extends BasePage {

	@FindBy(xpath = "//div[@id='search_results_table']//a[contains(@class, 'b-button')]")
	public List<WebElement> resultsOfSearch;

	@FindBy(xpath = "//*[@id='sort_by']/ul/li[3]/a")
	private WebElement filterByMaxRating;

	@FindBy(xpath = "//div[@id='hotellist_inner']/div[1]//a[contains(@class, 'b-button')]")
	private WebElement bestOffer;

	@FindBy(xpath = ".//div[@class='bui-review-score__badge']")
	private WebElement fieldRaiting;

	@FindBy(xpath = "(//strong/b)[1]")
	private WebElement fieldPrice;

	@FindBy(xpath = ".//*[@id='filter_mealplan']/div[@class='filteroptions']/a[@data-id='mealplan-1']")
	private WebElement filterIncludedBreakfast;

	@FindBy(xpath = ".//*[@id='filter_fc']/div[@class='filteroptions']/a[@data-id='fc-2']")
	private WebElement filterFreeCancel;

	@FindBy(xpath = ".//*[@id='filter_fc']/div[@class='filteroptions']/a[@data-id='fc-4']")
	private WebElement filterBookingWithoutCreditCard;

	@FindBy(xpath = ".//sup")
	public List<WebElement> offersWithBreakfast;

	@FindBy(xpath = "//div[@id='filter_price']/div[2]//a[1]")
	public WebElement filterByLowestPriceRange;

	@FindBy(xpath = "//div[@id='filter_price']/div[2]//a[last()]")
	public WebElement filterByHighestPriceRange;

	@FindBy(xpath = "//a[@data-category='price']")
	public WebElement filterLowestPriceFirst;

	public ResultPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void waitForUpdate() {
		fluentWait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				List<WebElement> alerts = driver
						.findElements(By.xpath("//div[@class='sr-usp-overlay sr-usp-overlay--wide']"));
				if (alerts.size() == 0) {
					return true;
				}
				return false;
			}
		});
	}

	public void chooseBestOffer() {
		waitForUpdate();
		bestOffer.click();
	}

	public String getRating() {
		driver.switchTo();
		return fieldRaiting.getText();
	}

	public int getPrice() {
		driver.switchTo();
		String priceString = fieldPrice.getText();
		priceString = priceString.replaceAll("[A-Za-z]", "").replaceAll("\\s", "");
		int price = Integer.parseInt(priceString);
		return price;
	}

	public void getOffersWithBreakfast() {
		filterIncludedBreakfast.click();
		waitForUpdate();
	}

	public void getFilterByMaxRating() {
		filterByMaxRating.click();
		waitForUpdate();
	}

	public void getFilterByMinPriceRange() {
		filterByLowestPriceRange.click();
		waitForUpdate();
	}

	public void getFilterByMaxPriceRange() {
		filterByHighestPriceRange.click();
		waitForUpdate();
	}

	public void lowestPriceFirst() {
		filterLowestPriceFirst.click();
		waitForUpdate();
	}

	public void getFilterByFreeCancel() {
		filterFreeCancel.click();
		waitForUpdate();
	}

	public void getFilterWithoutCreditCard() {
		filterBookingWithoutCreditCard.click();
		waitForUpdate();
	}
}
