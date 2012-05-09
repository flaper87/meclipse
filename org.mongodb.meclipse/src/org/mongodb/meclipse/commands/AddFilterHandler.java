package org.mongodb.meclipse.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.wizards.FilterWizard;

public class AddFilterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// FilterWizard wizard = new FilterWizard();
		// Shell shell = PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getShell();
		//
		// WizardDialog dialog = new WizardDialog(shell, wizard);
		// dialog.create();
		// dialog.open();
		// return null;
		MeclipsePlugin.getDefault().openWizard(FilterWizard.ID);
		return null;
	}


}
