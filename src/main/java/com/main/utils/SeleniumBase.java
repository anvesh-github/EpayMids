package com.main.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Throwables;

/**
 * Author : Heamanth Nath M A Company Name : TechMojo Project : E-Pay Services
 */

public class SeleniumBase {

	// log declaration
	public static Logger APPLICATION_LOGS = Logger.getLogger(SeleniumBase.class
			.getName());
	// Selenium variable declaration
	public static WebDriver driver = null;
	protected static InternetExplorerDriverService service;
	protected static JavascriptExecutor jse;
	protected static String browser = "";

	// Properties declaration
	public static Properties config = new Properties();
	public static Properties OR = new Properties();

	// Exception Handling
	protected static TMExceptionHandle tme = TMExceptionHandle.getInstance();
	String WinhandleBefore;

	public SeleniumBase() {
	}

	/**
	 * Method to invoke browser, mention parameter in string as firefox, chrome,
	 * ie
	 * 
	 * @param browser
	 */
	public void getBrowser(String browser) {
		try {

			APPLICATION_LOGS.info("Getting browserType property: " + browser);

			if (browser.equalsIgnoreCase("INTERNET_EXPLORER")) {
				APPLICATION_LOGS.info("Launching " + browser);
				// start internet explorer driver instance
				System.setProperty("webdriver.ie.driver",
						System.getProperty("user.dir")
								+ "\\drivers\\IEDriverServer.exe");
				service = new InternetExplorerDriverService.Builder()
						.usingAnyFreePort()
						.withLogFile(new File("./TestOutput/Logs/IEDriver.log"))
						.withLogLevel(InternetExplorerDriverLogLevel.TRACE)
						.build();
				try {
					service.start();
				} catch (IOException e) {
					throw Throwables.propagate(e);
				}

				DesiredCapabilities capability = DesiredCapabilities
						.internetExplorer();
				capability
						.setCapability(
								InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
								true);
				driver = new InternetExplorerDriver(service, capability);
				jse = (JavascriptExecutor) driver;

			} else if (browser.equalsIgnoreCase("CHROME")) {
				APPLICATION_LOGS.info("Launching " + browser);
				// start chrome driver instance
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir")
								+ "\\drivers\\chromedriver.exe");
				driver = new ChromeDriver();
				jse = (JavascriptExecutor) driver;

			} else if (browser.equalsIgnoreCase("FIREFOX")) {
				APPLICATION_LOGS.info("Launching " + browser);
				// start firefox driver instance
				FirefoxBinary binary = new FirefoxBinary(new File(
						"C:/Program Files/Mozilla Firefox/firefox.exe"));
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference(
						"capability.policy.default.Window.QueryInterface",
						"allAccess");
				profile.setPreference(
						"capability.policy.default.Window.frameElement.get",
						"allAccess");
				profile.setPreference(
						"capability.policy.default.HTMLDocument.compatMode.get",
						"allAccess");
				profile.setPreference(
						"capability.policy.default.Window.pageYOffset.get",
						"allAccess");
				profile.setPreference(
						"capability.policy.default.Window.mozInnerScreenY.get",
						"allAccess");
				profile.setPreference("network.proxy.type",
						ProxyType.AUTODETECT.ordinal());
				profile = changeDownloadPathForFireFox(profile);

				driver = new FirefoxDriver(binary, profile);
				jse = (JavascriptExecutor) driver;
			}
			driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void loadURL(String URL) {

		driver.get(URL);
	}

	/**
	 * Method to setup download path for browser firefox
	 * 
	 * @param profile
	 * @return
	 */
	protected FirefoxProfile changeDownloadPathForFireFox(FirefoxProfile profile) {
		return profile;
	}

	/**
	 * Getter method for the current driver being used for testing
	 * 
	 * @return
	 */
	public static WebDriver getDriverInstance() {
		return driver;
	}

	/**
	 * Getter method for the current browser being used for testing
	 * 
	 * @return the browser string being used for testing
	 */
	public static String getBroswer() {
		return browser;
	}

	public void tearDown() {

	}// End tearDown

	/**
	 * Method to find element based on the object type for a given object
	 * locator
	 * 
	 * @param driver
	 * @param objectLocator
	 * @return WebElement
	 * @throws TMExceptionHandle
	 * @throws IOException
	 */
	public static WebElement findElement(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {

		try {
			String[] splits = objectLocator.split("~");
			String objecttype = splits[0];
			APPLICATION_LOGS.info("obj type: " + objecttype);
			String objectvalue = splits[1];
			APPLICATION_LOGS.info("obj value: " + objectvalue);

			if (objecttype.equalsIgnoreCase("id")) {
				APPLICATION_LOGS.info("Element is found by " + objecttype);
				return driver.findElement(By.id(objectvalue));
			} else if (objecttype.equalsIgnoreCase("xpath")) {
				APPLICATION_LOGS.info("Element is found by " + objecttype);
				return driver.findElement(By.xpath(objectvalue));
			} else if (objecttype.equalsIgnoreCase("name")) {
				APPLICATION_LOGS.info("Element is found by " + objecttype);
				return driver.findElement(By.name(objectvalue));
			} else if (objecttype.equalsIgnoreCase("classname")) {
				APPLICATION_LOGS.info("Element is found by " + objecttype);
				return driver.findElement(By.className(objectvalue));
			} else if (objecttype.equalsIgnoreCase("tagname")) {
				APPLICATION_LOGS.info("Element is found by " + objecttype);
				return driver.findElement(By.tagName(objectvalue));
			} else if (objecttype.equalsIgnoreCase("css")) {
				APPLICATION_LOGS.info("Element is found by " + objecttype);
				return driver.findElement(By.cssSelector(objectvalue));
			} else if (objecttype.equalsIgnoreCase("linkText")) {
				APPLICATION_LOGS.info("Element is found by " + objecttype);
				return driver.findElement(By.linkText(objectvalue));
			} else if (objecttype.equalsIgnoreCase("partialLinkText")) {
				APPLICATION_LOGS.info("Element is found by " + objecttype);
				return driver.findElement(By.partialLinkText(objectvalue));
			}

		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		return null;
	}

	/**
	 * Method to find List of web elements based on the object type for a given
	 * object locator
	 * 
	 * @param driver
	 * @param object
	 * @return List<WebElement>
	 * @throws TMExceptionHandle
	 * @throws IOException
	 */
	public List<WebElement> findElements(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {

		try {
			String[] splits = objectLocator.split("~");
			String objecttype = splits[0];
			APPLICATION_LOGS.info("obj type: " + objecttype);
			String objectvalue = splits[1];
			APPLICATION_LOGS.info("obj val: " + objectvalue);

			if (objecttype.equalsIgnoreCase("id")) {
				APPLICATION_LOGS.info("Elements are found by " + objecttype);
				return driver.findElements(By.id(objectvalue));
			} else if (objecttype.equalsIgnoreCase("xpath")) {
				APPLICATION_LOGS.info("Elements are found by " + objecttype);
				return driver.findElements(By.xpath(objectvalue));
			} else if (objecttype.equalsIgnoreCase("name")) {
				APPLICATION_LOGS.info("Elements are found by " + objecttype);
				return driver.findElements(By.name(objectvalue));
			} else if (objecttype.equalsIgnoreCase("classname")) {
				APPLICATION_LOGS.info("Elements are found by " + objecttype);
				return driver.findElements(By.className(objectvalue));
			} else if (objecttype.equalsIgnoreCase("tagname")) {
				APPLICATION_LOGS.info("Elements are found by " + objecttype);
				return driver.findElements(By.tagName(objectvalue));
			} else if (objecttype.equalsIgnoreCase("css")) {
				APPLICATION_LOGS.info("Elements are found by " + objecttype);
				return driver.findElements(By.cssSelector(objectvalue));
			} else if (objecttype.equalsIgnoreCase("linkText")) {
				APPLICATION_LOGS.info("Elements are found by " + objecttype);
				return driver.findElements(By.linkText(objectvalue));
			} else if (objecttype.equalsIgnoreCase("partialLinkText")) {
				APPLICATION_LOGS.info("Elements are found by " + objecttype);
				return driver.findElements(By.partialLinkText(objectvalue));
			}
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}

		return null;
	}

	/**
	 * Explicitly wait for element to be clickable
	 * 
	 * @param driver
	 * @param objectLocator
	 * @param timeOutInSeconds
	 * @throws TMExceptionHandle
	 * @throws IOException
	 */
	public static void explicitWaitElementClickable(WebDriver driver,
			String objectLocator, int timeOutInSeconds)
			throws TMExceptionHandle {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			APPLICATION_LOGS.info("Waiting for " + timeOutInSeconds
					+ " seconds for the element to be clickable");
			wait.until(ExpectedConditions.elementToBeClickable(findElement(
					driver, objectLocator)));
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}

	}

	/**
	 * Explicitly wait for the element to be present
	 * 
	 * @param driver
	 * @param timeOutInSeconds
	 * @param objectLocator
	 * @throws TMExceptionHandle
	 * @throws IOException
	 */
	public void explicitWaitElementToBeVisible(WebDriver driver,
			String objectLocator, int timeOutInSeconds)
			throws TMExceptionHandle {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			APPLICATION_LOGS.info("Waiting for " + timeOutInSeconds
					+ " seconds for the element to be visible");
			wait.until(ExpectedConditions.visibilityOf(findElement(driver,
					objectLocator)));
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}
	}

	/**
	 * Method to get the Page Title
	 * 
	 * @param driver
	 * @return page title
	 */
	public String getPageTitle(WebDriver driver) throws TMExceptionHandle {
		String title = null;
		try {
			title = driver.getTitle();
			APPLICATION_LOGS.info("The page title is: " + title);
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		return title;
	}

	/**
	 * Method to get Text based on object locator
	 * 
	 * @param driver
	 * @param objectLocator
	 * @return elementText
	 * @throws TMExceptionHandle
	 * @throws IOException
	 */
	public String getText(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {
		String text = findElement(driver, objectLocator).getText();
		APPLICATION_LOGS.info("The text value of the element is: " + text);
		return text;
	}

	/**
	 * Method to get element Value based on object locator
	 * 
	 * @param driver
	 * @param objectLocator
	 * @return elementValue
	 * @throws TMExceptionHandle
	 * @throws IOException
	 */
	public String getAttributeValue(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {
		String value = findElement(driver, objectLocator).getAttribute("value");
		APPLICATION_LOGS.info("The value of the element is: " + value);
		return value;
	}

	/**
	 * Method to get attribute Value list based on object locators list
	 * 
	 * @param driver
	 * @param objectLocators
	 * @return elementValue
	 * @throws TMExceptionHandle
	 * @throws IOException
	 */
	public List<String> getAttributeValueList(WebDriver driver,
			List<String> objectLocators) throws TMExceptionHandle {
		List<String> value = new ArrayList<String>();
		for (String val : objectLocators) {
			val = findElement(driver, val).getAttribute("value");
			value.add(val);
			APPLICATION_LOGS.info("The value of the element is: " + value);
		}
		return value;
	}

	/**
	 * Method to click an element
	 * 
	 * @param driver
	 * @param objectLocator
	 * @throws TMExceptionHandle
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void click(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {
		try {
			explicitWaitElementClickable(driver, objectLocator, 5000);
			if (findElement(driver, objectLocator).isEnabled()) {
				findElement(driver, objectLocator).click();
				APPLICATION_LOGS.info("The web element is clicked");
			} else
				APPLICATION_LOGS
						.info("The web element is not clickable, hence blocked");
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to click using java script function
	 * 
	 * @param driver
	 * @param objectLocator
	 * @throws TMExceptionHandle
	 */
	public void clickByJavaScript(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {
		try {
			explicitWaitElementClickable(driver, objectLocator, 10);
			((JavascriptExecutor) driver).executeScript("arguments[0].click()",
					findElement(driver, objectLocator));
			APPLICATION_LOGS
					.info("The web element is clicked by javascript executor");
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}
	}

	/**
	 * Method to perform double click on an element
	 * 
	 * @param driver
	 * @param objectLocator
	 * @throws TMExceptionHandle
	 */
	public void doubleClick(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {
		try {
			waitForElementPresent(driver, objectLocator, 10);
			Actions action = new Actions(driver).doubleClick(findElement(
					driver, objectLocator));
			action.build().perform();

			APPLICATION_LOGS.info("Performed double click on " + objectLocator);
		} catch (TMExceptionHandle e) {
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to clear and send keys to a text box
	 * 
	 * @param driver
	 * @param objectLocator
	 * @throws TMExceptionHandle
	 */
	public static void sendKeys(WebDriver driver, String objectLocator,
			String value) throws TMExceptionHandle {
		try {
			waitForElementPresent(driver, objectLocator, 60);
			findElement(driver, objectLocator).clear();
			findElement(driver, objectLocator).sendKeys(value);
			APPLICATION_LOGS
					.info("The keys entered in web element is:" + value);
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}
	}

	/**
	 * Method to get hover message text
	 * 
	 * @param driver
	 * @param objectLocator
	 * @return tootltip title
	 * @throws TMExceptionHandle
	 * @throws IOException
	 */
	public String getHoverMessage(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {
		String tooltip = null;
		try {
			tooltip = findElement(driver, objectLocator).getAttribute("title");
			APPLICATION_LOGS.info("The tooltip massage is: " + tooltip);
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}
		return tooltip;
	}

	/**
	 * Method to select a drop down
	 * 
	 * @param driver
	 * @param objectLocator
	 * @param value
	 * @throws TMExceptionHandle
	 * @throws IOException
	 * 
	 */
	public void selectDropdown(WebDriver driver, String objectLocator,
			String value) throws TMExceptionHandle {
		try {
			waitForElementPresent(driver, objectLocator, 10);
			new Select(findElement(driver, objectLocator))
					.selectByVisibleText(value);
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to select a radio button
	 * 
	 * @param driver
	 * @param objectLocator
	 * @param value
	 * @throws IOException
	 * 
	 */
	public void selectRadioCheckboxButton(WebDriver driver,
			String objectLocator, boolean flag) throws TMExceptionHandle {
		try {
			List<WebElement> select = findElements(driver, objectLocator);
			for (WebElement radio : select) {
				if (radio.isSelected() != flag)
					radio.click();
				else
					APPLICATION_LOGS.info("The element is already selected");
			}
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to get popup text message
	 * 
	 * @param driver
	 * @return popup message
	 */
	public void acceptAlert(WebDriver driver) throws TMExceptionHandle {
		String message = null;
		try {
			Alert alert = driver.switchTo().alert();
			message = alert.getText();
			alert.accept();
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		APPLICATION_LOGS.info("message" + message);
	}

	/**
	 * Method to cancel popup message box
	 * 
	 * @param driver
	 */
	public void dismissAlert(WebDriver driver) throws TMExceptionHandle {
		try {
			Alert alert = driver.switchTo().alert();
			alert.dismiss();
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to perform drag-and-drop
	 * 
	 * @param driver
	 * @throws TMExceptionHandle
	 */
	public void performDragAndDrop(WebDriver driver, String fromObjectLocator,
			String toObjectLocator) throws TMExceptionHandle {

		try {
			Actions act = new Actions(driver);
			WebElement fromElement = findElement(driver, fromObjectLocator);
			WebElement toElement = findElement(driver, toObjectLocator);
			act.dragAndDrop(fromElement, toElement).build().perform();
			APPLICATION_LOGS.info("<< Drag drop action is performed >>");
			act.release();
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}
	}

	/**
	 * Method to perform drag-and-drop with web elements
	 * 
	 * @param driver
	 * @throws TMExceptionHandle
	 */
	public void performDragAndDrop(WebDriver driver, WebElement src,
			WebElement dest) throws TMExceptionHandle {
		Actions act = new Actions(driver);
		act.dragAndDrop(src, dest).build().perform();
		APPLICATION_LOGS.info("<< Drag drop action is performed >>");
		act.release();
	}

	/**
	 * Method to store current window handle
	 * 
	 * @param driver
	 */
	public String storingCurrentWindowHandle(WebDriver driver)
			throws TMExceptionHandle {
		// store the current window handle
		try {
			WinhandleBefore = driver.getWindowHandle();
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		return WinhandleBefore;
	}

	/**
	 * Method to get window handles
	 * 
	 * @param driver
	 */
	public Set<String> getWindowHandles(WebDriver driver)
			throws TMExceptionHandle {
		// store the current window handle
		Set<String> list = null;
		try {
			list = driver.getWindowHandles();
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		return list;
	}

	/**
	 * Method to perform switchToNewWindow
	 */
	public void switchToNewWindow(WebDriver driver, String beforeWindow)
			throws TMExceptionHandle {
		try {
			// switch to window
			Set<String> winhandles = driver.getWindowHandles();
			Iterator<String> i = winhandles.iterator();
			String tabbedWindowId = null;
			while (i.hasNext()) {
				String t = i.next();
				if (!t.equals(beforeWindow))
					tabbedWindowId = t;
			}
			driver.switchTo().window(tabbedWindowId);
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to close child window and switching back to parent window
	 * 
	 * @param driver
	 */
	public void switchBackToParentWindow(WebDriver driver)
			throws TMExceptionHandle {
		// driver.close();
		try {
			driver.switchTo().window(WinhandleBefore);
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to quit a window
	 * 
	 * @param driver
	 */
	public void quitWindow(WebDriver driver) throws TMExceptionHandle {
		try {
			driver.quit();
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to close a window
	 * 
	 * @param driver
	 */
	public void closeWindow(WebDriver driver) throws TMExceptionHandle {
		try {
			driver.close();
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to scroll Down The Page
	 * 
	 * @param driver
	 */
	public void scrollDownThePage(WebDriver driver) throws TMExceptionHandle {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("scroll(250, 0)");
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to scroll Up The Page
	 * 
	 * @param driver
	 */
	public void scrollUpThePage(WebDriver driver) throws TMExceptionHandle {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("scroll(250, 0)");
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	public void scrollDownDropDown(WebDriver driver, String objectLocator) {

		try {
			// Create instance of Javascript executor
			JavascriptExecutor je = (JavascriptExecutor) driver;
			WebElement element = findElement(driver, objectLocator);
			// now execute query which actually will scroll until that element is not appeared on page.
			je.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (TMExceptionHandle e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Method to navigate to a given url
	 * 
	 * @param driver
	 * @param URL
	 */
	public void navigateToURL(WebDriver driver, String URL)
			throws TMExceptionHandle {
		try {
			driver.navigate().to(URL);
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to get URL
	 * 
	 * @param driver
	 * @param URL
	 */
	public void getURL(WebDriver driver, String URL) throws TMExceptionHandle {
		try {
			driver.get(URL);
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to switch to a new frame
	 * 
	 * @param driver
	 * @param frame
	 * @throws TMExceptionHandle
	 */
	public void switchToFrame(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {
		try {
			waitForElementPresent(driver, objectLocator, 10);
			WebElement frame = findElement(driver, objectLocator);
			driver.switchTo().frame(frame);
		} catch (TMExceptionHandle e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}
	}

	/**
	 * Method to switch back to the old frame
	 * 
	 * @param driver
	 */
	public void switchBackToDefault(WebDriver driver) throws TMExceptionHandle {
		try {
			// driver.switchTo().defaultContent();
			driver.switchTo().parentFrame();
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
	}

	/**
	 * Method to wait for an element to be present. Polls every 500 milliseconds
	 * and check whether the element is present or not
	 * 
	 * @param by
	 *            - of the element to be checked (120=60seconds=1minute)
	 * @return true if element is found, else returns false.
	 */
	public static void waitForElementPresent(WebDriver driver,
			String objectLocator, int timeInSeconds) throws TMExceptionHandle {
		try {
			@SuppressWarnings("unused")
			WebElement myDynamicElement = (new WebDriverWait(driver,
					timeInSeconds))
					.until(ExpectedConditions
							.presenceOfElementLocated(getByObject(driver,
									objectLocator)));
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}
	}

	/**
	 * Return true is the element is present else returns false
	 * 
	 * @param - by of the element to be checked
	 * @return true if element is found, else returns false.
	 */
	public static boolean isElementPresent(WebDriver driver,
			String objectLocator, int timeInSeconds) throws TMExceptionHandle {
		try {
			WebElement myDynamicElement = (new WebDriverWait(driver,
					timeInSeconds))
					.until(ExpectedConditions
							.presenceOfElementLocated(getByObject(driver,
									objectLocator)));
			if (myDynamicElement != null)
				return true;
			else
				return false;
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		return false;
	}

	/**
	 * Wait dynamically until the progress bar to load completely with jquery
	 * and java script.
	 * 
	 */
	public boolean waitForJQueryProcessing(WebDriver driver,
			int timeOutInSeconds) throws TMExceptionHandle {
		boolean jQcondition = false;
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			if ((Boolean) executor
					.executeScript("return window.jQuery != undefined")) {
				while (!(Boolean) executor
						.executeScript("return jQuery.active == 0")) {
					Thread.sleep(timeOutInSeconds);
				}
			}
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);

		}
		return jQcondition;
	}

	/**
	 * To get the current time details
	 */
	public String getCurrentTimeInstance() throws TMExceptionHandle {
		String currentTimeInstance = null;
		try {
			Calendar calendar = Calendar.getInstance();
			currentTimeInstance = calendar.get(Calendar.YEAR) + "-"
					+ calendar.get(Calendar.MONTH) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "-"
					+ calendar.get(Calendar.HOUR_OF_DAY) + "-"
					+ calendar.get(Calendar.MINUTE) + "-"
					+ calendar.get(Calendar.SECOND) + "-"
					+ calendar.get(Calendar.MILLISECOND);
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		return currentTimeInstance;

	}

	/**
	 * Method to get By object
	 * 
	 * @param driver
	 * @param objectLocator
	 * @return WebElement
	 * @throws IOException
	 */
	public static By getByObject(WebDriver driver, String objectLocator)
			throws TMExceptionHandle {

		try {
			String[] splits = objectLocator.split("~");
			String objecttype = splits[0];
			APPLICATION_LOGS.info("obj type: " + objecttype);
			String objectvalue = splits[1];
			APPLICATION_LOGS.info("obj value: " + objectvalue);

			if (objecttype.equalsIgnoreCase("id"))
				return By.id(objectvalue);
			if (objecttype.equalsIgnoreCase("xpath"))
				return By.xpath(objectvalue);
			if (objecttype.equalsIgnoreCase("css"))
				return By.cssSelector(objectvalue);
			if (objecttype.equalsIgnoreCase("tagname"))
				return By.tagName(objectvalue);
			if (objecttype.equalsIgnoreCase("name"))
				return By.name(objectvalue);
			if (objecttype.equalsIgnoreCase("linktext"))
				return By.linkText(objectvalue);
			if (objecttype.equalsIgnoreCase("partiallinktext"))
				return By.partialLinkText(objectvalue);
			if (objecttype.equalsIgnoreCase("classname"))
				return By.className(objectvalue);
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		return null;
	}

	/**
	 * Get the data from Web Table cells
	 * 
	 * @param tableId
	 * @param rowXpath
	 * @param columnXpath
	 * @param inputData
	 */
	public String getWebTableValues(WebDriver driver, String tableId,
			String rowXpath, String columnXpath, String inputData)
			throws TMExceptionHandle {
		String cellValues = null;
		try {
			String[] splits = tableId.split("~");
			String objectvalue = splits[1];
			WebElement table_element = driver.findElement(By.id(objectvalue));
			splits = rowXpath.split("~");
			objectvalue = splits[1];
			List<WebElement> numberOfRows = table_element.findElements(By
					.xpath(objectvalue));
			APPLICATION_LOGS.info("NUMBER OF ROWS IN THIS TABLE = "
					+ numberOfRows.size());
			int row_num, col_num;
			row_num = 1;
			boolean b = false;
			for (WebElement rowElement : numberOfRows) {
				splits = rowXpath.split("~");
				objectvalue = splits[1];
				List<WebElement> numberOfCol = rowElement.findElements(By
						.xpath(objectvalue));
				APPLICATION_LOGS.info("NUMBER OF COLUMNS = "
						+ numberOfCol.size());
				col_num = 1;
				for (WebElement colElement : numberOfCol) {
					APPLICATION_LOGS.info("row # " + row_num + ", col # "
							+ col_num + ", text=" + colElement.getText());
					// if
					// (colElement.getText().trim().equalsIgnoreCase(inputData))
					// {
					if (colElement.getText().trim().equals(inputData)) {
						cellValues = colElement.getText().trim();
						b = true;
						break;
					}
					col_num++;
				}
				if (b) {
					break;
				}
				row_num++;
			}
		} catch (Exception e) {
			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());
			APPLICATION_LOGS.error(e);
		}
		System.out.println("Expected Data: " + inputData + "Actual Data: "
				+ cellValues);
		return (cellValues);
	}
}
