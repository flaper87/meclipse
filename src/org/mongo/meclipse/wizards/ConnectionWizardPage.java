package org.mongo.meclipse.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.events.*;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.jface.viewers.*;
import org.mongo.meclipse.views.objects.Connection;

/**
 * The "New" wizard page allows setting the container for
 * the new file as well as the file name. The page
 * will only accept file name without the extension OR
 * with the extension that matches the expected one (invokatron).
 */

public class ConnectionWizardPage extends WizardPage {
	private Text connName;
	private Text host;
	private Text port;
	private Text username;
	private Text password;
	private Connection conn;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param pageName
	 */
	public ConnectionWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("New Connection Wizard");
		//setDescription("");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;
		
		Label label = new Label(container, SWT.NULL);
		label.setText("&Name:");
		connName = new Text(container, SWT.BORDER | SWT.SINGLE);
		connName.setLayoutData(gd);
		
		label = new Label(container, SWT.NULL);
		label.setText("&Host:");
		host = new Text(container, SWT.BORDER | SWT.SINGLE);
		host.setLayoutData(gd);
		
		label = new Label(container, SWT.NULL);
		label.setText("&Port:");
		port = new Text(container, SWT.BORDER | SWT.SINGLE);
		port.setLayoutData(gd);
		
		label = new Label(container, SWT.NULL);
		label.setText("&Username:");
		username= new Text(container, SWT.BORDER | SWT.SINGLE);
		username.setLayoutData(gd);
		
		label = new Label(container, SWT.NULL);
		label.setText("Passwo&rd:");
		password = new Text(container, SWT.BORDER | SWT.SINGLE);
		password.setLayoutData(gd);
		
		Button button = new Button(container, SWT.PUSH);
		button.setText("Validate Connection...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				validateConnection();
			}
		});
		
		initialize();
		setControl(container);
		setPageComplete(false);
	}
	
	/**
	 * Tests if the current workbench selection is a suitable
	 * container to use.
	 */
	
	private void initialize() {
		if (selection!=null && selection.isEmpty()==false && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection)selection;
			if (ssel.size()>1) return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer)obj;
				else
					container = ((IResource)obj).getParent();
			}
		}
	}
	
	/**
	 * Ensures that both text fields are set.
	 */

	private void validateConnection() {
		String name = getConnName();
		String host = getHost();
		
		if ( name.length() == 0 || 
				host.length() == 0 ||
				getPort() == -2 ) { 
			updateStatus("All fields required");
			return;
		} else if (getPort() == -1) {
			updateStatus("Port most be an integer");
			return;
		}
		
		try {
			conn = new Connection(name, host, getPort());
			if (conn.validate()) {
				updateStatus(null);
				return;
			}
		} catch (Exception exc) {
		}
		updateStatus("Unsuccessful Connection!!!");
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	public String getConnName() {
		return connName.getText();
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public String getHost() {
		return host.getText();
	}
	
	public int getPort() {
		try {
			String portT = port.getText();
			if ( portT == null)
				return -2;
		    return Integer.parseInt(portT);
		  } catch(NumberFormatException e) { 
			  return -1;
		  }
	}
	
	public String getUsername() {
		return username.getText();
	}
	
	public String getPassword() {
		return password.getText();
	}
	
}