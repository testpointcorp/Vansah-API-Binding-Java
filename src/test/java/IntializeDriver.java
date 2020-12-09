import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.gargoylesoftware.htmlunit.WebClient;


//Singelton class for Driver
public class IntializeDriver {
	
	public WebDriver driver;
	WebClient wclient=new WebClient();
	String baseURL="http://www.testpoint.com.au/";
	
	IntializeDriver initializedriver = null;
	protected IntializeDriver()
	{
		
	}
	
	public IntializeDriver getInstance()
	{
		if(initializedriver==null)
		{
			initializedriver= new IntializeDriver();
		}
		return initializedriver;
	}


	public WebDriver setUp() throws Exception {
		
	try{

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
		Capabilities capObj = ((RemoteWebDriver) driver).getCapabilities();
		System.out.println(" Browser Name is :" +capObj.getBrowserName());
		System.out.println("Browswe Version is :"+capObj.getVersion());
		driver.get(baseURL);
	}
	catch(WebDriverException e)
	{
		System.out.println("Web Driver Intialization Failed");
	}
	return driver;
	}

	
	public void TearDown()
	{
		driver.close();
		driver.quit();
	}
}
