package event.support;


import java.beans.IntrospectionException;

import com.google.common.base.Preconditions;

import event.Event;
import utils.Bean;
import utils.BeanPropertyNotFoundException;
import utils.Utilities;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class BeanBasedEvent implements Event {
	private final String[] m_typeIds;
	private final Bean m_bean;
	
	public static BeanBasedEvent from(Object obj) throws IntrospectionException {
		return new BeanBasedEvent(obj);
	}
	
	private BeanBasedEvent(Object obj) throws IntrospectionException {
		m_typeIds = Utilities.getInterfaceAllRecusively(obj.getClass())
							.stream()
							.map(Class::getName)
							.toArray(sz -> new String[sz]);
		m_bean = new Bean(obj);
	}

	@Override
	public String[] getEventTypeIds() {
		return m_typeIds;
	}

	@Override
	public String[] getPropertyNameAll() {
		return m_bean.getPropertyNames();
	}

	@Override
	public Object getProperty(String name) {
		Preconditions.checkNotNull(name, "Property name is null");
		
		try {
			return m_bean.getProperty(name);
		}
		catch ( BeanPropertyNotFoundException e ) {
			return null;
		}
		catch ( Exception e ) {
			return null;
		}
	}

}
