package org.mongodb.meclipse.wizards;

import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.views.FilterPlacement;
import org.mongodb.meclipse.views.objects.Collection;
import org.mongodb.meclipse.views.objects.Filter;
import org.mongodb.meclipse.views.objects.TreeParent;

public class FilterWizardPage extends WizardPage {
	private Set<Filter> existingFilters;
	private Text nameText;
	private Text queryText;
	private Boolean nameValid = false;
	private Boolean jsonValid = false;

	public FilterWizardPage(ISelection selection) {
		super("Filter details");
		setTitle("New Filter Wizard");
		setDescription("Enter filter details here");
		
		ITreeSelection treeSelection = (ITreeSelection)selection;
		Object obj = treeSelection.getFirstElement();
		if (!(obj instanceof Collection) && !(obj instanceof Filter))
			throw new IllegalStateException(obj.getClass().getSimpleName() + "? Should not arrive here without a reference to a Collection or Filter");
		
		TreeParent parent = (TreeParent)obj;
		existingFilters = MeclipsePlugin.getDefault().getFilters(new FilterPlacement(parent));
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
		nameText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				String text = ((Text)e.widget).getText();
				nameValid = true;
				if (existingFilters != null)
				{
					for (Filter filter : existingFilters)
						if (text.equals(filter.getName()))
						{
							nameValid = false;
							setErrorMessage("A filter already exists with that name.");
							setPageComplete(false);
							break;
					}
				}
				if (nameValid && jsonValid)
					setPageComplete(true);
			}
			});

		label = new Label(container, SWT.NULL);
		label.setText("Query (JSON)");
		queryText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		queryText.setLayoutData(gd);
		queryText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				jsonValid = true;
				String text = ((Text)e.widget).getText();
				try {
					new JSONObject(new JSONTokener(text));
					if (jsonValid && nameValid)
					{
						setErrorMessage(null);
						setPageComplete(true);
					}
				} catch (JSONException e1) {
					jsonValid = false;
					setErrorMessage("The JSON entered is not valid.");
					setPageComplete(false);
				}
			}
			});
		
		setControl(container);
	}

	public Filter getFilter() {
		return new Filter(nameText.getText(), queryText.getText());
	}
}
