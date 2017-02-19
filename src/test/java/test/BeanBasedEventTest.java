package test;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

import event.Event;
import event.support.BeanBasedEvent;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class BeanBasedEventTest {
	public static class BeanClass implements Runnable {
		public int getOne() {
			return 1;
		}
		
		public String getMessage() {
			return "msg";
		}

		@Override
		public void run() {
		}
	}
	
	@Test
	public void test01() {
		BeanClass obj = new BeanClass();
		Event ev = BeanBasedEvent.from(obj);
		
		Assert.assertEquals(1, ev.getProperty("one"));
		Assert.assertEquals("msg", ev.getProperty("message"));
		Assert.assertEquals(null, ev.getProperty("two"));

		Assert.assertArrayEquals(new String[]{Runnable.class.getName()}, ev.getEventTypeIds());
		Assert.assertEquals(Sets.newHashSet("one","message","class"),
							Sets.newHashSet(ev.getPropertyNames()));
	}
	
	@Test(expected=NullPointerException.class)
	public void test02() {
		BeanClass obj = new BeanClass();
		Event ev = BeanBasedEvent.from(obj);
		
		ev.getProperty(null);
	}
}
