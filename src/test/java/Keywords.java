

import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Keywords extends GetBrowserInstance {
	//Keywords key= new Keywords();
	
	public void findElementClick(WebDriver driver, By locator) {
		try {
			waitElementUntilClickable(driver, locator);
			driver.findElement(locator).click();
		}catch (Exception e) {
			System.out.println("Click wasn't required.");
			System.out.println(e);
		}
	}
	
	public void findElementClickRITM(WebDriver driver, By locator) {
		
			waitElementUntilClickable(driver, locator);
			driver.findElement(locator).click();
	}
	
	public void findElementClickBack(WebDriver driver, By locator) {
		
			waitElementUntilClickable(driver, locator);
			driver.findElement(locator).click();
		
	
		
	}
	
	
	
	
	
	public void findElementClickTask(WebDriver driver, By locator) {
		try {
			waitElementUntilClickable(driver, locator);
			driver.findElement(locator).click();
		}catch (Exception e) {
			System.out.println("Click wasn't required. Probably because there were not anymore tasks to open.");
			System.out.println(e);
		}
	}
	
	
	public void pressTab(WebDriver driver, By locator) throws InterruptedException {
		Thread.sleep(3000);
		WebElement webElement = driver.findElement(locator);
		webElement.sendKeys(Keys.TAB);
		Thread.sleep(3000);
	}
	public void pressEnter(WebDriver driver, By locator) throws InterruptedException {
		Thread.sleep(2000);
		WebElement webElement = driver.findElement(locator);
		webElement.sendKeys(Keys.ENTER);
		Thread.sleep(3000);
	}
	
	public void waitUntilPageIsComplete(WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 60);// 1 minute 
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
		
	public void waitElementUntilClickable(WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Constants.LongWait);
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitElementUntilClickable(WebDriver driver, By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
	public void waitElementUntilDisplayed(WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Constants.LongWait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public void waitElementUntilDisplayedLongWait(WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Constants.veryLongWait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public boolean isElementPresent(WebDriver driver, By locator) {// ===Find elements presence
		try {
			WebDriverWait wait = new WebDriverWait(driver, Constants.LongWait);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			System.out.println(locator + " Not found");
			return false;
		}
	}
	public boolean isElementVisible(WebDriver driver, By locator) {// ===Find elements presence
		try {
			WebDriverWait wait = new WebDriverWait(driver, Constants.LongWait);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			System.out.println(locator + " Not found");
			return false;
		}
	}
	public boolean isElementVisibleShortWait(WebDriver driver, By locator) {// ===Find elements presence
		try {
			WebDriverWait wait = new WebDriverWait(driver, Constants.ShortWait);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			System.out.println(locator + " Not found");
			return false;
		}
	}
	

	
	
	public WebElement findElement(WebDriver driver, By locator) {
		waitElementUntilDisplayed(driver, locator);
		return driver.findElement(locator);
	}
	public void MoveMouseByAction(WebDriver driver, By locator) {
		Actions action = new Actions(driver);
		WebElement we = driver.findElement(locator);
		action.moveToElement(we).build().perform();

	}

	public void clickByAction(WebDriver driver, By locator) {
		Actions action = new Actions(driver);
		WebElement we = driver.findElement(locator);
		action.moveToElement(we).perform();
		action.click().build().perform();
	}

	public void moveToElementByJSE(WebDriver driver,By locator) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);",driver.findElement(locator));
	}

	public boolean verifyPageTitle(WebDriver driver,String pagetitle) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, Constants.LongWait);
			wait.until(ExpectedConditions.titleContains(pagetitle));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void findElementSendKey(WebDriver driver, By locator, String SendkeyData) {
		WebDriverWait wait = new WebDriverWait(driver, 15);// 1 minute 
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).clear();
		driver.findElement(locator).sendKeys(SendkeyData);
	}
	

	
	public void findElementSendKeyAlternativeWay(WebDriver driver, String id, String SendkeyData) {
		// Initialize JS object
		JavascriptExecutor JS = (JavascriptExecutor)driver;
		JS.executeScript("document.getElementById('" + id + "').value='" + SendkeyData + "'");
	}

	public boolean isFileDownloaded(String downloadPath, String fileName) {
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();

		for (int i = 0; i < dirContents.length; i++) {
			System.out.println(downloadPath);
			System.out.println(dirContents[i].getName());
			if (dirContents[i].getName().equals(fileName)) {

				// File has been found, it can now be deleted:
				dirContents[i].delete();

				return true;
			}
		}
		return false;
	}


	public void findElementClick(WebDriver driver, By locator, int timeout) {
		waitElementUntilClickable(driver, locator, timeout);
		driver.findElement(locator).click();
	}

	public void waitUntilInvisible(WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Constants.LongWait);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void waitUntilInvisible(WebDriver driver, By locator, int timer) {
		WebDriverWait wait = new WebDriverWait(driver, timer);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	
	// ================Switch to Frames via String/Integer/default======>
	public void switchToFrame(WebDriver driver, String frame) {
		driver.switchTo().frame(frame);
	}

	

	public void switchToDefault(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	// =============Scroll functions=======================

	public void scrollDownScreen(WebDriver driver) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0,500)", "");
	}

	public void scrollUpScreen(WebDriver driver) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0,-500)", "");
	}

	public void scrollToElementLocation(WebDriver driver, By locator) {
		String location = driver.findElement(locator).getLocation().toString();
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo" + location + "", "");
	}

	public void scrollIntoView(WebDriver driver, By locator) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(locator));

	}

	public void scrollToElementLocation(WebDriver driver, WebElement element) {
		String location = element.getLocation().toString();
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo" + location + "", "");
	}

	public void scrollIntoView(WebDriver driver, WebElement element) {

		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public String getAttributeByJse(WebDriver driver, By locator, String attributeName) {

		JavascriptExecutor je = (JavascriptExecutor) driver;
		String attribute = je.executeScript("return arguments[0].getAttribute('" + attributeName + "')", driver.findElement(locator))
				.toString();
		System.out.println("THE VALUE EXTRACTED WAS: " + attribute);
		return attribute;
	}


	public String getAttributeByJse(WebDriver driver, WebElement element, String attributeName) {

		JavascriptExecutor je = (JavascriptExecutor) driver;
		String attribute = je.executeScript("return arguments[0].getAttribute('" + attributeName + "')", element)
				.toString();
		return attribute;

	}
	public void setAttributeByJse(WebDriver driver, By locator, String AttributeName, String AttributeValue) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].setAttribute('" + AttributeName + "','" + AttributeValue + "')",
				driver.findElement(locator));

	}

	public String getElementPropertyByJse(WebDriver driver, By locator, String propertyName) {

		JavascriptExecutor je = (JavascriptExecutor) driver;
		String attribute = je.executeScript("return arguments[0]." + propertyName, driver.findElement(locator))
				.toString();
		return attribute;

	}

	public String getElementPropertyByJse(WebDriver driver, WebElement element, String propertyName) {

		JavascriptExecutor je = (JavascriptExecutor) driver;
		String attribute = je.executeScript("return arguments[0]." + propertyName, element).toString();
		return attribute;

	}
	
	public String getText(WebDriver driver, By locator, String AttributeName) {
		WebElement TxtBoxContent = driver.findElement(locator);
		String attribute = TxtBoxContent.getAttribute(AttributeName);
		//System.out.println("Printing " + attribute);
		return attribute; 
	}
	
	public String getTextByGetText(WebDriver driver, By locator) {
		String text = driver.findElement(locator).getText();
		
		//System.out.println("Printing " + attribute);
		return text; 
	}



	public String getFutureDate(int i) {
		LocalDateTime ldt = LocalDateTime.now().plusDays(i);
		DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
		return formmat1.format(ldt);
	}

	public String getCurrentDate() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
		return formmat1.format(ldt);
	}

	public void switchToNewWindow(WebDriver driver) {
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
		}

	}



	public void selectBoxSelectOption(WebDriver driver, By onlineUpdateInputBox, String onlineUpdate) {
		waitElementUntilDisplayed(driver, onlineUpdateInputBox);
		new Select(findElement(driver, onlineUpdateInputBox)).selectByVisibleText(onlineUpdate);
	}
	
	public void selectBoxSelectOptionHandledIfNot(WebDriver driver, By onlineUpdateInputBox, String onlineUpdate) {
		try {
			waitElementUntilDisplayed(driver, onlineUpdateInputBox);
			new Select(findElement(driver, onlineUpdateInputBox)).selectByVisibleText(onlineUpdate);
		} catch (Exception e) {
			System.out.println("Select wasn't required");
		}
	}

	public void selectBoxSelectOptionByValue(WebDriver driver, By onlineUpdateInputBox, String onlineUpdate) {
		waitElementUntilDisplayed(driver, onlineUpdateInputBox);
		new Select(findElement(driver, onlineUpdateInputBox)).selectByValue(onlineUpdate);
	}

	public void checkBoxOn(WebDriver driver, By locator) {
		waitElementUntilClickable(driver, locator);
		if(!driver.findElement(locator).isSelected()) {
			driver.findElement(locator).click();
		}
	}

	public void checkBoxOff(WebDriver driver, By locator) {
		waitElementUntilClickable(driver, locator);
		if(driver.findElement(locator).isSelected()) {
			driver.findElement(locator).click();
		}
	}


	public void closeTab(WebDriver driver) {
		driver.close();

	}

	public String generateRandomNumbers(int length) {
		String AB = "0123456789";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();

	}
	// Get future date.
	public static String getFutureDateByDays(String dateFormat, long forDays) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		LocalDate localDate = LocalDate.now();
		String future_Date = dtf.format(localDate.plusDays(forDays));
		return future_Date;
	}

	// Get Past date.
	public static String getPreviousDateByDays(String dateFormat, long forDays) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		LocalDate localDate = LocalDate.now();
		String previous_Date = dtf.format(localDate.minusDays(forDays));
		return previous_Date;
	}
	
	/**
	 * @desc User defined pattern Like: [dd-MM-yyyy, dd/MM/yyyy, dd MM yyyy]
	 * @param pattern
	 * @return current date with the specified date pattern
	 */
	public static String getCurrentDateAsD_M_Y(String pattern) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
		LocalDate localDate = LocalDate.now();
		String current_Date = dtf.format(localDate);
		return current_Date;
	}
}