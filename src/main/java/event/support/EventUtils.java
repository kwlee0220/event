package event.support;

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
