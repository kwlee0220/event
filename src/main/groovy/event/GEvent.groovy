package event

import com.google.common.base.Preconditions

import event.support.AbstractEvent

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
class GEvent extends AbstractEvent implements Event {
	Set<Class<?>> types = []
	Map<String,Object> properties = [:]
	
	GEvent(Map props = [:]) {
		properties = props
	}
	
	GEvent(Set<Class<?>> types, Map props) {
		this.types = types
		properties = props
	}
	
	def addType(Class<?> type) {
		types.add(type)
	}

	@Override
	public String[] getPropertyNames() {
		return properties.values() as String[];
	}
	
	@Override
    public Object getProperty(String name) {
		Preconditions.checkArgument(name != null, "Property name was null");
		
		def prop = properties[name]
		if ( !prop ) {
			throw new IllegalArgumentException("Property not found: name=" + name);
		}
		
		prop
    }
	
	def getAt(String name) {
		getProperty(name)
	}
	
	def putAt(String name, value) {
		properties[name] = value
	}
	
	def propertyMissing(String name) {
		getProperty(name)
	}
	
	def propertyMissing(String name, value) {
		properties[name] = value
	}
}
