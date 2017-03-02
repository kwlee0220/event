package event.support;

import java.util.Arrays;
import java.util.stream.Collectors;

import event.Event;
import event.EventPublisher;
import event.EventSubscriber;
import net.jcip.annotations.GuardedBy;
import rx.Observable;
import rx.Subscriber;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class EventUtils {
	private static final String[] EMPTY = new String[0];
    public static final Event NULL = new Event() {
		@Override
		public String[] getEventTypeIds() {
			return EMPTY;
		}

		@Override
		public String[] getPropertyNames() {
			return EMPTY;
		}

		@Override
		public Object getProperty(String name) {
			return EMPTY;
		}
    };
    
	public static final Event empty() {
		return NULL;
	}
	
	public static final String toString(Event event) {
		String typeNames = Arrays.stream(event.getEventTypeIds())
								.map(typeStr -> { 
									int ridx = typeStr.lastIndexOf('.');
									return ridx >= 0 ? typeStr.substring(ridx+1) : typeStr;
								})
								.collect(Collectors.joining(":"));
		String propsStr = Arrays.stream(event.getPropertyNames())
								.map(name -> name + ":" + event.getProperty(name))
								.collect(Collectors.joining(","));
		return typeNames + "[" + propsStr + "]";
	}
	
	public static Observable<Event> createObservable(EventPublisher publisher) {
		return Observable.using(()->new EventSubscription(publisher),
								 sub -> Observable.create(rxSub -> sub.m_rxSubscriber = rxSub),
								 EventSubscription::release);
	}
	
	public static Observable<Event> createObservable(EventPublisher publisher, String filter) {
		return Observable.using(()->new EventSubscription(publisher, filter),
								 sub -> Observable.create(rxSub -> sub.setRxSubscriber(rxSub)),
								 EventSubscription::release);
	}
	
	static class EventSubscription implements EventSubscriber {
		private final EventPublisher m_publisher;
		@GuardedBy("this") private String m_subsribeId;
		private Subscriber<? super Event> m_rxSubscriber;

		EventSubscription(EventPublisher publisher) {
			m_publisher = publisher;
			synchronized ( this ) {
				m_subsribeId = m_publisher.subscribe(this);
			}
		}

		EventSubscription(EventPublisher publisher, String filter) {
			m_publisher = publisher;
			synchronized ( this ) {
				m_subsribeId = m_publisher.subscribe(filter, this);
			}
		}
		
		synchronized void release() {
			m_publisher.unsubscribe(m_subsribeId);
			m_subsribeId = null;
		}
		
		void setRxSubscriber(Subscriber<? super Event> subscriber) {
			m_rxSubscriber = subscriber;
		}
		
		@Override
		public synchronized void receiveEvent(Event event) {
			if ( m_subsribeId != null ) {
				try {
					m_rxSubscriber.onNext(event);
				}
				catch ( Exception e ) {
					m_rxSubscriber.onError(e);
				}
			}
		}
	}
}
