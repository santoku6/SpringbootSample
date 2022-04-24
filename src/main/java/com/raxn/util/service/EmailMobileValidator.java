package com.raxn.util.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailMobileValidator {

	private static EmailMobileValidator emailMobileValidator = null;

	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

	static {
		emailMobileValidator = new EmailMobileValidator();
	}

	private EmailMobileValidator() {

	}

	public static EmailMobileValidator getInstance() {
		return emailMobileValidator;
	}

	public boolean emailValidator(final String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = null;
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean mobileValidator(String mobile) {
		Pattern ptrn = Pattern.compile("[6-9][0-9]{9}");
		// the matcher() method creates a matcher that will match the given input
		// against this pattern
		Matcher matcher = ptrn.matcher(mobile);
		return matcher.matches();
	}

}
