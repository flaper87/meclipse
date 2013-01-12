package org.mongodb.meclipse.views.objects;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 * @author Joey Mink (walknwind@GitHub)
 */
public abstract class TreeParent extends TreeObject {

	public TreeParent(String name) {
		super(name);
	}

	public abstract TreeObject[] getChildren();

	// public boolean hasChildren() {
	// return true;
	// }
}