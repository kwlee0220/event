package event.support;

import event.EventPublisher;
import event.EventSubscriber;


public class SubscriberDesc {
	private final EventPublisher m_outPort;
	final String m_id;
	final Object m_filter;
	final EventSubscriber m_subscriber;
	
	static {
//		MVEL.setThreadSafe(true);
	}
	
	SubscriberDesc(EventSubscriber subscriber) {
		this(null, null, subscriber);
	}
	
	SubscriberDesc(EventPublisher outPort, Object filter, EventSubscriber subscriber) {
		m_outPort = outPort;
		m_filter = filter;
		m_subscriber = subscriber;
		m_id = "" + System.identityHashCode(m_subscriber);
	}

	@Override
	public int hashCode() {
		return m_subscriber.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( obj == null || getClass() != obj.getClass() ) { 
			return false;
		}
		
		SubscriberDesc other = (SubscriberDesc)obj;
		return m_outPort.equals(other.m_outPort) && m_subscriber.equals(other.m_subscriber);
	}
	
	public String toString() {
		return m_filter != null ? String.format("[%s]%s", m_filter, m_subscriber)
								: String.format("[*]%s", m_subscriber);
	}
}