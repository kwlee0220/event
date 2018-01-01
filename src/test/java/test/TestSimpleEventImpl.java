package test;

import org.junit.Assert;
import org.junit.Test;

import event.Event;
import event.support.SimpleEventImpl;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class TestSimpleEventImpl {
	@Test
	public void test01() {
		Event event = new SimpleEventImpl(new String[0], new String[0], new Object[0]);
		
		Assert.assertArrayEquals(new String[0], event.getPropertyNames());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void test02() {
		Event event = new SimpleEventImpl(new String[0], new String[0], new Object[0]);
		
		Assert.assertEquals(null, event.getProperty("a"));
	}
	
	@Test
	public void test03() {
		Event event = new SimpleEventImpl(new String[0], new String[]{"a", "b"},
											new Object[]{1, "B"});
		
		Assert.assertArrayEquals(new String[]{"a", "b"}, event.getPropertyNames());
		Assert.assertEquals(1, event.getProperty("a"));
		Assert.assertEquals("B", event.getProperty("b"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void test04() {
		Event event = new SimpleEventImpl(new String[0], new String[]{"a", "b"},
											new Object[]{1, "B"});
		
		Assert.assertArrayEquals(new String[]{"a", "b"}, event.getPropertyNames());
		Assert.assertEquals(1, event.getProperty("c"));
	}
}
