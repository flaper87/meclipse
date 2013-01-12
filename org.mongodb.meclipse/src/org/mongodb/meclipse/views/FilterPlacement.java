package org.mongodb.meclipse.views;

import org.mongodb.meclipse.views.objects.TreeParent;

public final class FilterPlacement {
	private String placementStr;

	public FilterPlacement(TreeParent parent) {
		TreeParent treeObj = parent;
		do {
			if (placementStr == null)
				placementStr = treeObj.getName();
			else
				placementStr = treeObj.getName() + "." + placementStr;
			treeObj = treeObj.getParent();
		} while (treeObj != null);
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof FilterPlacement))
			return false;

		return placementStr.equals(((FilterPlacement) obj).placementStr);
	}

	public int hashCode() {
		int hash = placementStr.hashCode();
		return hash;
	}
}
