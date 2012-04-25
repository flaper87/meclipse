package org.mongodb.meclipse.editors.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.mongodb.meclipse.editors.CollectionEditorInput;
import org.mongodb.meclipse.editors.CollectionEditor;
import org.mongodb.meclipse.views.MeclipseView;
import org.mongodb.meclipse.views.objects.Collection;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class ConnectionEditorCall extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Get the view
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		MeclipseView view = (MeclipseView) page.findView(MeclipseView.ID);
		// Get the selection
		IStructuredSelection selection = (IStructuredSelection) view.getViewer().getSelection();
		
		if (selection != null && selection instanceof IStructuredSelection) {
			Collection obj = (Collection)selection.getFirstElement();
			// If we had a selection lets open the editor
			if (obj != null) {
				CollectionEditorInput input = new CollectionEditorInput(obj);
				try {
					page.openEditor(input, CollectionEditor.ID);

				} catch (PartInitException e) {
					System.out.println(e.getStackTrace());
				}
			}
		}
		return null;
	}

}
