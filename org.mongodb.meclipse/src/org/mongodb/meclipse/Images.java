package org.mongodb.meclipse;

//TAKEN From EclipseTracPlugin
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

public enum Images {
    
    Error("error.gif"), Modified("modified.png"),

    ServerConnected("server.png"), ServerDisconnected("server_disconnected.png"),
    NewServer("newServer.png"),

    PageCommit("page-commit.gif"), Macro("macro.gif"), Word("word.png"),
    Template("template.gif"),
    CreatePage("create_page.png"),

    Step("step.gif"),

    Trac16("trac_16.png"),
    
    Trac48("trac_48.png");
    
    // //////////////////////////////////////////////////
    
    private final String path;
    
    private Images( String filename )
    {
        this.path = "icons/" + filename;
    }
    
    public String getPath()
    {
        return path;
    }
    
    /**
     * 
     * 
     * @param img
     *            The image enumeration reference
     * @return The image instance
     */
    public static Image get( Images img )
    {
        ImageRegistry registry = MeclipsePlugin.getDefault().getImageRegistry();
        Image image = registry.get( img.path );
        if ( image == null )
        {
            ImageDescriptor desc = MeclipsePlugin.getImageDescriptor( img.path );
            registry.put( img.path, desc );
            image = registry.get( img.path );
        }
        return image;
    }
    
    public static ImageDescriptor getDescriptor( Images img )
    {
        ImageRegistry registry = MeclipsePlugin.getDefault().getImageRegistry();
        ImageDescriptor descriptor = registry.getDescriptor( img.path );
        if ( descriptor == null )
        {
            descriptor = MeclipsePlugin.getImageDescriptor( img.path );
            registry.put( img.path, descriptor );
        }
        return descriptor;
    }
    
    
    
    public static ImageDescriptor getShared( String img )
    {
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor( img );
    }
    
}
