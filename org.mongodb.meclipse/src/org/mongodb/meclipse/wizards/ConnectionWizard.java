package org.mongodb.meclipse.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.preferences.MongoInstance;
import org.mongodb.meclipse.views.objects.Connection;

/**
 * @author Flavio [FlaPer87] Percoco Premoli,
 * @author Joey Mink, ExoAnalytic Solutions
 */
public class ConnectionWizard extends Wizard implements INewWizard {
	private ConnectionWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for InvokatronWizard.
	 */
	public ConnectionWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new ConnectionWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		// 1st, add the connection to our overall state:
		MongoInstance mongoInstance = new MongoInstance(page.getConnName());
		mongoInstance.setHost(page.getHost());
		mongoInstance.setPort(page.getPort());
		MeclipsePlugin.getDefault().addMongo(page.getConnName(), mongoInstance);

		return true;
	}

	public Connection getNewConnection() {
		return page.getConnection();
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}