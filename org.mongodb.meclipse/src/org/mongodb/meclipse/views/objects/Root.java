package org.mongodb.meclipse.views.objects;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.views.MeclipseView;

/**
 * 
 * @author Naoki Takezoe
 */
public class Root extends TreeParent {

	public Root(MeclipseView view){
		super("");
		setViewer(view);
	}
	
	@Override
	public TreeObject[] getChildren() {
		List<Connection> children = new ArrayList<Connection>();
		for (String mongoName : MeclipsePlugin.getDefault().getMongoNames()){
			Connection conn = new Connection(mongoName);
			conn.setParent(this);
			conn.setViewer(view);
			children.add(conn);
		}
		return children.toArray(new TreeObject[children.size()]);
	}
	
}
