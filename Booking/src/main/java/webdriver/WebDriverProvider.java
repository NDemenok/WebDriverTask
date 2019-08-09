package webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverProvider {

	public static WebDriver driverInstance;
	
	public static WebDriver getDriverInstance()
	 {
	  if (driverInstance == null)
	  {
	   driverInstance = new ChromeDriver();
	  }
	  return driverInstance
	 }
}
