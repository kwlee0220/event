package event.support;

import java.util.Collection;
import java.util.Map;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import event.Event;
import event.EventPublisher;
import event.EventSubscriber;
import utils.ExceptionUtils;

/**
 * 
 * 본 클래스는 ThreadSafe하도록 구현되었다.
 * 
 * @author Kang-Woo Lee
 */
public class EventPublisherImpl implements EventPublisher {
	private static final Logger s_logger = LoggerFactory.getLogger(EventPublisherImpl.class);

	private final Map<String, SubscriberDesc> m_subscribers;
	
	public EventPublisherImpl() {
		m_subscribers = Maps.newHashMap();
	}

	public final int getEventSubscriberCount() {
		return m_subscribers.size();
	}

	@Override
	public final String subscribe(EventSubscriber subscriber) {
		Preconditions.checkNotNull(subscriber);
		
		return _subscribe(null, subscriber);
	}

	@Override
	public final String subscribe(String filter, EventSubscriber subscriber) {
		Preconditions.checkNotNull(filter, "event filter was null");
		Preconditions.checkNotNull(subscriber, "subscriber was null");
		
		return _subscribe(filter, subscriber);
	}

	@Override
	public final boolean unsubscribe(String subscriberId) {
		Preconditions.checkNotNull(subscriberId, "subscriberId was null");

		SubscriberDesc desc = m_subscribers.remove(subscriberId);
		if ( desc != null ) {
			s_logger.debug("unsubscribed: subscriber={}, channel={}", subscriberId, this);
		}
		
		return desc != null;
	}
	
	@Override
	public String toString() {
		return m_subscribers.toString();
	}
	
	protected Collection<SubscriberDesc> getSubscriberAll() {
		return m_subscribers.values();
	}
	
	/**
	 * 주어진 구독자에게 이벤트를 {@link EventSubscriber#receiveEvent(Event)} 메소드
	 * 호출을 통해 전달한다.
	 * <p>
	 * 만일 구독자가 필터를 갖고 있는 경우는 먼저 이벤트가 필터를 통과하는가 판정하여
	 * 통과하는 경우만 구독자에게 전달한다. 만일 필터를 통과하지 못하는 경우는 false를 반환한다.
	 * 필터 통과 여부 확인 과정 중 오류가 발생하는 경우는 통과하지 못한 것으로 간주하고,
	 * 대상 구독자의 {@link EventSubscriber#receiveEvent(Event)} 메소드 호출 과정 중
	 * 발생되는 RuntimeException은 호출자에게 전달된다.
	 * <br>
	 * 본 메소드는 인자에 대한 validity 검사를 수행하지 않아, 인자에 null이 전달되는 경우
	 * 오류가 발생될 수 있다.
	 * 
	 * @param	desc	전달 대상 구독자 기술자 객체.
	 * @param	event	전달할 이벤트 객체.
	 * @return	이벤트 전달 여부.
	 */
	protected final boolean notifyIfMatched(SubscriberDesc desc, Event event) {
		boolean passed = true;
		if ( desc.m_filter != null ) {
			try {
				passed = (Boolean)MVEL.executeExpression(desc.m_filter, event);
			}
			catch ( Throwable ignored ) {
				passed = false;
			}
		}
		
		if ( passed ) {
			desc.m_subscriber.receiveEvent(event);
		}
			
		return passed;
	}
	
	/**
	 * {@link #notifyIfMatched(SubscriberDesc, Event)}와 동일한 작업을 수행하지만, 구독자의
	 * {@link EventSubscriber#receiveEvent(Event)} 호출시 RuntimeException가 발생되면
	 * 이를 무시하고 false를 반환한다.
	 * 
	 * @param	desc	전달 대상 구독자 기술자 객체.
	 * @param	event	전달할 이벤트 객체.
	 * @return	이벤트 전달 여부.
	 */
	protected final boolean notifyIfMatchedQuietly(SubscriberDesc desc, Event event) {
		try {
			return notifyIfMatched(desc, event);
		}
		catch ( Throwable e ) {
			e = ExceptionUtils.unwrapThrowable(e);
			s_logger.info("failed in publishing event={} to subscriber={}, cause={}", event, desc, e);
			
			return false;
		}
	}
	
	private String _subscribe(String filter, EventSubscriber subscriber) {
		Object compiled = (filter != null) ? MVEL.compileExpression(filter) : null;
		SubscriberDesc desc = new SubscriberDesc(this, compiled, subscriber);
		m_subscribers.put(desc.m_id, desc);
		s_logger.debug("subscribed: subscriber={}, channel={}", subscriber, this);
		
		return desc.m_id;
	}
}
