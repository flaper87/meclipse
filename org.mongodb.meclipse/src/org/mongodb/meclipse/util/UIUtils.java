package org.mongodb.meclipse.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Naoki Takezoe
 */
public class UIUtils {

	public static void openErrorDialog(Shell shell, String message){
		MessageDialog.openError(shell, "Error", message);		
	}
	
//	public static MeclipseView getMeclipseView(){
//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		IWorkbenchPage page = window.getActivePage();
//		MeclipseView view = (MeclipseView) page.findView(MeclipseView.ID);
//		return view;
//	}
}
