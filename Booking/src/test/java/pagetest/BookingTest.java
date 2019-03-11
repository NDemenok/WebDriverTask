package pagetest;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.OrderPage;
import pages.ResultPage;
import pages.SearchPage;
import propertyprovider.PropertyProvider;
import webdriver.WebDriverProvider;

public class BookingTest extends BaseTest {

	private LoginPage loginPage;
	private SearchPage searchPage;
	private ResultPage resultPage;
	private OrderPage orderPage;
	private double expectedRating = 9.0;
	private int maxPriceByNightInLowerRange = 240;
	private int minPriceByNightInHigherRange = 960;

	@BeforeClass
	public void beforeClass() {
		setUpDriver();
		WebDriver driver = WebDriverProvider.getDriverInstance();
		loginPage = new LoginPage(driver);
		searchPage = new SearchPage(driver);
		resultPage = new ResultPage(driver);
		orderPage = new OrderPage(driver);
		LOG.info("Starting tests");
	}

	@BeforeMethod
	public void setInfirmation() {
		cleanCookies();
		goHomePage();
		searchPage.inputCity(PropertyProvider.getProperty("city"));
		int tripDuration = Integer.parseInt(PropertyProvider.getProperty("tripDuration"));
		searchPage.setDate(tripDuration);
		searchPage.getInformationAboutGuests(2, 4, 0);
		searchPage.pushSearchButton();
	}

	@Test(priority = 0)
	public void availabilityHotelsOnSelectedDates() {
		Assert.assertTrue(resultPage.resultsOfSearch.size() > 0, "There are not hotels on selected dates");
	}

	@Test(priority = 1)
	public void sortByMaxRatingTest() {
		resultPage.getFilterByMaxRating();
		resultPage.chooseBestOffer();
		String ratingString = resultPage.getRating();
		Double rating = Double.parseDouble(ratingString.replaceAll(",", "."));
		Assert.assertTrue(rating >= expectedRating, "The rating less than 9");
	}

	@Test(priority = 2)
	public void sortByPriceRangeAndChooseHotelWithLowerPriceTest() {
		resultPage.getFilterByMinPriceRange();
		resultPage.lowestPriceFirst();
		int price = resultPage.getPrice();
		int tripDuration = Integer.parseInt(PropertyProvider.getProperty("tripDuration"));
		Assert.assertTrue(price / tripDuration <= maxPriceByNightInLowerRange, "The price does not match the range");
	}

	@Test(priority = 3)
	public void sortByPriceRangeAndChooseHotelWithHighestPriceTest() {
		resultPage.getFilterByMaxPriceRange();
		int price = resultPage.getPrice();
		int tripDuration = Integer.parseInt(PropertyProvider.getProperty("tripDuration"));
		Assert.assertTrue(price / tripDuration >= minPriceByNightInHigherRange, "The price does not match the range");
	}

	@Test(priority = 4)
	public void sortByRatingAndIncudedBreakfast() {
		resultPage.getFilterByMaxRating();
		resultPage.getOffersWithBreakfast();
		Assert.assertFalse(resultPage.offersWithBreakfast.isEmpty(), "No offers with breakfast");
	}

	@Test(priority = 5)
	public void verifyInvalidCardAlertTest() {
		resultPage.getFilterByMaxRating();
		resultPage.chooseBestOffer();
		String appeal = PropertyProvider.getProperty("appeal");
		String lastName = PropertyProvider.getProperty("lastName");
		String login = PropertyProvider.getProperty("login");
		String country = PropertyProvider.getProperty("country");
		String phoneNumber = PropertyProvider.getProperty("phoneNumber");
		orderPage.fillFormPersonalInformation(appeal, lastName, login, country, phoneNumber);
		String ccType = PropertyProvider.getProperty("ccType");
		String cardNumber = PropertyProvider.getProperty("cardNumber");
		String expMonth = PropertyProvider.getProperty("expMonth");
		String expYear = PropertyProvider.getProperty("expYear");
		String cvcCode = PropertyProvider.getProperty("cvcCode");
		orderPage.fillFormCardData(lastName, ccType, cardNumber, expMonth, expYear, cvcCode);
		orderPage.bookRooms();
		Assert.assertTrue(orderPage.verifyInvalidCardAlert(), "Invalid card accepted");
	}

	@Test(priority = 6)
	public void successfulBookingTest() {
		resultPage.getFilterByMaxRating();
		resultPage.getFilterByFreeCancel();
		resultPage.getFilterWithoutCreditCard();
		resultPage.chooseBestOffer();
		String appeal = PropertyProvider.getProperty("appeal");
		String lastName = PropertyProvider.getProperty("lastName");
		String login = PropertyProvider.getProperty("login");
		String country = PropertyProvider.getProperty("country");
		String phoneNumber = PropertyProvider.getProperty("phoneNumber");
		orderPage.fillFormPersonalInformation(appeal, lastName, login, country, phoneNumber);
		orderPage.bookRooms();
		String password = PropertyProvider.getProperty("password");
		loginPage.enterLoginAndPass(login, password);
		Assert.assertTrue(orderPage.confirmationPresents(), "No booking confirmation");
	}
   
	@AfterClass
	public void afterClass() {
		tearDownDriver();
	}
}
