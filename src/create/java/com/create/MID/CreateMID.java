package com.create.MID;

import java.io.IOException;
import java.util.Hashtable;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.main.utils.TMExceptionHandle;
import com.main.utils.TestUtil;

public class CreateMID extends CreateMIDbase {

	public CreateMID() throws IOException, TMExceptionHandle {
		super();
	}

	@DataProvider
	public Object[][] getDataToCreateMID() {
		return TestUtil.getData("createMID", midXls, "TestDataToCreateMID");
	}

	@Test(dataProvider = "getDataToCreateMID")
	public void createMID(Hashtable<String, String> data)
			throws TMExceptionHandle, Exception {

		String methodName = getMethodName();
		System.out.println("Method Name : " + methodName);
		APPLICATION_LOGS.info(">>>Starting execution of:'" + methodName
				+ "'<<<");
		// Verifying TestCase RunMode & TestData Runmode, If set to NO,
		// TestCase/TestData will be skipped
		testDataRunModeVerificaton(methodName, midXls, data);

		clickOnTabSuppliers();

		if (searchForSupplier(data.get("SupplierName"))) {

			clickOnTabMIDs();

			createMID(data.get("MIDname"), data.get("SupplierName"),
					data.get("WebShopName"), data.get("PaymentInstruement"),
					data.get("SaleAPI"), data.get("Params"));

		} else {

			createSupplier(data.get("SupplierName"));
			createMID(data.get("MIDname"), data.get("MIDname"),
					data.get("WebShopName"), data.get("PaymentInstruement"),
					data.get("SaleAPI"), data.get("Params"));

		}

		clickOnTabSuppliers();

	}

	@DataProvider
	public Object[][] getDataToCreateChannel_Solution() {
		return TestUtil.getData("createChannel_Solution", midXls,
				"TestDataToCreateMID");
	}

	@Test(dataProvider = "getDataToCreateChannel_Solution")
	public void createChannel_Solution(Hashtable<String, String> data)
			throws TMExceptionHandle, Exception {

		String methodName = getMethodName();
		System.out.println("Method Name : " + methodName);
		APPLICATION_LOGS.info(">>>Starting execution of:'" + methodName
				+ "'<<<");
		
		

	}

}
