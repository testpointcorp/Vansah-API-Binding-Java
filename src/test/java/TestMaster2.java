import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.gargoylesoftware.htmlunit.WebClient;
import com.mashape.unirest.http.Unirest;
import testpoint.Vansah;

public class TestMaster2
{

	private static WebDriver driver;
	static GetBrowserInstance browser=new GetBrowserInstance();
	
	
	Vansah vansah = new Vansah();
	
	public static void main (String[] args) throws Exception
	{
	
			
	
		Vansah vansahApp = new Vansah();
		
		
		

		
	
	}

	
	public static void TearDown()
	{
		driver.close();
		driver.quit();
	}
	
}




