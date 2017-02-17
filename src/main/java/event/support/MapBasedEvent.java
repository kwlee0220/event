package event.support;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import event.Event;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class MapBasedEvent extends AbstractEvent implements Event {
	private final Set<Class<?>> m_types;
	private final Map<String,Object> m_properties;
	
	private MapBasedEvent(Set<Class<?>> types, Map<String,Object> props) {
		m_types = types;
		m_properties = Maps.newHashMap(props);
	}

	@Override
	public String[] getPropertyNames() {
		return m_properties.keySet().toArray(new String[0]);
	}

	@Override
	public Object getProperty(String name) {
		return m_properties.get(name);
	}

	@Override
	protected Set<Class<?>> getTypes() {
		return m_types;
	}

	public static class Builder {
		private final Set<Class<?>> m_types = Sets.newHashSet();
		private final Map<String,Object> m_properties = Maps.newHashMap();
		
		public MapBasedEvent build() {
			return new MapBasedEvent(m_types, m_properties);
		}
		
		public Builder addType(Class<?> intf) {
			m_types.add(intf);
			return this;
		}
		
		public Builder addProperty(String name, Object value) {
			m_properties.put(name, value);
			return this;
		}
	}
}
