package org.mongo.meclipse;

import java.net.UnknownHostException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.mongodb.Mongo;
import com.mongodb.DB;

/**
 * The activator class controls the plug-in life cycle
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class MeclipsePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.mongo.meclipse";

	// The shared instance
	private static MeclipsePlugin plugin;
	private static Mongo connection;
	private static DB db;
	
	/**
	 * The constructor
	 */
	public MeclipsePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MeclipsePlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Taken from EclipseTracPlugin
	 * 
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    protected static ImageDescriptor getImageDescriptor( String path )
    {
        return imageDescriptorFromPlugin( PLUGIN_ID, path );
    }
}
