package com.main.utils;

import com.opera.core.systems.OperaDesktopDriver;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jfree.chart.*;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Summary: Implementation of a listener to take screen shots when using web
 * driver for tests that fail. Screen shots are saved to a screen shots folder
 * on the users Desktop in the folder ProjectLocation+/TestOutput/Screenshots/, must
 * have admin rights so the folder is created and saved etc.
 * 
 * Dependencies: Requires an instance of the browsers web driver as this class
 * calls the WebDriverManager to get the browsers web driver instance.
 * 
 */

public class CaptureScreenShotOnFailureListener extends TestListenerAdapter {
	// static Logger APPLICATION_LOGS = Logger.getLogger("devpinoyLogger");
	TMExceptionHandle me = TMExceptionHandle.getInstance();
	List<String> notRunCount = new ArrayList<String>();
	DefaultPieDataset dataset = new DefaultPieDataset();
	XlsReader xls;
	String status = "";
	String buildName = "";
	String userIdentity = "";
	String serverUrl = "";
	String appName = "";
	String errCode = "";

	public CaptureScreenShotOnFailureListener() {
		try {
			xls = new XlsReader(System.getProperty("user.dir") + File.separator
					+ "TestOutput" + File.separator + "Results"
					+ File.separator + "Results.xls");

		} catch (Exception e) {
			System.out.println("MyTestListener()" + e.toString());
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		super.onTestFailure(result);
		SeleniumBase.APPLICATION_LOGS = Logger.getLogger(result.getName()
				+ "'FAILED");
		long time = (result.getEndMillis() - result.getStartMillis());
		ITestNGMethod method = result.getMethod();
		String[] className = result.getMethod().toString().split("\\.");
		Object[] obj = result.getParameters();
		try {
			if (!xls.isSheetExist(className[0])) {
				xls.addSheet(className[0]);
				xls.addColumn(className[0], "MethodName");
				xls.addColumn(className[0], "ClassName");
				xls.addColumn(className[0], "MethodResult");
				xls.addColumn(className[0], "MethodTimeout");
				xls.addColumn(className[0], "MethodParameterCount");
				xls.addColumn(className[0], "MethodExecutionTime");
				xls.addColumn(className[0], "Parameters");
				xls.addColumn(className[0], "ExceptionThrown");
			
			}
			int row = xls.getRowCount(className[0]) + 1;
			xls.setCellData(className[0], "MethodName", row,
					method.getMethodName());
			xls.setCellData(className[0], "ClassName", row, className[0]);
			// xls.setCellData(className[0], "MethodResult", row, "FAILED");
			xls.setCellData(className[0], "MethodTimeout", row,
					String.valueOf((method.getTimeOut())));
			xls.setCellData(className[0], "MethodParameterCount", row,
					String.valueOf(result.getParameters().length));
			xls.setCellData(className[0], "MethodExecutionTime", row,
					String.valueOf(time));
			if (result.getParameters().length > 0) {
				xls.setCellData(className[0], "Parameters", row,
						String.valueOf(obj[0]));
			}
			errCode = me
					.checkErrorType(result.getThrowable().toString(), result
							.getThrowable().getClass().getSimpleName()
							.toString());
			xls.setCellData(className[0], "ExceptionThrown", row, errCode);
			@SuppressWarnings("unused")
			String methodRes;
			if ((result.getThrowable().toString()
					.contains("not found after waiting for") || (result
					.getThrowable().toString()
					.contains("ThreadTimeoutException:")
					|| (result.getThrowable().toString()
							.contains("RuntimeException")) || (result
					.getThrowable().toString()
					.contains("NoSuchElementException:"))))) {
				methodRes = "NOTRUN";
				xls.setCellData(className[0], "MethodResult", row, "NOTRUN");
				notRunCount.add("NOTRUN");
			} else {
				methodRes = "FAILED";
				xls.setCellData(className[0], "MethodResult", row, "FAILED");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// call the superclass
		super.onTestFailure(result);

		// Get a driver instance from the web driver manager object
		WebDriver driver = SeleniumBase.getDriverInstance();

		/*
		 * We can only take screen shots for those browsers that support screen
		 * shot capture, html unit does not support screenshots as part of its
		 * capabilities.
		 */
		if ((driver instanceof InternetExplorerDriver)
				|| (driver instanceof FirefoxDriver)
				|| (driver instanceof ChromeDriver)
				|| (driver instanceof OperaDesktopDriver)) {
			// Create a calendar object so we can create a date and time for the
			// screenshot
			Calendar calendar = Calendar.getInstance();

			// Get the users home path and append the screen shots folder
			// destination
			// String userHome = System.getProperty("user.home");
			String screenShotsFolder = System.getProperty("user.dir")
					+ File.separator + "TestOutput" + File.separator
					+ "Screenshots" + File.separator;

			// The file includes the the test method and the test class
			String testMethodAndTestClass = result.getMethod().getMethodName()
					+ "(" + result.getTestClass().getName() + ")";

			SeleniumBase.APPLICATION_LOGS.info(" *** This "
					+ "is where the capture file is created for the Test \n"
					+ testMethodAndTestClass);

			// Create the filename for the screen shots
			String filename = screenShotsFolder + SeleniumBase.getBroswer()
					+ "-" + testMethodAndTestClass + "-"
					+ calendar.get(Calendar.YEAR) + "-"
					+ calendar.get(Calendar.MONTH) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "-"
					+ calendar.get(Calendar.HOUR_OF_DAY) + "-"
					+ calendar.get(Calendar.MINUTE) + "-"
					+ calendar.get(Calendar.SECOND) + "-"
					+ calendar.get(Calendar.MILLISECOND) + ".jpg";

			// Take the screen shot and then copy the file to the screen shot
			// folder
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);

			try {
				FileUtils.copyFile(scrFile, new File(filename));
			} catch (IOException e) {
				SeleniumBase.APPLICATION_LOGS.error(e.toString(), e);
				e.printStackTrace();
			}

		} // end of if
	} // end of onTestFailure

	@Override
	public void onTestSuccess(ITestResult result) {
		SeleniumBase.APPLICATION_LOGS = Logger.getLogger(result.getName()
				+ "'PASSED");
		super.onTestSuccess(result);
		long time = (result.getEndMillis() - result.getStartMillis());
		ITestNGMethod method = result.getMethod();
		String[] className = result.getMethod().toString().split("\\.");
		Object[] obj = result.getParameters();
		try {
			if (!xls.isSheetExist(className[0])) {
				xls.addSheet(className[0]);
				xls.addColumn(className[0], "MethodName");
				xls.addColumn(className[0], "ClassName");
				xls.addColumn(className[0], "MethodResult");
				xls.addColumn(className[0], "MethodTimeout");
				xls.addColumn(className[0], "MethodParameterCount");
				xls.addColumn(className[0], "MethodExecutionTime");
				xls.addColumn(className[0], "Parameters");
				xls.addColumn(className[0], "ExceptionThrown");
				
			}
			int row = xls.getRowCount(className[0]) + 1;
			xls.setCellData(className[0], "MethodName", row,
					method.getMethodName());
			xls.setCellData(className[0], "ClassName", row, className[0]);
			xls.setCellData(className[0], "MethodResult", row, "PASSED");
			xls.setCellData(className[0], "MethodTimeout", row,
					String.valueOf((method.getTimeOut())));
			xls.setCellData(className[0], "MethodParameterCount", row,
					String.valueOf(result.getParameters().length));
			xls.setCellData(className[0], "MethodExecutionTime", row,
					String.valueOf(time));
			if (result.getParameters().length > 0) {
				xls.setCellData(className[0], "Parameters", row,
						String.valueOf(obj[0]));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		SeleniumBase.APPLICATION_LOGS = Logger.getLogger(result.getName()
				+ "'SKIPPED");
		super.onTestSkipped(result);
		long time = (result.getEndMillis() - result.getStartMillis());
		ITestNGMethod method = result.getMethod();
		String[] className = result.getMethod().toString().split("\\.");
		Object[] obj = result.getParameters();
		try {
			if (!xls.isSheetExist(className[0])) {
				xls.addSheet(className[0]);
				xls.addColumn(className[0], "MethodName");
				xls.addColumn(className[0], "ClassName");
				xls.addColumn(className[0], "MethodResult");
				xls.addColumn(className[0], "MethodTimeout");
				xls.addColumn(className[0], "MethodParameterCount");
				xls.addColumn(className[0], "MethodExecutionTime");
				xls.addColumn(className[0], "Parameters");
				xls.addColumn(className[0], "ExceptionThrown");
				
			}
			int row = xls.getRowCount(className[0]) + 1;
			xls.setCellData(className[0], "MethodName", row,
					method.getMethodName());
			xls.setCellData(className[0], "ClassName", row, className[0]);
			xls.setCellData(className[0], "MethodResult", row, "SKIPPED");
			xls.setCellData(className[0], "MethodTimeout", row,
					String.valueOf((method.getTimeOut())));
			xls.setCellData(className[0], "MethodParameterCount", row,
					String.valueOf(result.getParameters().length));
			xls.setCellData(className[0], "MethodExecutionTime", row,
					String.valueOf(time));
			if (result.getParameters().length > 0) {
				xls.setCellData(className[0], "Parameters", row,
						String.valueOf(obj[0]));
			}
			xls.setCellData(className[0], "ExceptionThrown", row, result
					.getThrowable().toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFinish(ITestContext testContext) {
		System.out.println("Total Tests Executed: "
				+ testContext.getAllTestMethods().length);
		String className = testContext.getCurrentXmlTest().getClasses().get(0)
				.getName().replaceAll(".*?\\.", "").trim();
		System.out.println("Summary of XML Tests: " + className);
		xls.setCellData(className, "TotalPassed", 2, testContext
				.getPassedTests().size() + "");
		xls.setCellData(className, "TotalFailed", 2, testContext
				.getFailedTests().size() - notRunCount.size() + "");
		xls.setCellData(className, "TotalSkipped", 2, testContext
				.getSkippedTests().size() + "");
		xls.setCellData(className, "TotalNotRun", 2, notRunCount.size() + "");

		// To Generate the PieChart
		dataset.setValue("PASSED", testContext.getPassedTests().size());
		dataset.setValue("FAILED", testContext.getFailedTests().size()
				- notRunCount.size());
		dataset.setValue("SKIPPED", testContext.getSkippedTests().size());
		dataset.setValue("NOTRUN", notRunCount.size());

		JFreeChart piechart = ChartFactory.createPieChart(
				"" + className.replace("Test", " ")
						+ "Test Cases Execution Summary", dataset, true, true,
				false);
		PiePlot plot = (PiePlot) piechart.getPlot();
		/*
		 * We can now use setSectionPaint method to change the color of our
		 * chart
		 */
		PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(
				"{0} = {2}", new DecimalFormat("0"), new DecimalFormat("0.00%"));
		plot.setLabelGenerator(generator);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setSectionPaint("PASSED", Color.GREEN);
		plot.setSectionPaint("FAILED", Color.RED);
		plot.setSectionPaint("SKIPPED", Color.BLUE);
		plot.setSectionPaint("NOTRUN", Color.YELLOW);
		try {
			ChartUtilities.saveChartAsJPEG(
					new File(System.getProperty("user.dir") + File.separator
							+ "TestOutput" + File.separator + "pieCharts"
							+ File.separator + className + ".jpg"), 1,
					piechart, 640, 480);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

} // enf of class