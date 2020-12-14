import org.openqa.selenium.WebDriver;
import testpoint.Vansah;

public class TestMaster1
{
	private static WebDriver driver;
	static GetBrowserInstance browser=new GetBrowserInstance();
	
	public static void main (String[] args) throws Exception
	{
	
		String VANSAH_CYCLE = "93";
	    String VANSAH_CASE = "68";
		String VANSAH_RELEASE = "R1";
		String VANSAH_BUILD = "B1";
		String VANSAH_ENVIRONMENT = "TEST";	
		
		Vansah vansah = new Vansah();
		
		driver = browser.getDriver("chrome");
		driver.get("https://testpoint.com.au");
		
		vansah.addTestLog(VANSAH_CYCLE, VANSAH_CASE, VANSAH_RELEASE, VANSAH_BUILD, VANSAH_ENVIRONMENT);
		vansah.quickTestUpdate(2, "Passed", 2, null, true, driver);
		//vansah.quickTestUpdate(1, "Failed.", true, driver);
		//vansah.quickTestUpdate(2, "Passed.", true, driver);
		//vansah.quickTestUpdate(3, "Passed.", true, driver);

		TearDown();
	}
	
	public static void TearDown()
	{
		driver.close();
		driver.quit();
	}

}
