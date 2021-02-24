
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import testpoint.Vansah;



//#########################################################################################################

public class Script1 {
		
		//Get an instance of a browser
		GetBrowserInstance browser=new GetBrowserInstance();
		
		//Create key object. Key will contain all methods of actions
		Keywords key= new Keywords();

		//Get an instance of WebDriver
		private WebDriver driver;
		
		//Vansah properties (VANSAH CYCLE, BUILD, ENVIRONMENT, VANSAH REQUIREMENT ID, RELEASE, TESTCASE ID
		String build = Constants.BUILD;
		String environment = Constants.ENVIRONMENT;
		String release = Constants.RELEASE;
		String cycle = Constants.CYCLE;
		String requirement = "34";
		String testCase = "198";
		Vansah vansah = new Vansah();
		HashMap<String, String> testFields = new HashMap<String, String>();
		  

		//Vansah Test Step ID
		int testStepID;
		String testStep;
		
		
	//#########################################################################################################	
		@Test	
	public void Script1() throws Exception {
		try {
			
			this.driver = browser.getDriver(Constants.BROWSER);
			vansah.testScript((testCase));
			vansah.dataSet(testCase, Constants.CYCLE, Constants.ENVIRONMENT);
			vansah.sessionVariable(testCase, Constants.CYCLE, Constants.ENVIRONMENT);
	
			Map<String, List<String>> testData = new HashMap<String, List<String>>();
			
			testData = vansah.getTestData();

			System.out.println("TEST DATA TC 198 " + testData);
			
			for (int i = 0; i <= vansah.getNumberOfTestDataRows(); i++) {
				Map<Integer, String> testStepIDArray = vansah.getTestSteps();

				for (Map.Entry<Integer,String> entry : testStepIDArray.entrySet())  {
					
					System.out.println("Test Step   : "+entry.getValue()); 
					System.out.println("Test Step ID: "+ entry.getKey());
					
					//Call the Main Script
					StartTest(i, entry.getKey(),entry.getValue());
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	//#########################################################################################################

	
	public void StartTest(int i, int testStepID,String testStep) throws Exception{
			
			Map<String, List<String>> testData = new HashMap<String, List<String>>();
			
			
			testData = vansah.getTestData();
			testFields = vansah.getTestFields();
			String jobNumber = testFields.get("jobNumber" + i);

			this.testStep=testStep;
			this.testStepID=testStepID;
			switch (testStepID) {
			
			//Step 1 - ****** USE STEP ORDER INSTEAD OF TESTSTEPID
			case 1:
				System.out.println("TEST STEP 1");
				vansah.addTestLog(cycle, testCase, release, build, environment);
				this.driver.get(testData.get("URL").get(i));
				
				key.findElementClick(driver, By.xpath("//a[@class='find_more']"));
				System.out.println("FIELD 1" + testData.get("Field1").get(i));
				System.out.println("FIELD 2" + testData.get("Field1").get(i));
				System.out.println("FIELD 3" + testData.get("Field1").get(i));
				vansah.quickTestUpdate(2, "Passed", testStepID, null, true, driver);
			break;
			
			
			case 2:
				System.out.println("TEST STEP 2");
				vansah.quickTestUpdate(1, "Failed", testStepID, null, true, driver);
				
				driver.close();
				driver.quit();
				driver = null;
				
			break;
			
				

			default:
				vansah.quickTestUpdate(1, "Failed", 2, null, true, driver);
			}
		}

	}
