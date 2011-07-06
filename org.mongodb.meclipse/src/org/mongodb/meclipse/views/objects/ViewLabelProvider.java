package org.mongodb.meclipse.views.objects;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.mongodb.meclipse.MeclipsePlugin;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class ViewLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof Connection)
		{
			ImageRegistry imgReg = MeclipsePlugin.getDefault().getImageRegistry();
			return imgReg.get(MeclipsePlugin.CONNECTION_IMG_ID);			
		}
		if (obj instanceof Database)
		{
			ImageRegistry imgReg = MeclipsePlugin.getDefault().getImageRegistry();
			return imgReg.get(MeclipsePlugin.DATABASE_IMG_ID);
		}
		if (obj instanceof Collection)
		{
			ImageRegistry imgReg = MeclipsePlugin.getDefault().getImageRegistry();
			return imgReg.get(MeclipsePlugin.COLLECTION_IMG_ID);
		}
		if (obj instanceof TreeParent)
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}