package event;

/**
 * <code>EventChannel</code>는 이벤트 채널 인터페이스를 정의한다.
 * 
 * <p><code>EventChannel</code>은 Publisher/Subscriber 기반 이벤트 전달 방식의
 * 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface EventChannel extends EventPublisher {
	/**
	 * 주어진 이벤트를 본 채널을 통해 발송한다.
	 * <p>
	 * 발송된 이벤트는 채널을 구독하는 모든 이벤트 구독자들에게 전달된다.
	 * 
	 * @param event	발송할 이벤트 객체.
	 * @throws NullPointerException	{@literal event}가 <code>null</code>인 경우.
	 */
	public void publishEvent(Event event);
}
