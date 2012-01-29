package org.mongodb.meclipse.util;

import org.eclipse.jface.dialogs.MessageDialog;

public class UIUtils {

	public static void openErrorDialog(String message){
		MessageDialog.openError(null, "Error", message);		
	}
	
}
