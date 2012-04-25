package org.mongodb.meclipse.views.objects.properties;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.mongodb.meclipse.views.objects.Database;

import com.mongodb.DBObject;

public class DatabasePropertySource implements IPropertySource {
	private static final String NAME = "NAME";
	private static final String PROFILING_LEVEL = "PROFILING_LEVEL";
	private Database database;

	public DatabasePropertySource(Database database)
	{
		this.database = database;
	}
	
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[]
       		{
	       		new PropertyDescriptor(NAME, getCaption("databasePS.name")),
	       		new PropertyDescriptor(PROFILING_LEVEL, getCaption("databasePS.profileLevel")),
       		};
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(NAME))
			return database.getDB().getName();
		else if (id.equals(PROFILING_LEVEL))
		{
			DBObject curProfileLevel = database.getDB().command("profile");
			return curProfileLevel.get("was");
		}
		else return null;
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
