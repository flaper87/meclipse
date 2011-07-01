package org.mongo.meclipse.wizards;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.events.*;
import org.eclipse.jface.viewers.*;
import org.mongo.meclipse.MeclipsePlugin;
import org.mongo.meclipse.preferences.SavedServer;
import org.mongo.meclipse.views.objects.Connection;


/**
 * @author Flavio [FlaPer87] Percoco Premoli
 * @author Joey Mink, ExoAnalytic Solutions
 */
public class ConnectionWizardPage extends WizardPage {
	private Text connName;
	private Text host;
	private Text port;
	private Text username;
	private Text password;
	private Connection conn;
	private ISelection selection;
	private Button saveCheckBox;
	private Combo savedServersSelect;
	/** the servers that were saved at the time this wizard page was loaded **/
	private Map<String, SavedServer> savedServers = new HashMap<String, SavedServer>();

	public Map<String, SavedServer> getSavedServers() {
		return savedServers;
	}

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

		savedServers.clear();
		for (SavedServer server : loadSavedServers())
			savedServers.put(server.getName(), server);
		
		Label label = new Label(container, SWT.NULL);
		label.setText("&Load saved server?");
		savedServersSelect = new Combo(container, SWT.READ_ONLY);
		savedServersSelect.setLayoutData(gd);
		for (SavedServer server : savedServers.values())
			savedServersSelect.add(server.getName());
		savedServersSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				populateFromSavedServer();
			}
		});
		
		label = new Label(container, SWT.NULL);
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
		
		saveCheckBox = new Button(container, SWT.CHECK);
		saveCheckBox.setText("Save connection");
		saveCheckBox.setLayoutData(gd);
		
		initialize();
		setControl(container);
		setPageComplete(false);
	}
	
	private void populateFromSavedServer()
	{
		String selection = savedServersSelect.getItems()[savedServersSelect.getSelectionIndex()];
		//if (selection == null || selection.length > 1)
		//	throw new IllegalStateException("Should only be able to select one item form saved servers select.");
		
		SavedServer server = savedServers.get(selection/*[0]*/);
		if (server == null)
			return;
		
		this.connName.setText(server.getName());
		this.host.setText(server.getHost());
		this.port.setText(String.valueOf(server.getPort()));
	}
	
	private SavedServer[] loadSavedServers() {
		FileInputStream inputStream = null;
		try {
			IPath libPath = MeclipsePlugin.getDefault().getStateLocation();
			libPath = libPath.append("servers.cfg");
			File file = libPath.toFile();
			if (!file.exists())
				return new SavedServer[0];
			
			inputStream = new FileInputStream(file);
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(dataInputStream));
			
			java.util.List<SavedServer> savedServersList = new ArrayList<SavedServer>();
			String line;
			while ((line = bReader.readLine()) != null)
			{
				// TODO: use CsvWriter
				String[] values = line.split(",");
				SavedServer server = new SavedServer();
				server.setName(values[0]);
				server.setHost(values[1]);
				try
				{
					server.setPort(Integer.valueOf(values[2]));
				}
				catch (NumberFormatException ex)
				{
					System.out.println(ex);
				}
				savedServersList.add(server);
			}
			return savedServersList.toArray(new SavedServer[savedServersList.size()]);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return new SavedServer[0];
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
			// TODO: add connection management - don't want more than one connection to the same svr
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
	
	public Boolean isSaveConnection() {
		return saveCheckBox.getSelection();
	}
}