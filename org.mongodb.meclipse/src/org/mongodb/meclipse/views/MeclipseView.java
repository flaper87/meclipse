package org.mongodb.meclipse.views;

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

import com.mongodb.Mongo;

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
	private Action action2;
	private Action doubleClickAction;
	private ViewContentProvider content = new ViewContentProvider();

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public MeclipseView() {
	}

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
		manager.add(action2);
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
				/*viewer.*/refresh(true);
			}
		};

		connection.setText("New Connection");
		connection.setToolTipText("New Connection");
		connection.setImageDescriptor(Images.getDescriptor(Images.PageCommit));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
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
	
	public void refresh(boolean notify) {
		refresh_();
		viewer.refresh(notify);
		
		if (notify)
			this.notifyChanged();
	}
	
	private void refresh_()
	{
		for (String mongoName : MeclipsePlugin.getDefault().getMongoNames())
		{
			for (TreeObject obj : this.content.getRoot().getChildren())
			{
				if (obj instanceof Connection)
				{
					String connName = ((Connection) obj).getName();
					if (connName.equals(mongoName))
						return; // already have this connection in the view
				}
			}
			// if we get here, we did not find a tree entry for a mongo connection we have - create it.
			Connection conn = new Connection(mongoName);
			conn.setViewer(this);
			content.getRoot().addChild(conn);
			viewer.refresh(true);
			conn.doubleClickAction(); // hack to get the expansion arrow to show immediately in the tree view				
		}
	}
	
	public TreeViewer getViewer() {
		return this.viewer;
	}

	// Taken from EclipseTracPlugin
	protected void notifyChanged() {
		/*
		try {
			savePreferences();
		} catch (ParserConfigurationException e) {
		} catch (IOException e) {
		} catch (TransformerException e) {
		}
		*/
	}

	// Taken from EclipseTracPlugin
	/*private String getServerInfoAsXML() throws ParserConfigurationException,
			IOException, TransformerException {
		Document doc = getDocument();
		Element config = doc.createElement("connectionsInfo"); //$NON-NLS-1$
		doc.appendChild(config);
		for (TreeObject obj : content.getRoot().getChildren()) {
			Connection server = (Connection)obj;
			Element serverElement = doc.createElement("connection"); //$NON-NLS-1$
			serverElement.setAttribute("name", server.getName()); //$NON-NLS-1$
			serverElement.setAttribute("host", server.getHost()); //$NON-NLS-1$
			serverElement.setAttribute("port", Integer.toString(server.getPort())); //$NON-NLS-1$
//			serverElement.setAttribute("username", server.getUsername()); //$NON-NLS-1$
//			serverElement.setAttribute("password", server.getPassword()); //$NON-NLS-1$
			config.appendChild(serverElement);
		}
		return serializeDocument(doc);
	}
	*/
	
	/*
	// Taken from EclipseTracPlugin
	private Document getDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		return doc;
	}
	*/

	/*
	// Taken from EclipseTracPlugin
	private String serializeDocument(Document doc) throws IOException,
			TransformerException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

		DOMSource source = new DOMSource(doc);
		StreamResult outputTarget = new StreamResult(s);
		transformer.transform(source, outputTarget);

		return s.toString("UTF8"); //$NON-NLS-1$			
	}
	*/
}