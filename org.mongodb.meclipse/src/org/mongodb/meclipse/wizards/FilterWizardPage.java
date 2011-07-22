package org.mongodb.meclipse.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mongodb.meclipse.views.objects.Filter;

public class FilterWizardPage extends WizardPage {

	private Text nameText;
	private Text queryText;

	public FilterWizardPage() {
		super("Filter details");
		setTitle("New Filter Wizard");
		setDescription("Enter filter details here");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		GridData gd = new GridData(GridData.FILL);
		gd.widthHint = 250;

		Label label;
		
		label = new Label(container, SWT.NULL);
		label.setText("&Name:");
		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("Query (JSON)");
		queryText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		queryText.setLayoutData(gd);
		
		setControl(container);
	}

	public Filter getFilter() {
		return new Filter(nameText.getText(), queryText.getText());
	}
}
