package com.test.SCT;

import java.io.IOException;
import java.util.Hashtable;

import com.main.utils.TMExceptionHandle;

public class SecureTradingTest extends SecureTradingBase {

	public SecureTradingTest() throws IOException, TMExceptionHandle {
		super();
	}

	public void testSct(Hashtable<String, String> data)
			throws TMExceptionHandle, Exception {

		String methodName = getMethodName();
		System.out.println("Method Name : " + methodName);
		APPLICATION_LOGS.info(">>>Starting execution of:'" + methodName
				+ "'<<<");

		// Verifying TestCase RunMode & TestData Runmode, If set to NO,
		// TestCase/TestData will be skipped
		testDataRunModeVerificaton(methodName, sctXls, data);
	}

}
