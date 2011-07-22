package org.mongodb.meclipse.wizards;

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
		page = new FilterWizardPage();
		addPage(page);
	}

	public boolean performFinish() {
		if (selection == null)
			// last-ditch effort to capture the current selection:
			selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection(); 
		// Add our new filter to the appropriate collection in the MongoDB View:
		addFilter(selection, page.getFilter());
		return true;
	}
	
	public void addFilter(ISelection selection, Filter filter)
	{
		if (selection == null)
			throw new IllegalStateException("Can't add filter to a null selection");

		if (!(selection instanceof ITreeSelection))
			throw new IllegalStateException(selection.getClass().getSimpleName() + "? Did not expect to be called with anything but an ITreeSelection object.");
		
		ITreeSelection treeSelection = (ITreeSelection)selection;
		Object obj = treeSelection.getFirstElement();
		if (!(obj instanceof Collection) && !(obj instanceof Filter))
			throw new IllegalStateException(obj.getClass().getSimpleName() + "? Should not arrive here without a reference to a Collection or Filter");
		
		TreeParent parent = (TreeParent)obj;
		MeclipsePlugin.getDefault().addFilter(new FilterPlacement(parent), filter);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
