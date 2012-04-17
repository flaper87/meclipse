package org.mongodb.meclipse.editors;

import java.util.Iterator;
import java.util.Map;

import net.miginfocom.swt.MigLayout;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
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
public class MeclipseEditor
    extends MultiPageEditorPart
    implements IResourceChangeListener
{

    public static final String ID = "org.mongodb.meclipse.editors.meclipseEditor";

    private Button search;

    private Button more;

    private Text query;

    private Text skipV;

    private Text limitV;

    private Collection col;
    private ExpandBar bar;
private static final int maxElements = 20;
    private Iterator<DBObject> cursor;

    public MeclipseEditor()
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

        composite.setLayout( new MigLayout( "wrap 8", "[][][][][][][][]", "[30px!][100%-30px!]") );
        Label find = new Label( composite, SWT.FILL );
        find.setLayoutData( "w 30!" );
        find.setText( "Find:" );

        query = new Text( composite, SWT.FILL );
        query.setLayoutData( "w 100: 500: 600" );
        query.setText( "{}" );

        Label skip = new Label( composite, SWT.FILL );
        skip.setText( "Skip:" );

        skipV = new Text( composite, SWT.FILL );
        skipV.setText( "0" );

        Label limit = new Label( composite, SWT.FILL );
        limit.setText( "Limit:" );

        limitV = new Text( composite, SWT.FILL );
        limitV.setText( "10" );

        search = new Button( composite, SWT.PUSH );
        search.setToolTipText( "Go!" );
        search.setImage( reg.get( MeclipsePlugin.FIND_IMG_ID ) );
        search.addListener( SWT.Selection, new Listener()
        {
            @SuppressWarnings( "unchecked" )
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
                int count = 0;
                while (cursor.hasNext()) {
                    createExpander(bar, (Map<String, Object>) cursor.next().toMap(), col.getType());
                    if (count++ == maxElements) {
                        break;
                    }
                }
                more.setEnabled( cursor.hasNext() );
                } catch (JSONParseException e) {
                    //TODO display Error message
                    System.out.println(e);
                }
            }
        } );

        more = new Button( composite, SWT.PUSH );
        more.setToolTipText( "Get next 20 results" );
        more.setImage( reg.get( MeclipsePlugin.GET_NEXT_IMG_ID ) );
        more.setEnabled( false );
        more.addListener( SWT.Selection, new Listener() {

            @SuppressWarnings( "unchecked" )
            @Override
            public void handleEvent( Event arg0 )
            {
                if (cursor != null) {
                    int count = 0;
                    while(cursor.hasNext()) {
                        createExpander(bar,(Map<String, Object>) cursor.next().toMap(), col.getType());
                        if (count++ == maxElements) {
                            break;
                        }
                    }
                    more.setEnabled( cursor.hasNext() );
                }
            }
            
        });


        bar = new ExpandBar( composite, SWT.V_SCROLL );
        bar.setLayoutData( "span, w 100% !" );
//        bar.setLayoutData(  new GridData( SWT.FILL, SWT.FILL, true, true ) );

        for ( DBObject o : col.getCollection().find().limit( maxElements ) )
        {
            @SuppressWarnings( "unchecked" )
            Map<String, Object> map = (Map<String, Object>) o.toMap();
            createExpander( bar, map, col.getType() );
        }

        int index = addPage( composite );
        setPageText( index, "Properties" );
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

        for ( Object key : o.keySet() )
        {
            if ( key == "_id" || key == "_ns" )
                continue;
            Label keyLabel = new Label( composite, SWT.NONE );
            keyLabel.setText( key.toString() );
            Label valueLabel = new Label( composite, SWT.WRAP );
            Object value = o.get( key );
            valueLabel.setText( String.valueOf( value ) );
        }
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
        expandItem.setHeight( 500 );
        expandItem.setControl( composite );
    }
}
