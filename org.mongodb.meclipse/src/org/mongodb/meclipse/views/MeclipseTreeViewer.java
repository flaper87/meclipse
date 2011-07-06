package org.mongodb.meclipse.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class MeclipseTreeViewer extends TreeViewer {

	private MeclipseView view;
	
	public MeclipseTreeViewer(Composite parent) {
		super(parent);
	}
	
	public MeclipseTreeViewer(Composite parent, int flags) {
		super(parent, flags);
	}
	
	public MeclipseTreeViewer(Composite parent, MeclipseView view, int flags) {
		this(parent, flags);
		this.view = view;
	}
	
	@Override
	public void refresh(boolean notify) {
		super.refresh();
		if (notify)
			view.notifyChanged();
	}
	

}
