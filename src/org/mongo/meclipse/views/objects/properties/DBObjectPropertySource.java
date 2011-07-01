package org.mongo.meclipse.views.objects.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.mongodb.DBObject;

public class DBObjectPropertySource implements IPropertySource {

	private DBObject dbObj;

	DBObjectPropertySource (DBObject dbObj)
	{
		this.dbObj = dbObj;
	}
	
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] returnVal = new IPropertyDescriptor[dbObj.keySet().size()];
		int counter = 0;
		for (String propKey : dbObj.keySet())
		{
			returnVal[counter] = new PropertyDescriptor(propKey, propKey);
			counter++;
		}
		return returnVal;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (!(id instanceof String))
			return null;

		Object value = dbObj.get((String)id);
		if (value instanceof DBObject)
			return new DBObjectPropertySource((DBObject)value);
		
		return value;
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
