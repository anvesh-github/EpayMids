package com.main.utils;

import org.apache.log4j.Logger;

public class TMExceptionHandle extends Throwable implements TMExceptionCodes {
	public static Logger logger = Logger.getLogger(TMExceptionHandle.class
			.getName());
	private static final long serialVersionUID = 1L;
	private static TMExceptionHandle exceptionObj;

	private TMExceptionHandle() {

	}

	/**
	 * Create a static method to get instance.
	 */
	public static TMExceptionHandle getInstance() {
		if (exceptionObj == null) {
			exceptionObj = new TMExceptionHandle();
		}
		return exceptionObj;
	}

	/**
	 * @param message
	 * @param errorCode
	 * @return
	 */
	public String checkErrorType(String message, String errorCode) {
		if (message.startsWith(TMExceptionCodes.sePackage)) {
			return seleniumExceptionErrorHandler(errorCode);
		} else if (message.startsWith(TMExceptionCodes.langPackage)
				|| (message.startsWith(TMExceptionCodes.ioPackage))) {
			return javaExceptionErrorHandler(errorCode);
		} else if (message.startsWith(TMExceptionCodes.soPackage)
				|| (message.startsWith(TMExceptionCodes.soParserPackage) || (message
						.startsWith(TMExceptionCodes.soTransformerPackage)))) {
			return soapExceptionErrorHandler(errorCode);
		} else {
			return errorCode;
		}
	}

	/**
	 * @param e
	 *            Handles Java Related Exceptions
	 * @return Java Error Code
	 */
	public String javaExceptionErrorHandler(String e) {
		switch (e) {
		case A:
			return ErrCode.JAVA.toString().concat(": Class not found: " + A);
		case B:
			return ErrCode.JAVA.toString().concat(
					": Array index is out-of-bounds: " + B);
		case C:
			return ErrCode.JAVA.toString().concat(
					": Invalid use of a null reference"
							+ "Locator or Data entered is passed as null : "
							+ C);
		case D:
			return ErrCode.JAVA.toString().concat(
					": Illegal argument used to invoke a method: " + D);
		case E:
			return ErrCode.JAVA.toString().concat(
					": Invalid conversion of a string to a numeric format: "
							+ E);
		case F:
			return ErrCode.JAVA.toString().concat(
					": Attempt to violate security: " + F);
		case G:
			return ErrCode.JAVA.toString().concat(
					": Attempt to index outside the bounds of a string: " + G);
		case H:
			return ErrCode.JAVA
					.toString()
					.concat(": One thread has been interrupted by another thread: "
							+ H);
		case I:
			return ErrCode.JAVA
					.toString()
					.concat(": Could not find the file:The file should be "
							+ "in the folder from which you invoke the program: "
							+ I);
		case J:
			return ErrCode.JAVA.toString().concat(
					": Attempt to index outside the bounds of a string: " + J);
		case K:
			return ErrCode.JAVA.toString().concat(
					": Attempted to read one type of "
							+ "token, but the next token was a different type:"
							+ K);
		case L:
			return ErrCode.JAVA.toString().concat(
					": The server encountered an internal error that prevented it "
							+ "from fulfilling this request:" + L);
		default:
			return e;
		}
	}

	/**
	 * @param e
	 *            Handles Selenium WebDriver Related Exceptions
	 * @return Selenium Error Code
	 */
	private String seleniumExceptionErrorHandler(String e) {
		switch (e) {
		case a:
			return ErrCode.SELENIUM
					.toString()
					.concat(": Thrown when trying to select an unselectable "
							+ "element.For example, selecting a �script� element : "
							+ a);
		case b:
			return ErrCode.SELENIUM.toString().concat(
					": Element is present on the DOM, but it "
							+ "is not visible: " + b);
		case c:
			return ErrCode.SELENIUM.toString().concat(
					": when frame or window target to be switched "
							+ "doesn�t exist: " + c);
		case d:
			return ErrCode.SELENIUM.toString().concat(
					": An error has occurred on the server side: " + d);
		case f:
			return ErrCode.SELENIUM.toString().concat(
					": Alert is not yet on the screen when switching:" + f);
		case g:
			return ErrCode.SELENIUM.toString().concat(
					": Element could not be found: " + g);
		case h:
			return ErrCode.SELENIUM.toString().concat(
					": Frame target to be switched doesn�t exist: " + h);
		case i:
			return ErrCode.SELENIUM.toString().concat(
					": Window target to be switched doesn�t exist: " + i);
		case j:
			return ErrCode.SELENIUM.toString().concat(
					": Command does not complete in enough time: " + j);
		case k:
			return ErrCode.SELENIUM.toString().concat(
					": Base webdriver exception: " + k);
		case l:
			return ErrCode.SELENIUM.toString().concat(
					": When a reference to an element is now stale: " + l);
		case m:
			return ErrCode.SELENIUM.toString().concat(
					": An unexpected alert is present : " + m);
		case n:
			return ErrCode.SELENIUM.toString().concat(
					": The target provided to the ActionsChains move() "
							+ "method is invalid, i.e. out of document : " + n);
		case o:
			return ErrCode.SELENIUM.toString().concat(
					": When a reference to an element is now stale: " + o);
		case p:
			return ErrCode.SELENIUM.toString().concat(
					": The attribute of element could not be found: " + p);
		default:
			return e;
		}
	}

	private String soapExceptionErrorHandler(String e) {
		switch (e) {
		case II:
			return ErrCode.SOAP
					.toString()
					.concat(": Thrown when there is a difficulty setting a header, not being able to send a message, and not being able to get a connection with the provider : "
							+ II);
		case III:
			return ErrCode.SOAP
					.toString()
					.concat(": Thrown when there is a warning information from either the XML parser or the application: a parser writer or application writer can subclass it to provide additional functionality : "
							+ III);
		case IV:
			return ErrCode.SOAP.toString().concat(
					": Thrown when there is a serious configuration error : "
							+ IV);
		case V:
			return ErrCode.SOAP.toString().concat(
					": Thrown when there is a serious configuration error : "
							+ V);
		case VI:
			return ErrCode.SOAP
					.toString()
					.concat(": Thrown when there is an exceptional condition that occurred during the transformation process : "
							+ VI);
		default:
			return e;

		}

	}

}
