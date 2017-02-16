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
public class MapBasedEvent implements Event {
	private final String[] m_typeIds;
	private final Map<String,Object> m_properties;
	
	private MapBasedEvent(String[] typeIds, Map<String,Object> props) {
		m_typeIds = typeIds;
		m_properties = Maps.newHashMap(props);
	}

	@Override
	public String[] getEventTypeIds() {
		return m_typeIds;
	}

	@Override
	public String[] getPropertyNameAll() {
		return m_properties.keySet().toArray(new String[0]);
	}

	@Override
	public Object getProperty(String name) {
		return m_properties.get(name);
	}

	public static class Builder {
		private final Set<String> m_typeIdSet = Sets.newHashSet();
		private final Map<String,Object> m_properties = Maps.newHashMap();
		
		public MapBasedEvent build() {
			return new MapBasedEvent(m_typeIdSet.toArray(new String[0]), m_properties);
		}
		
		public Builder addType(Class<?> intf) {
			m_typeIdSet.add(intf.getName());
			return this;
		}
		
		public Builder addProperty(String name, Object value) {
			m_properties.put(name, value);
			return this;
		}
	}
}
