package org.mongodb.meclipse.views.objects.properties;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.mongodb.meclipse.views.objects.CollectionBase;

import com.mongodb.DBObject;

public class CollectionPropertySource implements IPropertySource {
	private static final String NAME = "NAME";
	private static final Object DOCUMENT_COUNT = "DOCUMENT_COUNT";
	private static final Object INDEXES = "INDEXES";
	private CollectionBase coll;

	public CollectionPropertySource(CollectionBase collectionBase) {
		this.coll = collectionBase;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[]{
				new PropertyDescriptor(NAME, getCaption("collectionPS.name")),
				new PropertyDescriptor(DOCUMENT_COUNT,
						getCaption("collectionPS.documents")),
				new PropertyDescriptor(INDEXES,
						getCaption("collectionPS.indexes")),};
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(NAME))
			return coll.getName();
		else if (id.equals(DOCUMENT_COUNT))
			return coll.getCollection().find(coll.getQuery()).count();
		else if (id.equals(INDEXES))
			return new IndexesPropertySource(coll.getCollection()
					.getIndexInfo());
		else
			return null;
	}

	class IndexesPropertySource implements IPropertySource {
		// private static final String INDEX = "INDEX";
		private List<DBObject> indexInfo;

		public IndexesPropertySource(List<DBObject> indexInfo) {
			this.indexInfo = indexInfo;
		}

		@Override
		public Object getEditableValue() {
			return null;
		}

		@Override
		public IPropertyDescriptor[] getPropertyDescriptors() {
			IPropertyDescriptor[] descriptors = new PropertyDescriptor[indexInfo
					.size()];
			for (int i = 0; i < descriptors.length; i++) {
				DBObject indexObj = indexInfo.get(i);
				descriptors[i] = new PropertyDescriptor(indexObj, indexObj.get(
						"name").toString());
			}
			return descriptors;
		}

		@Override
		/**
		 * @param id a DBObject instance as set in getPropertyDescriptors()
		 */
		public Object getPropertyValue(Object id) {
			if (id == null)
				return null;

			for (DBObject obj : indexInfo)
				if (obj == id)
					return obj.get("key");

			// didn't find the given index DBObject
			return null;
		}

		@Override
		public boolean isPropertySet(Object id) {
			return false;
		}

		@Override
		public void resetPropertyValue(Object id) {
		}

		@Override
		public void setPropertyValue(Object id, Object value) {
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub

	}

}
