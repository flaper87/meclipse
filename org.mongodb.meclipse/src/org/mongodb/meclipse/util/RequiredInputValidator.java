package org.mongodb.meclipse.util;

import org.eclipse.jface.dialogs.IInputValidator;

public class RequiredInputValidator implements IInputValidator {

	private String errorMessage;

	public RequiredInputValidator(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String isValid(String value) {
		if (value.length() == 0) {
			return errorMessage;
		}
		return null;
	}

}
