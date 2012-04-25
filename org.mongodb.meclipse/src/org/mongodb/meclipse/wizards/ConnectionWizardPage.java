package org.mongodb.meclipse.wizards;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.mongodb.meclipse.preferences.MongoInstance;
import org.mongodb.meclipse.views.objects.Connection;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 * @author Joey Mink, ExoAnalytic Solutions
 */
public class ConnectionWizardPage extends WizardPage implements Listener{
	private Text connName;
	private Text host;
	private Text port;
	private Text username;
	private Text password;
	private Connection conn;
//	private ISelection selection;
	private Button saveCheckBox;
	//private Combo savedServersSelect;
	/** the servers that were saved at the time this wizard page was loaded **/
	private Map<String, MongoInstance> savedServers = new HashMap<String, MongoInstance>();

	public Map<String, MongoInstance> getSavedServers() {
		return savedServers;
	}

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param pageName
	 */
	public ConnectionWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle(getCaption("connectionWizard.title"));
		//setDescription("");
//		this.selection = selection;
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

		Label label;
		
		label = new Label(container, SWT.NULL);
		label.setText(getCaption("connectionWizard.label.name"));
		connName = new Text(container, SWT.BORDER | SWT.SINGLE);
		connName.setLayoutData(gd);
		connName.addListener(SWT.CHANGED, this);
		
		label = new Label(container, SWT.NULL);
		label.setText(getCaption("connectionWizard.label.host"));
		host = new Text(container, SWT.BORDER | SWT.SINGLE);
		host.setLayoutData(gd);
		host.addListener(SWT.CHANGED, this);
		
		label = new Label(container, SWT.NULL);
		label.setText(getCaption("connectionWizard.label.port"));
		port = new Text(container, SWT.BORDER | SWT.SINGLE);
		port.setLayoutData(gd);
		port.addListener(SWT.CHANGED, this);
		
		/*
		label = new Label(container, SWT.NULL);
		label.setText("&Username:");
		username= new Text(container, SWT.BORDER | SWT.SINGLE);
		username.setLayoutData(gd);
		*/
		
		/*
		label = new Label(container, SWT.NULL);
		label.setText("Passwo&rd:");
		password = new Text(container, SWT.BORDER | SWT.SINGLE);
		password.setLayoutData(gd);
		*/
		label = new Label(container, SWT.NULL);
		label.setText("Auth not supported yet...");
		
		initialize();
		setControl(container);
		setPageComplete(false);
	}

	/**
	 * Tests if the current workbench selection is a suitable
	 * container to use.
	 */
	
	private void initialize() {
//		if (selection!=null && selection.isEmpty()==false && selection instanceof IStructuredSelection) {
//			IStructuredSelection ssel = (IStructuredSelection)selection;
//			if (ssel.size()>1) return;
//			Object obj = ssel.getFirstElement();
//			if (obj instanceof IResource) {
//				IContainer container;
//				if (obj instanceof IContainer)
//					container = (IContainer)obj;
//				else
//					container = ((IResource)obj).getParent();
//			}
//		}
	}

//	/**
//	 * @param message should be null if page has been validated successfully.
//	 */
//	private void updateStatus(String message) {
//		setErrorMessage(message);
//		setPageComplete(message == null);
//	}
	
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
	
	public Boolean isSaveConnection() {
		return saveCheckBox.getSelection();
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.type)
		{
		case SWT.CHANGED:
			if (this.connName.getText() != null && !connName.getText().equals("") &&
					this.host.getText() != null && !host.getText().equals("") &&
					this.port.getText() != null)
			{
				try
				{
					Integer.valueOf(port.getText());
					setPageComplete(true);
				}
				catch (NumberFormatException ex)
				{
					setPageComplete(false);
				}
			}
		}
	}
}