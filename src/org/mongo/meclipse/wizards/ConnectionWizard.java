package org.mongo.meclipse.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import java.net.MalformedURLException;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.mongo.meclipse.MeclipsePlugin;
import org.mongo.meclipse.preferences.SavedServer;
import org.mongo.meclipse.views.objects.Connection;

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
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		// save server preferences here
		if (page.isSaveConnection())
		{
			FileWriter writer = null;
			try {
				Map<String, SavedServer> savedServers = page.getSavedServers();

				IPath libPath = MeclipsePlugin.getDefault().getStateLocation();
				libPath = libPath.append("servers.cfg");
				File file = libPath.toFile();
				if (!file.exists())
					file.createNewFile();
				writer = new FileWriter(file, false); // overwrite all servers
				for (SavedServer server : savedServers.values())
				{
					if (!server.getName().equals(page.getConnName())) // don't want duplicates
						writer.write(server.getName() + "," + server.getHost() + "," + server.getPort() + "\n");
				}
				
				// Write the newly-created connection
				writer.write(page.getConnName() + "," + page.getHost() + "," + page.getPort() + "\n");
				// TODO: use CsvWriter
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				if (writer != null)
				{
					try
					{
						writer.close();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}// end finally
		}// end if
		return true;
	}
	
	public Connection getNewConnection() {
		return page.getConnection();
	}
	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents =
			"This is the initial file contents for *.invokatron file that should be word-sorted in the Preview page of the multi-page editor";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "Invokatron", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}