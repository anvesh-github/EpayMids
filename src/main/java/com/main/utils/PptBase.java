package com.main.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.log4j.PropertyConfigurator;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class PptBase extends SeleniumBase {

	public PptBase() {

		FileInputStream fs, orfs;
		// PropertiesConfigurator is used to configure logger from properties
		// file
		try {
			PropertyConfigurator.configure(System.getProperty("user.dir")
					+ "\\src\\main\\resoruces\\log4j.properties");

			fs = new FileInputStream(System.getProperty("user.dir")
					+ "\\src\\test\\resources\\config\\config.properties");
			config.load(fs);
			orfs = new FileInputStream(System.getProperty("user.dir")
					+ "\\src\\test\\resources\\locators\\OR.properties");

			OR.load(orfs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	public void testDataRunModeVerificaton(String methodName, XlsReader xls,
			Hashtable<String, String> data) {

		// Skips the test case if runmode is set to N
		if (!TestUtil.isTestCaseExecutable(methodName, xls)) {
			APPLICATION_LOGS.info("Skipping the test '" + methodName
					+ "' as testcase Runmode is set to: NO");
			throw new SkipException("Skipping the test '" + methodName
					+ "' as testcase Runmode is set to: NO");
		}
		// Skips the testcase based on the runmode in Test Data sheet
		if (!data.get("RunMode").equals("Y")) {
			APPLICATION_LOGS.info("Skipping the execution of '" + methodName
					+ "' as Runmode of test data is set to: NO");
			throw new SkipException("Skipping the execution of '" + methodName
					+ "' as Runmode of test data is set to: NO");
		}

	}

	/*@Parameters({ "browser" })
	@BeforeSuite
	public void setUp(String browser) {

		// Launch Browser
		getBrowser(browser);

	}

	@AfterSuite
	public void tearDown() {
		driver.quit();

	}*/

}
