package org.mongodb.meclipse.wizards;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.views.FilterPlacement;
import org.mongodb.meclipse.views.objects.Collection;
import org.mongodb.meclipse.views.objects.Filter;
import org.mongodb.meclipse.views.objects.TreeParent;

import com.mongodb.util.JSON;

public class FilterWizardPage extends WizardPage {
	private Set<Filter> existingFilters;

	private Text nameText;

	private Text queryText;
	private IObservableValue nameValue = new WritableValue("", String.class);
	private IObservableValue queryValue = new WritableValue("", String.class);

	private final class FilterNameValidator implements IValidator {
		public IStatus validate(Object value) {
			if (existingFilters != null) {
				for (Filter filter : existingFilters)
					if (filter.getName().equals(value)) {
						return ValidationStatus
								.error(getCaption("filterWizard.error.nameAlreadyUsed"));
					}
			}
			if (null == value || ((String) value).trim().isEmpty()) {
				return ValidationStatus
						.error(getCaption("filterWizard.error.empty.name"));
			}
			return ValidationStatus.ok();
		}
	}

	private final class JSONValidator implements IValidator {

		@Override
		public IStatus validate(Object value) {
			if (null == value || ((String) value).trim().isEmpty()) {
				return ValidationStatus
						.error(getCaption("filterWizard.error.empty.json"));
			}
			try {
				JSON.parse((String) value);
				return ValidationStatus.ok();
			} catch (Exception e) {
				return ValidationStatus
						.error(getCaption("filterWizard.error.noValidJSON"));
			}
		}

	}

	public FilterWizardPage(ISelection selection) {
		super("Filter details");
		setTitle(getCaption("filterWizard.title"));
		setDescription(getCaption("filterWizard.detail"));

		ITreeSelection treeSelection = (ITreeSelection) selection;
		Object obj = treeSelection.getFirstElement();
		if (!(obj instanceof Collection) && !(obj instanceof Filter))
			throw new IllegalStateException(obj.getClass().getSimpleName()
					+ getCaption("filterWizard.error.noCollection"));

		TreeParent parent = (TreeParent) obj;
		existingFilters = MeclipsePlugin.getDefault().getFilters(
				new FilterPlacement(parent));
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		GridData gd = new GridData(SWT.FILL);
		gd.widthHint = 250;

		Label label;

		label = new Label(container, SWT.NULL);
		label.setText(getCaption("filterWizard.label.name"));
		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setImage(new Image(container.getDisplay(), MeclipsePlugin.class
				.getClassLoader().getResourceAsStream(
						MeclipsePlugin.HELP_IMG_ID)));
		label.setToolTipText(getCaption("filterWizard.tooltip.name"));

		label = new Label(container, SWT.NULL);
		label.setText(getCaption("filterWizard.label.query"));
		queryText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP
				| SWT.FILL);
		queryText.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setToolTipText(getCaption("filterWizard.tooltip.query"));
		label.setImage(new Image(container.getDisplay(), MeclipsePlugin.class
				.getClassLoader().getResourceAsStream(
						MeclipsePlugin.HELP_IMG_ID)));

		// add WizardPage validators
		DataBindingContext dbc = new DataBindingContext();
		WizardPageSupport.create(this, dbc);
		dbc.bindValue(SWTObservables.observeText(nameText, SWT.Modify),
				nameValue, new UpdateValueStrategy()
						.setBeforeSetValidator(new FilterNameValidator()), null);
		dbc.bindValue(SWTObservables.observeText(queryText, SWT.Modify),
				queryValue, new UpdateValueStrategy()
						.setBeforeSetValidator(new JSONValidator()), null);

		setControl(container);
		// disable save until everything matches
		setPageComplete(false);
	}

	public Filter getFilter() {
		return new Filter(nameText.getText(), queryText.getText());
	}
}
