package org.mongodb.meclipse.editors;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import java.util.Iterator;
import java.util.Map;

import net.miginfocom.swt.MigLayout;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.views.objects.Collection;
import org.mongodb.meclipse.views.objects.Collection.CollectionType;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class CollectionEditor
    extends MultiPageEditorPart
    implements IResourceChangeListener
{

    public static final String ID = "org.mongodb.meclipse.editors.CollectionEditor";

    private Button search;

    private Button more;
    
    private Button all;

    private Text query;

    private Text skipV;

    private Text limitV;

    private Collection col;
    private ExpandBar bar;
    private static final int maxElements = 20;
    private Iterator<DBObject> cursor;
    
    private Listener runQuery = new Listener()
    {
        @Override
        public void handleEvent( Event arg0 )
        {
            int skip = 0;
            int limit = 0;
            try {
            skip = Integer.valueOf(skipV.getText() );
            } catch (NumberFormatException e) {
                //TODO just ignore it?
            }
            try{
                limit = Integer.valueOf(limitV.getText());
            } catch (NumberFormatException e) {
                //TODO just ignore it?
            }

            for (ExpandItem item : bar.getItems()) {
                item.dispose();
            }
            bar.setData(null);
            bar.setLayoutData( new GridData(SWT.FILL, SWT.FILL, true, true ) );
            try {
            cursor = col.getCollection().find((DBObject)JSON.parse( query.getText() )).limit(limit).skip(skip).iterator();
            loadEntries(false);
            
            } catch (JSONParseException e) {
                //TODO display Error message
                System.out.println(e);
            }
        }
    };
    
    public CollectionEditor()
    {
    }

    @Override
    public void doSave( IProgressMonitor monitor )
    {

    }

    @Override
    public void doSaveAs()
    {
    }

    @Override
    public void init( IEditorSite site, IEditorInput input )
        throws PartInitException
    {
        setSite( site );
        setInput( input );
        this.col = ( (CollectionEditorInput) input ).getObject();
        setPartName( col.getName() );

    }

    @Override
    public boolean isDirty()
    {
        return false; // our editor currently does not support editing
    }

    @Override
    public boolean isSaveAsAllowed()
    {
        return false;
    }

    @Override
    protected void createPages()
    {
        final Composite composite = new Composite( getContainer(), SWT.FILL );
        ImageRegistry reg = MeclipsePlugin.getDefault().getImageRegistry();

        composite.setLayout( new MigLayout( "wrap 9", "[][][][40px!][][40px!][][][]", "[30px!][]") );
        Label find = new Label( composite, SWT.FILL );
        find.setLayoutData( "w 30!" );
        find.setText( getCaption("collectionEditor.find") );

        query = new Text( composite, SWT.FILL );
        query.setLayoutData( "w 100: 500: 600" );
        query.setText( "{}" );

        Label skip = new Label( composite, SWT.FILL );
        skip.setText( getCaption("collectionEditor.skip") );

        skipV = new Text( composite, SWT.FILL );
        skipV.setText( "0" );
        skipV.setLayoutData( "w 40px!" );
        skipV.addListener(SWT.DefaultSelection, runQuery);

        Label limit = new Label( composite, SWT.FILL );
        limit.setText( getCaption("collectionEditor.limit") );

        limitV = new Text( composite, SWT.FILL );
        limitV.setText( "10" );
        limitV.setLayoutData( "w 40px!" );
        limitV.addListener(SWT.DefaultSelection, runQuery);

        search = new Button( composite, SWT.PUSH );
        search.setToolTipText( getCaption("collectionEditor.tooltip.search") );
        search.setImage( reg.get( MeclipsePlugin.FIND_IMG_ID ) );
        search.addListener( SWT.Selection, runQuery);

        more = new Button( composite, SWT.PUSH );
        more.setToolTipText( getCaption("collectionEditor.tooltip.next") );
        more.setImage( reg.get( MeclipsePlugin.GET_NEXT_IMG_ID ) );
        more.setEnabled( false );
        more.addListener( SWT.Selection, new Listener() {

            @Override
            public void handleEvent( Event arg0 )
            {
               loadEntries(false);
            }
            
        });
        
        all = new Button(composite, SWT.PUSH);
        all.setToolTipText(String.format( getCaption("collectionEditor.tooltip.getAll"),maxElements));
        all.setImage(reg.get(MeclipsePlugin.GET_ALL_IMG_ID));
        all.setEnabled(false);
        all.addListener(SWT.Selection, new Listener() {

        	@Override
        	public void handleEvent(Event arg0) {
        		loadEntries(true);
        	}
        });


        bar = new ExpandBar( composite, SWT.V_SCROLL );
        bar.setLayoutData( "span, w 100%-20px !,h 100%-50px !" );

        cursor = col.getCollection().find().limit(maxElements);
        loadEntries(false);

        int index = addPage( composite );
        setPageText( index, getCaption( "collectionEditor.tab.properties" ) );
    }

    @Override
    public void setFocus()
    {
    }

    @Override
    public void resourceChanged( IResourceChangeEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    public void createExpander( final ExpandBar bar, Map<String, Object> o, CollectionType collType )
    {
        System.out.println(o);
        // First item
        final Composite composite = new Composite( bar, SWT.FILL );
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
        composite.setLayout( layout );

        final ExpandItem expandItem = new ExpandItem( bar, SWT.NONE, 0 );
        GC gc = new GC(bar);
        int h =  gc.textExtent( "T" ,SWT.DRAW_DELIMITER).y;
        int c = 2;
        gc.dispose();

        for ( Object key : o.keySet() )
        {
            c++;
            if ( key == "_id" || key == "_ns" )
                continue;
            Label keyLabel = new Label( composite, SWT.NONE );
            keyLabel.setText( key.toString() );
            Label valueLabel = new Label( composite, SWT.WRAP );
            Object value = o.get( key );
            valueLabel.setText( String.valueOf( value ) );
        }
        System.out.println(c);
        Object value;
        switch ( collType )
        {
            case SYSINDEX:
                value = o.get( "ns" ).toString() + "." + o.get( "name" ).toString();
                break;
            default:
                value = o.get( "_id" );
                break;
        }
        expandItem.setText( String.valueOf( value ) );
        expandItem.setHeight( h * c );
        expandItem.setControl( composite );
    }
    
    @SuppressWarnings("unchecked")
	public void loadEntries(boolean ignoreLimit) {
    	 if (cursor != null) {
             int count = 0;
             while(cursor.hasNext()) {
                 createExpander(bar,(Map<String, Object>) cursor.next().toMap(), col.getType());
                 if (count++ == maxElements && !ignoreLimit) {
                     break;
                 }
             }
             more.setEnabled(cursor.hasNext());
             all.setEnabled(cursor.hasNext());
         }
    }
}
