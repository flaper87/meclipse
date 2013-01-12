package org.mongodb.meclipse.wizards;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.views.FilterPlacement;
import org.mongodb.meclipse.views.objects.Collection;
import org.mongodb.meclipse.views.objects.Filter;
import org.mongodb.meclipse.views.objects.TreeParent;

/**
 * @author walknwind
 */
public class FilterWizard extends Wizard implements INewWizard {
	public static final String ID = "org.mongodb.meclipse.wizards.newFilterWizard";
	private FilterWizardPage page;
	private ISelection selection;

	public FilterWizard() {
		super();
		setNeedsProgressMonitor(false);
	}

	public void addPages() {
		if (selection == null)
			selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getSelectionService().getSelection();
		page = new FilterWizardPage(selection);
		addPage(page);
	}

	public boolean performFinish() {
		// Add our new filter to the appropriate collection in the MongoDB View:
		addFilter(selection, page.getFilter());
		return true;
	}

	public void addFilter(ISelection selection, Filter filter) {
		if (selection == null)
			throw new IllegalStateException(
					getCaption("filterWizard.error.nullSelection"));

		if (!(selection instanceof ITreeSelection))
			throw new IllegalStateException(selection.getClass()
					.getSimpleName()
					+ getCaption("filterWizard.error.noITreeSelection"));

		ITreeSelection treeSelection = (ITreeSelection) selection;
		Object obj = treeSelection.getFirstElement();
		if (!(obj instanceof Collection) && !(obj instanceof Filter))
			throw new IllegalStateException(obj.getClass().getSimpleName()
					+ getCaption("filterWizard.error.noCollection"));

		TreeParent parent = (TreeParent) obj;
		filter.setParent(parent);
		MeclipsePlugin.getDefault().addFilter(new FilterPlacement(parent),
				filter);
	}

	/**
	 * NOTE: I don't believe this is ever actually called...
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
