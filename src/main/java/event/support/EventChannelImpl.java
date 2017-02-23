package event.support;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import event.Event;
import event.EventChannel;
import utils.thread.AsyncConsumer;


/**
 * 
 * 본 클래스는 ThreadSafe하도록 구현되었다.
 * 
 * @author Kang-Woo Lee
 */
public class EventChannelImpl extends EventPublisherImpl implements EventChannel {
    private final AsyncConsumer<Event> m_eventPublisher;
    
    public static EventChannelImpl singleWorker() {
    	return new EventChannelImpl(1);
    }
    
    public static EventChannelImpl multipleWorkers(int workerCount) {
    	return new EventChannelImpl(workerCount);
    }
    
    public static EventChannelImpl get() {
    	return new EventChannelImpl(-1);
    }
    
    public static EventChannelImpl from(ExecutorService executor) {
    	return new EventChannelImpl(executor);
    }
	
	private EventChannelImpl(int workerCount) {
		if ( workerCount == 1 ) {
			m_eventPublisher = AsyncConsumer.singleWorker(m_publishEvent);
		}
		else if ( workerCount > 1 ) {
			m_eventPublisher = AsyncConsumer.multipleWorkers(m_publishEvent, workerCount);
		}
		else {
			m_eventPublisher = AsyncConsumer.from(m_publishEvent, Executors.newCachedThreadPool());
		}
	}
	
	private EventChannelImpl(AsyncConsumer<Event> eventPublisher) {
		m_eventPublisher = eventPublisher;
	}
	
	private EventChannelImpl(ExecutorService executor) {
		m_eventPublisher = AsyncConsumer.from(m_publishEvent, executor);
	}
	
	public Executor getExecutor() {
		return m_eventPublisher.getExecutor();
	}
	
	public final void publishEvent(final Event event) {
		m_eventPublisher.accept(event);
	}
	
	private final Consumer<Event> m_publishEvent = (event) -> {
		for ( final SubscriberDesc desc : getSubscriberAll() ) {
			notifyIfMatchedQuietly(desc, event);
		}
	};
}
