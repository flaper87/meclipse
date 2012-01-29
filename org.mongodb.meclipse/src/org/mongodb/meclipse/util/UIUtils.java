package org.mongodb.meclipse.util;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 
 * @author Naoki Takezoe
 */
public class UIUtils {

	public static void openErrorDialog(String message){
		MessageDialog.openError(null, "Error", message);		
	}
	
//	public static MeclipseView getMeclipseView(){
//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		IWorkbenchPage page = window.getActivePage();
//		MeclipseView view = (MeclipseView) page.findView(MeclipseView.ID);
//		return view;
//	}
}
