package org.mongodb.meclipse.wizards;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mongodb.meclipse.Images;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.preferences.MongoInstance;
import org.mongodb.meclipse.views.objects.Connection;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 * @author Joey Mink, ExoAnalytic Solutions
 */
public class ConnectionWizardPage extends WizardPage /* implements Listener */{
	private Text connName;
	private Text host;
	private Text port;
	private Text username;
	private Text password;
	private Connection conn;
	private static Image help = Images.get(Images.Help);

	// private ISelection selection;
	private Button saveCheckBox;
	// private Combo savedServersSelect;
	/** the servers that were saved at the time this wizard page was loaded **/
	private Map<String, MongoInstance> savedServers = new HashMap<String, MongoInstance>();

	private IObservableValue hostValue = new WritableValue("", String.class);
	private IObservableValue portValue = new WritableValue("", String.class);
	private IObservableValue nameValue = new WritableValue("", String.class);
	private Set<String> mongoInstances = MeclipsePlugin.getDefault()
			.getMongoNames();

	private final class DuplicateValidator implements IValidator {

		@Override
		public IStatus validate(Object arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	/**
	 * Validator which verifies that the name is matching criteria
	 */
	private final class NameValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			if (null == value || ((String) value).trim().isEmpty()) {
				return ValidationStatus
						.error(getCaption("connectionWizard.error.empty.name"));
			} else if (mongoInstances.contains((String) value)) {
				return ValidationStatus
						.error(getCaption("connectionWizard.error.dupl.name"));
			}
			return ValidationStatus.ok();
		}
	}

	/**
	 * Validator which validates that a hostname matching the required criteria
	 * is inserted for a new connection
	 */
	private final class HostNameValidator implements IValidator {
		public IStatus validate(Object value) {
			if (null != value && ((String) value).trim().isEmpty()) {
				return ValidationStatus
						.error(getCaption("connectionWizard.error.empty.host"));
			}
			Integer val = 0;
			try {
				val = Integer.parseInt(port.getText());
			} catch (NumberFormatException e) {
			}

			return checkDuplicateConnection((String) value, val);
		}
	}

	/**
	 * Validator which validates that the inserted port matches criteria
	 */
	private final class PortValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			if (null != value && ((String) value).trim().isEmpty()) {
				return ValidationStatus
						.error(getCaption("connectionWizard.error.port"));
			}
			int val = 0;
			try {
				val = Integer.parseInt((String) value);
				if (val < 0 || val > 65535) {
					return ValidationStatus
							.error(getCaption("connectionWizard.error.port"));
				}
			} catch (NumberFormatException e) {
				return ValidationStatus
						.error(getCaption("connectionWizard.error.port"));
			}
			return checkDuplicateConnection(host.getText(), val);
		}

	}

	public IStatus checkDuplicateConnection(String hostname, int port) {
		Iterator<String> it = mongoInstances.iterator();
		while (it.hasNext()) {
			String cur = it.next();
			MongoInstance instance = MeclipsePlugin.getDefault()
					.getMongoInstance(cur);
			if (instance.getPort() == port
					&& instance.getHost().equals(hostname)) {
				return ValidationStatus.warning(String.format(
						getCaption("connectionWizard.warn.duplicate"), cur));
			}
		}
		return ValidationStatus.ok();
	}

	public Map<String, MongoInstance> getSavedServers() {
		return savedServers;
	}

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ConnectionWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle(getCaption("connectionWizard.title"));
		// setDescription("");
		// this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;

		Label label;

		label = new Label(container, SWT.NULL);
		label.setText(getCaption("connectionWizard.label.name"));
		connName = new Text(container, SWT.BORDER | SWT.SINGLE);
		connName.setLayoutData(gd);
		label = new Label(container, SWT.NULL);
		label.setImage(help);
		label.setToolTipText(getCaption("connectionWizard.tooltip.name"));

		label = new Label(container, SWT.NULL);
		label.setText(getCaption("connectionWizard.label.host"));
		host = new Text(container, SWT.BORDER | SWT.SINGLE);
		host.setLayoutData(gd);
		label = new Label(container, SWT.NULL);
		label.setImage(help);
		label.setToolTipText(getCaption("connectionWizard.tooltip.host"));

		label = new Label(container, SWT.NULL);
		label.setText(getCaption("connectionWizard.label.port"));
		port = new Text(container, SWT.BORDER | SWT.SINGLE);
		port.setLayoutData(gd);
		port.setText("27017");
		label = new Label(container, SWT.NULL);
		label.setImage(help);
		label.setToolTipText(getCaption("connectionWizard.tooltip.port"));

		/*
		 * label = new Label(container, SWT.NULL); label.setText("&Username:");
		 * username= new Text(container, SWT.BORDER | SWT.SINGLE);
		 * username.setLayoutData(gd);
		 */

		/*
		 * label = new Label(container, SWT.NULL); label.setText("Passwo&rd:");
		 * password = new Text(container, SWT.BORDER | SWT.SINGLE);
		 * password.setLayoutData(gd);
		 */
		label = new Label(container, SWT.NULL);
		label.setText("Auth not supported yet...");

		// add WizardPage validators
		DataBindingContext dbc = new DataBindingContext();
		WizardPageSupport.create(this, dbc);

		dbc.bindValue(SWTObservables.observeText(connName, SWT.Modify),
				nameValue, new UpdateValueStrategy()
						.setBeforeSetValidator(new NameValidator()), null);
		dbc.bindValue(SWTObservables.observeText(host, SWT.Modify), hostValue,
				new UpdateValueStrategy()
						.setBeforeSetValidator(new HostNameValidator()), null);
		dbc.bindValue(SWTObservables.observeText(port, SWT.Modify), portValue,
				new UpdateValueStrategy()
						.setBeforeSetValidator(new PortValidator()), null);

		initialize();
		setControl(container);
		// disable Save until everything matches
		setPageComplete(false);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		// if (selection!=null && selection.isEmpty()==false && selection
		// instanceof IStructuredSelection) {
		// IStructuredSelection ssel = (IStructuredSelection)selection;
		// if (ssel.size()>1) return;
		// Object obj = ssel.getFirstElement();
		// if (obj instanceof IResource) {
		// IContainer container;
		// if (obj instanceof IContainer)
		// container = (IContainer)obj;
		// else
		// container = ((IResource)obj).getParent();
		// }
		// }
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
			if (portT == null)
				return -2;
			return Integer.parseInt(portT);
		} catch (NumberFormatException e) {
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