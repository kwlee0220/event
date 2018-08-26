package event.support;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import event.Event;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public abstract class AbstractEvent implements Event {
	protected abstract Set<Class<?>> getTypes();

	@Override
	public String[] getEventTypeIds() {
		return getTypes().stream()
						.map(Class::getName)
						.toArray(sz -> new String[sz]);
	}

	@Override
	public boolean isInstanceOf(Class<?> intfc) {
		Objects.requireNonNull(intfc, "intfc is null");
		
		return getTypes().stream()
						.anyMatch(type -> intfc.isAssignableFrom(type));
	}

	@Override
	public boolean isInstanceOf(String typeId) {
		Objects.requireNonNull(typeId, "typeId is null");
		
		return Arrays.stream(getEventTypeIds())
					.anyMatch(tid -> tid.equals(typeId));
	}
}
