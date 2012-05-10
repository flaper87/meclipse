package org.mongodb.meclipse.views.objects;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.preferences.MongoInstance;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class ViewLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		if (obj instanceof Connection) {
			MongoInstance mongoInstance = MeclipsePlugin.getDefault()
					.getMongoInstance(((Connection) obj).getName());
			return mongoInstance.getName() + " (" + mongoInstance.getHost()
					+ ":" + mongoInstance.getPort() + ")";
		} else {
			return obj.toString();
		}
	}

	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		ImageRegistry imgReg = MeclipsePlugin.getDefault().getImageRegistry();
		if (obj instanceof Connection) {
			if (((Connection) obj).getMongo() != null) {
				return imgReg.get(MeclipsePlugin.CONNECTION_IMG_ID);
			} else {
				return imgReg.get(MeclipsePlugin.CONNECTION_ERROR_IMG_ID);
			}
		}
		if (obj instanceof Database) {
			return imgReg.get(MeclipsePlugin.DATABASE_IMG_ID);
		}
		if (obj instanceof Collection) {
			return imgReg.get(MeclipsePlugin.COLLECTION_IMG_ID);
		}
		if (obj instanceof Filter) {
			return imgReg.get(MeclipsePlugin.FILTER_IMG_ID);
		}
		if (obj instanceof TreeParent)
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}