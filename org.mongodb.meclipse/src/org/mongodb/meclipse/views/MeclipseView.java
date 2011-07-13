package org.mongodb.meclipse.views;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import org.mongodb.meclipse.*;
import org.mongodb.meclipse.views.objects.*;
import org.mongodb.meclipse.wizards.ConnectionWizard;


/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class MeclipseView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.mongodb.meclipse.views.MeclipseView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action connection;
	private Action doubleClickAction;
	private ViewContentProvider content = new ViewContentProvider();

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public MeclipseView() {}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(content);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Hook viewer up to the Eclipse selection provider:
		getSite().setSelectionProvider(viewer);
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
				"org.mongodb.meclipse.views");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		loadInitialContent();
	}

	/**
	 * Loads the initial contents of the view.
	 */
	private void loadInitialContent() {
		for (String mongoName : MeclipsePlugin.getDefault().getMongoNames())
		{
			Connection conn = new Connection(mongoName);
			conn.setViewer(this);
			content.getRoot().addChild(conn);
		}
		viewer.refresh();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        
		        if ( selection.isEmpty() )
		        {
		        	MeclipseView.this.fillContextMenu(manager);
		        	return;
		        }
		        TreeObject obj = (TreeObject)selection.getFirstElement();
		        
		        manager.add( new Separator() );
		        obj.fillContextMenu(manager);
		        
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(connection);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(connection);
		manager.add(new Separator());
//		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
//		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(connection);
		// manager.add(action2);
		// manager.add(new Separator());
		// drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		final MeclipseView mView = this;
		connection = new Action() {

			public void run() {

				ConnectionWizard wizard = new ConnectionWizard();
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();

				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.create();
				dialog.open();
				refreshViewerIfNecessary();
			}
		};

		connection.setText("New Connection");
		connection.setToolTipText("New Connection");
		connection.setImageDescriptor(Images.getDescriptor(Images.PageCommit));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				TreeObject obj = (TreeObject) ((IStructuredSelection) selection)
						.getFirstElement();
				obj.doubleClickAction();
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
				viewer.refresh();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"Meclipse View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void refreshViewerIfNecessary()
	{
		Set<String> mongoNames = MeclipsePlugin.getDefault().getMongoNames();
		Set<String> viewConnNames = getConnNames();
		
		for (String mongoName : mongoNames)
		{
			Boolean isDeleted = MeclipsePlugin.getDefault().getMongoInstance(mongoName).isDeleted();
			if (!viewConnNames.contains(mongoName)
					&& !isDeleted)
			{
				// if we get here, we did not find a tree entry for a mongo connection we have - create it.
				Connection conn = new Connection(mongoName);
				conn.setViewer(this);
				content.getRoot().addChild(conn);
				viewer.refresh(true);
				conn.doubleClickAction(); // hack to get the expansion arrow to show immediately in the tree view
			}
			else if (isDeleted)
			{
				// Find the child and delete it:
				for (TreeObject obj : this.content.getRoot().getChildren())
				{
					if (obj instanceof Connection)
					{
						String connName = ((Connection) obj).getName();
						if (connName.equals(mongoName))
						{
							content.getRoot().removeChild(obj);
							viewer.refresh(false);
						}
					}
				}
				MeclipsePlugin.getDefault().removeMongo(mongoName);
			}
		}
	}
	
	private Set<String> getConnNames()
	{
		Set<String> returnVal = new HashSet<String>();
		for (TreeObject obj : this.content.getRoot().getChildren())
		{
			if (obj instanceof Connection)
			{
				returnVal.add(((Connection) obj).getName());
			}
		}
		return returnVal;
	}

	public TreeViewer getViewer() {
		return this.viewer;
	}
}