


import java.util.HashMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

// import testpoint.StreamTest;

public class GetBrowserInstance {

  protected WebDriver driver;
  //protected StreamTest testPoint = new StreamTest();
  /*
   * This function will Initiate the type of browser defined in constants file and will create the
   * browser instance accordingly
   */
  public WebDriver getDriver(String browserName) {
    if (browserName.equalsIgnoreCase("chrome")) {
      driver = initChromeDriver();
      // driver = initChromeDriverIncognito();
    } else if (browserName.equalsIgnoreCase("firefox")) {
      driver = initFirefoxDriver();
    }
    return driver;
  }
  /*
   * This function will create the Chrome browser instance In Incognito mode and return the driver
   */
  public WebDriver initChromeDriverIncognito() {
    System.setProperty("webdriver.chrome.driver", "Lib\\chromedriver.exe");
    // DesiredCapabilities capabilities = DesiredCapabilities.chrome();
    // TO CHANGE DOWNLOAD PATH
    HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
    ChromeOptions options = new ChromeOptions();
    options.setExperimentalOption("prefs", chromePrefs);
    options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
    options.addArguments("disable-infobars");
    options.addArguments("incognito");
    options.addArguments("disable-extensions");
    options.addArguments("--start-maximized");

    driver = new ChromeDriver(options);
    return driver;

  }

  /*
   * This function will create the Chrome browser instance and return the driver
   */
  public WebDriver initChromeDriver() {
    System.setProperty("webdriver.chrome.driver", "Lib\\chromedriver.exe");
    HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
    ChromeOptions options = new ChromeOptions();
    options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
    options.setExperimentalOption("prefs", chromePrefs);
    options.addArguments("disable-infobars");
    options.addArguments("disable-extensions");
    options.addArguments("--start-maximized");
    options.addArguments("--incognito");
    
    driver = new ChromeDriver(options);

    return driver;
  }



  /*
   * This function will create the firefox browser instance and return the driver.
   */

  public WebDriver initFirefoxDriver() {
    System.setProperty("webdriver.gecko.driver", "Lib\\geckodriver.exe");
    driver = new FirefoxDriver();
    return driver;
  }

}

