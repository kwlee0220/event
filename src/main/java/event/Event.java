package event;

import java.util.Arrays;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface Event {
	/**
	 * 이벤트가 지원하는 모든 이벤트 타입 식별자를 반환한다.
	 * 
	 * @return	타입 식별자 배열.
	 */
	public String[] getEventTypeIds();
	
	public default boolean isInstanceOf(final String typeId) {
		Preconditions.checkNotNull(typeId, "typeId is null");
		
		return Arrays.stream(getEventTypeIds())
					.anyMatch(tid -> tid.equals(typeId));
	}
	
	public default boolean isInstanceOf(Class<?> intfc) {
		Preconditions.checkNotNull(intfc, "intfc is null");
		
		return isInstanceOf(intfc.getName());
	}
    
	/**
	 * 이벤트에 포함된 모든 속성 이름들을 반환한다.
	 * 
	 * @return	속성 이름 배열.
	 */
	public String[] getPropertyNames();
    
	/**
	 * 주어진 속성 이름의 속성 값을 반환한다.
	 * 
	 * @param name	속성 이름
	 * @return	속성 값.
	 */
    public Object getProperty(String name);
    
	/**
	 * 주어진 속성 이름의 속성 값을 문자열 형태로 반환한다.
	 * 
	 * @param name	속성 이름
	 * @return	속성 값.
	 */
    public default String getPropertyAsString(String name) {
    	return (String)getProperty(name);
    }
    
	/**
	 * 주어진 속성 이름의 속성 값을 정수형 형태로 반환한다.
	 * 
	 * @param name	속성 이름
	 * @return	속성 값.
	 */
    public default int getPropertyAsInt(String name) {
    	return (Integer)getProperty(name);
    }
}
