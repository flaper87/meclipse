package org.mongodb.meclipse.views.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 * @author Joey Mink (walknwind@GitHub)
 */
public class TreeParent extends TreeObject {
	private Map<String, TreeObject> children = new HashMap<String, TreeObject>();

	public TreeParent(String name) {
		super(name);
	}

	public void clearChildren()
	{
		children.clear();
	}

	public void addChild(TreeObject child) {
		children.put(child.getName(), child);
		child.setParent(this);
	}
	public void removeChild(TreeObject child) {
		children.remove(child.getName());
		child.setParent(null);
	}
	public TreeObject [] getChildren() {
		return (TreeObject [])children.values().toArray(new TreeObject[children.size()]);
	}
	public boolean hasChildren() {
		return true;
	}
}