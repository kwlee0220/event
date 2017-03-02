package event

import com.google.common.base.Preconditions
import com.google.common.collect.Sets

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
	
	GEvent(Map props, Class<?>... types) {
		this.types = Sets.newHashSet(types);
		properties = props
	}
	
	GEvent(Map props, Collection<Class<?>> types) {
		this.types = Sets.newHashSet(types);
		properties = props
	}
	
	@Override
	protected Set<Class<?>> getTypes() {
		return types
	}
	
	def addType(Class<?> type) {
		types.add(type)
	}

	@Override
	public String[] getPropertyNames() {
		return properties.keySet() as String[];
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
	
	@Override
	public String toString() {
		if ( types.size() == 1) {
			"${types[0].name}$properties"
		}
		else if ( !types.isEmpty() ) {
			def typeNames = types.each { it.name }
			"$typeNames: $properties"
		}
		else {
			"GEvent: $properties"
		}
	}
}
