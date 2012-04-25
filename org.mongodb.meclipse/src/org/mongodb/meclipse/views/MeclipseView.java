package org.mongodb.meclipse.views;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.mongodb.meclipse.Images;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.views.objects.Root;
import org.mongodb.meclipse.views.objects.TreeObject;
import org.mongodb.meclipse.views.objects.ViewContentProvider;
import org.mongodb.meclipse.views.objects.ViewLabelProvider;
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
//	private DrillDownAdapter drillDownAdapter;
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
//		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(content);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(new Root(this));

		// Hook viewer up to the Eclipse selection provider:
		getSite().setSelectionProvider(viewer);
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
				"org.mongodb.meclipse.views");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		MeclipsePlugin.getDefault().setMongoDbView(this);
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
				mView.refreshMe();
			}
		};

		connection.setText(getCaption("connection.new"));
		connection.setToolTipText(getCaption("connection.new"));
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
			}
		});
	}

//	private void showMessage(String message) {
//		MessageDialog.openInformation(viewer.getControl().getShell(),
//				"Meclipse View", message);
//	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void refreshMe()
	{
		viewer.refresh(false);
	}

	public TreeViewer getViewer() {
		return this.viewer;
	}
}