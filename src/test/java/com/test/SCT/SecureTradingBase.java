package com.test.SCT;

import java.io.FileInputStream;
import java.util.Properties;

import com.main.utils.PptBase;
import com.main.utils.TMExceptionHandle;
import com.main.utils.XlsReader;

public class SecureTradingBase extends PptBase {

	protected TMExceptionHandle tme = TMExceptionHandle.getInstance();
	Properties sct_or = new Properties();
	String projectLocation = System.getProperty("user.dir");

	public SecureTradingBase() {

		FileInputStream file_Input = null;
		try {

			file_Input = new FileInputStream(projectLocation
					+ "\\src\\test\\resources\\locators\\sctOR.properties");
			// Load property file
			sct_or.load(file_Input);
			

		} catch (Exception e) {

			tme.checkErrorType(e.getClass().getName(), e.getClass()
					.getSimpleName());

		}

	}
	// Load xls file
		public XlsReader sctXls = new XlsReader(
				System.getProperty("user.dir")
						+ "\\src\\test\\resources\\testData\\SCT.xls");

}
