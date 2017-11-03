package com.main.utils;

public interface TMExceptionCodes {
	public enum ErrCode {
		JAVA, SELENIUM, SOAP
	}

	public static final String langPackage = "java.lang";
	public static final String ioPackage = "java.io";
	public static final String sePackage = "org.openqa.selenium";
	public static final String soPackage = "javax.xml.soap";
	public static final String soParserPackage = "javax.xml.parsers";
	public static final String soTransformerPackage = "javax.xml.transform";

	/* Selenium Exception Codes */

	public static final String a = "ElementNotSelectableException";
	public static final String b = "ElementNotVisibleException";
	public static final String c = "InvalidSwitchToTargetException";
	public static final String d = "ErrorInResponseException";
	public static final String f = "NoAlertPresentException";
	public static final String g = "NoSuchElementException";
	public static final String h = "NoSuchFrameException";
	public static final String i = "NoSuchWindowException";
	public static final String j = "TimeoutException";
	public static final String k = "WebDriverException";
	public static final String l = "StaleElementReferenceException";
	public static final String m = "UnexpectedAlertPresentException";
	public static final String n = "MoveTargetOutOfBoundsException";
	public static final String o = "InvalidElementStateException";
	public static final String p = "NoSuchAttributeException";

	/* Java Exception Codes */

	public static final String A = "ClassNotFoundException";
	public static final String B = "ArrayIndexOutOfBoundsException";
	public static final String C = "NullPointerException";
	public static final String D = "IllegalArgumentException";
	public static final String E = "NumberFormatException";
	public static final String F = "SecurityException";
	public static final String G = "StringIndexOutOfBounds";
	public static final String H = "InterruptedException";
	public static final String I = "FileNotFoundException";
	public static final String J = "NoSuchElementException";
	public static final String K = "InputMismatchException";
	public static final String L = "RuntimeException";
	public static final String M = "UnsupportedOperationException";

	/* Soap Exception Codes */
	public static final String II = "SOAPException";
	public static final String III = "SAXException";
	public static final String IV = "ParserConfigurationException";
	public static final String V = "TransformerConfigurationException";
	public static final String VI = "TransformerException";

}
