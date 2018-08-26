package event.support;


import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.beanutils.PropertyUtils;

import event.Event;
import utils.Utilities;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class BeanBasedEvent extends AbstractEvent implements Event {
	private final Object m_obj;
	private final AtomicReference<Set<Class<?>>> m_types = new AtomicReference<>();
	
	public static BeanBasedEvent from(Object obj) {
		return new BeanBasedEvent(obj);
	}
	
	private BeanBasedEvent(Object obj) {
		m_obj = obj;
	}

	@Override
	public String[] getPropertyNames() {
		return Arrays.stream(PropertyUtils.getPropertyDescriptors(m_obj))
						.map(desc -> desc.getName())
						.toArray(sz -> new String[sz]);
	}

	@Override
	public Object getProperty(String name) {
		Objects.requireNonNull(name, "Property name is null");
		
		try {
			return PropertyUtils.getProperty(m_obj, name);
		}
		catch ( Throwable e ) {
			return null;
		}
	}

	@Override
	protected Set<Class<?>> getTypes() {
		Set<Class<?>> types = m_types.get();
		if ( types == null ) {
			types = Utilities.getInterfaceAllRecusively(m_obj.getClass());
			m_types.compareAndSet(null, types);
		}
		
		return types;
	}
}
