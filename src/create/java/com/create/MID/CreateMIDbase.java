package com.create.MID;

import java.io.FileInputStream;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.main.utils.PptBase;
import com.main.utils.TMExceptionHandle;
import com.main.utils.XlsReader;

public class CreateMIDbase extends PptBase {
	protected TMExceptionHandle tme = TMExceptionHandle.getInstance();
	Properties onlineReporting_or = new Properties();
	Properties thousandft_or = new Properties();
	String projectLocation = System.getProperty("user.dir");

	public CreateMIDbase() {

		FileInputStream fi_or, fi_tkft = null;
		try {
			// Load property file
			fi_or = new FileInputStream(
					projectLocation
							+ "\\src\\test\\resources\\locators\\onlineReporting.properties");
			onlineReporting_or.load(fi_or);

			fi_tkft = new FileInputStream(projectLocation
					+ "\\src\\test\\resources\\locators\\10kft.properties");
			thousandft_or.load(fi_tkft);

		} catch (Exception e) {

			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());

		}
	}

	// Load xls file

	public XlsReader midXls = new XlsReader(System.getProperty("user.dir")
			+ "\\src\\test\\resources\\testData\\MIDcreate.xls");

	@Parameters({ "browser" })
	@BeforeClass
	public void setUp(String browser) {

		// Launch Browser
		getBrowser(browser);

		loginOnlineReporting();

	}

	@AfterClass
	public void tearDown() {
		
		logoutOnlineReporting();
		driver.quit();

	}

	public void loginOnlineReporting() {

		// Load URL for Dev online reporting
		loadURL(config.getProperty("devOnlineReporting_URL"));

		// Login with Valid Credentials
		try {

			sendKeys(
					driver,
					onlineReporting_or.getProperty("loginPageUsernameField_ID"),
					config.getProperty("devORusername"));

			sendKeys(
					driver,
					onlineReporting_or.getProperty("loginPagePasswordField_ID"),
					config.getProperty("devORpassword"));

			click(driver,
					onlineReporting_or
							.getProperty("loginPageSubmitButton_XPATH"));
		} catch (TMExceptionHandle e) {

			e.printStackTrace();
		}

		APPLICATION_LOGS.info("Login to online reporting Successfull");

	}

	// Logout of OnlineReportingss
	public void logoutOnlineReporting() {

		try {

			// Click on User
			click(driver, onlineReporting_or.getProperty("user_CLASS"));

			// Click on button "Sign Out"
			click(driver,
					onlineReporting_or.getProperty("user_signOutButton_XPATH"));
		} catch (TMExceptionHandle e) {

			e.printStackTrace();
		}

	}

	public void clickOnTabSuppliers() {
		// click on tab Suppliers
		try {
			click(driver,
					onlineReporting_or
							.getProperty("homePageSuppliersTab_XPATH"));
		} catch (TMExceptionHandle e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clickOnTabMIDs() {
		// click on tab Suppliers
		try {
			click(driver,
					onlineReporting_or.getProperty("homePageMIDsTab_XPATH"));
		} catch (TMExceptionHandle e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean searchForSupplier(String supplierName) {

		boolean supplierPresent = false;

		try {
			sendKeys(driver,
					onlineReporting_or
							.getProperty("suppliersPage_searchField_XPATH"),
					supplierName);

			if (isElementPresent(driver,
					onlineReporting_or
							.getProperty("supplierspage_supplierInList_XPATH"),
					15))
				supplierPresent = true;

		} catch (TMExceptionHandle e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return supplierPresent;
	}

	public void createSupplier(String supplierName) {
		try {

			click(driver,
					onlineReporting_or
							.getProperty("suppliersPage_addSupplier_XPATH"));

			sendKeys(
					driver,
					onlineReporting_or
							.getProperty("suppliersPage_addSupplierField_XPATH"),
					supplierName);

			click(driver,
					onlineReporting_or
							.getProperty("suppliersPage_addSupplierAddButton_XPATH"));
		} catch (TMExceptionHandle e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createMID(String midName, String supplierName,
			String webshopName, String typeOfProcessing, String saleAPI,
			String params) {

		try {
			// click add button on MID creation Page
			click(driver,
					onlineReporting_or
							.getProperty("midsPage_addMID_addButton_XPATH"));

			// Enter value for MID Name
			sendKeys(driver,
					onlineReporting_or.getProperty("addMIDPage_NameField_ID"),
					midName);

			// Select supplier name from the suggestions list
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_SupplierField_XPATH"));
			sendKeys(
					driver,
					onlineReporting_or
							.getProperty("addMIDPage_SupplierSearchField_XPATH"),
					supplierName);

			click(driver, "xpath~//li[text()='" + supplierName + "']");

			// Select Company name
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_CompanyField_XPATH"));

			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_selectCompany_XPATH"));

			// Select Tag
			click(driver,
					onlineReporting_or.getProperty("addMIDPage_TagField_XPATH"));

			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_selectTag_XPATH"));

			// Select Industry
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_IndustryField_XPATH"));
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_SelectIndustry_XPATH"));

			// Select WebShop
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_WebshopFiled_ID"));
			sendKeys(
					driver,
					onlineReporting_or
							.getProperty("addMIDPage_WebShopSearchField_XPATH"),
					webshopName);
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_SelectWebshop_XPATH")
							+ (webshopName + "']"));

			// Enter Billing Descriptor
			sendKeys(
					driver,
					onlineReporting_or
							.getProperty("addMIDPage_BillingDescriptorField_ID"),
					onlineReporting_or
							.getProperty("addMIDPage_BillingDescriptorValue"));

			// click on tab "Secondary Info"
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_SecondaryInfoTab_XPATH"));

			// click check-box "Secondary Email"
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_SecondaryInfoTab_SecondaryEmail_NAME"));

			// click check-box "Secondary Phone"
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_SecondaryInfoTab_SecondaryPhone_NAME"));

			// click on tab "Processing Settings"
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_ProcessingSettingsTab_XPATH"));

			// Select Payment Instruments
			String[] splits = typeOfProcessing.split(",");

			int count = 0;
			for (String paymentInstrument : splits) {

				if ((count) <= 0) {

					// click on Search field of payment instrument
					click(driver,
							onlineReporting_or
									.getProperty("addMIDPage_PaymentInstrument_XPATH"));
					click(driver,
							onlineReporting_or
									.getProperty("addMIDPage_PaymentInstrumentselection_XPATH")
									+ (paymentInstrument + "']"));
					count++;
				} else {

					// Click for suggestions of Payment Instrument

					click(driver,
							onlineReporting_or
									.getProperty("addMIDPage_AfterSelectingFirstPaymentInstrument_XPATH"));

					scrollDownDropDown(
							driver,
							onlineReporting_or
									.getProperty("addMIDPage_PaymentInstrumentselection_XPATH")
									+ (paymentInstrument + "']"));
					System.out.println(paymentInstrument);
					click(driver,
							onlineReporting_or
									.getProperty("addMIDPage_PaymentInstrumentselection_XPATH")
									+ (paymentInstrument + "']"));
				}
			}

			// click on check box "Accept Hostile"
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_AcceptHostile_CheckBox_ID"));

			scrollUpThePage(driver);

			// click on tab "API Settings"
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_APIsettingsTab_XPATH"));

			// enter Sale API
			sendKeys(driver,
					onlineReporting_or.getProperty("addMIDPage_SaleAPI_ID"),
					saleAPI);

			// Enter "Param Name" & "Param Value"
			String[] splits1 = params.split(",");

			for (String paramset : splits1) {

				String[] paramNameValue = paramset.split("=");

				String paramName = paramNameValue[0];
				String paramValue = paramNameValue[1];

				sendKeys(driver,
						onlineReporting_or
								.getProperty("addMIDPage_ParamName_ID"),
						paramName);
				sendKeys(driver,
						onlineReporting_or
								.getProperty("addMIDPage_ParamValue_ID"),
						paramValue);

				// click on button "Add"
				click(driver,
						onlineReporting_or
								.getProperty("addMIDPage_ADDbutton_ID"));

			}

			scrollUpThePage(driver);

			// Click on button "Add" to save the MID
			click(driver,
					onlineReporting_or
							.getProperty("addMIDPage_ADDbutton_SaveMID_XPATH"));

		} catch (TMExceptionHandle e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
