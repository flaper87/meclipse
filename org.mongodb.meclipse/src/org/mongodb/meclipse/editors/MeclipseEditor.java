package org.mongodb.meclipse.editors;


import java.util.Map;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.mongodb.meclipse.views.objects.Collection;

import com.mongodb.DBObject;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class MeclipseEditor extends MultiPageEditorPart implements
IResourceChangeListener {


	public static final String ID = "org.mongodb.meclipse.editors.meclipseEditor";

	private Collection col;
	
	public MeclipseEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		this.col = ((CollectionEditorInput) input).getObject();
		setPartName(col.getName());

	}

	@Override
	public boolean isDirty() {
		return false; // our editor currently does not support editing
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	protected void createPages() {
		Composite composite = new Composite(getContainer(), SWT.FILL);
		
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 1;
		
		GridData gd = /*new GridData(GridData..FILL_HORIZONTAL);*/
			new GridData(SWT.FILL, SWT.FILL, true, true);
		ExpandBar bar = new ExpandBar(composite, SWT.V_SCROLL);
		bar.setLayoutData(gd);
		
		for (DBObject o : col.getCollection().find().limit(10)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o.toMap();
			createExpander(bar, map);
        }
		
		int index = addPage(composite);
		setPageText(index, "Properties");
	}

	@Override
	public void setFocus() {
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void createExpander(final ExpandBar bar, Map<String, Object> o) {
		// First item
		final Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout ();
		layout.numColumns = 2;
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		composite.setLayout(layout);
		
		final ExpandItem expandItem = new ExpandItem (bar, SWT.NONE, 0);
		
		for (Object key : o.keySet()) {
			if (key == "_id" || key == "_ns")
				continue;
			Label keyLabel = new Label(composite, SWT.NONE);
			keyLabel.setText(key.toString());
			Label valueLabel = new Label(composite, SWT.WRAP);
			Object value = o.get(key);
			valueLabel.setText(String.valueOf(value));
		}
		
		Object value = o.get( "_id" );
		expandItem.setText(String.valueOf( value ));
		expandItem.setHeight(500);
		expandItem.setControl(composite);
	}
}
