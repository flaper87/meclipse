package org.mongodb.meclipse.editors;



import java.awt.event.KeyListener;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FontDialog;
import org.mongodb.meclipse.views.objects.Collection;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class MeclipseEditor extends MultiPageEditorPart implements
IResourceChangeListener {


	public static final String ID = "org.mongodb.meclipse.editors.meclipseEditor";

	private Collection col;
	/** The text editor used in page 0. */
	//private TextEditor editor;

	/** The font chosen in page 1. */
	private Font font;

	/** The text widget used in page 2. */
	//private StyledText text;
	
	private Text queryText;
	private Label queryLabel;
	
	public MeclipseEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
//		person.getAddress().setCountry(text2.getText());
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
//		if (person.getAddress().getCountry().equals(text2.getText())) {
//			return false;
//		}
		return true;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	protected void createPages() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 1;

//		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		queryLabel = new Label(composite, SWT.NONE);
//		queryLabel.setText("Query: ");
//		//gd.horizontalSpan = 100;
//		queryText = new Text(composite, SWT.BORDER);
//		queryText.setLayoutData(gd);
//		
//		Button fontButton = new Button(composite, SWT.NONE);
//		fontButton.setText("&Execute");
//		fontButton.setLayoutData(gd);
//		
//		fontButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent event) {
//				executeQuery();
//			}
//		});
		
		Composite expComposite = new Composite(composite, SWT.NONE);
		GridLayout expLayout = new GridLayout();
		expComposite.setLayout(expLayout);
		expLayout.numColumns = 2;
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		ExpandBar bar = new ExpandBar(composite, SWT.V_SCROLL);
		bar.setLayoutData(gd);
		
		for (DBObject o : col.getCollection().find().limit(10)) {
			createExpander(bar, o.toMap());
            System.out.println(o);
        }
		
		int index = addPage(composite);
		setPageText(index, "Properties");
	}

	@Override
	public void setFocus() {
	}
	
	private void executeQuery() {
		DBCursor cur = this.col.getCollection().find();

        while(cur.hasNext()) {
            System.out.println(cur.next());
        }
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void createExpander(ExpandBar bar, Map o) {
		// First item
		Composite composite = new Composite (bar, SWT.NONE);
		GridLayout layout = new GridLayout ();
		layout.numColumns = 2;
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
//		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		
		ExpandItem item0 = new ExpandItem (bar, SWT.NONE, 0);
		
		for (Object key : o.keySet()) {
			if (key == "_id" || key == "_ns")
				continue;
			Label item = new Label(composite, SWT.NONE);
			item.setText(key.toString());
			Label item2 = new Label(composite, SWT.NONE);
			item2.setText(o.get(key).toString());
		}
		
		item0.setText(o.get("_id").toString());
		item0.setHeight(500);
		item0.setControl(composite);
//		item0.setImage(image);
//		image.dispose();
		
//		setControl(0, expComposite);
//		addPage(0, expComposite);
	}
}
