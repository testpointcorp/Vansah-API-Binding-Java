import org.openqa.selenium.WebDriver;

public class TestMaster
{

	private static WebDriver driver;
	static GetBrowserInstance browser=new GetBrowserInstance();
	
	
	public static void main (String[] args) throws Exception
	{

		TestMaster1 test1 = new TestMaster1();
		
		
		TestMaster1.main(args);
		
		
	}

}




