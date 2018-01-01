package test;

import org.junit.Assert;
import org.junit.Test;

import event.support.MapBasedEvent;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class TestMapBasedEvent {
	@Test
	public void test01() {
		MapBasedEvent event = MapBasedEvent.builder().build();
		
		Assert.assertArrayEquals(new String[0], event.getPropertyNames());
		Assert.assertEquals(null, event.getProperty("a"));
	}
	
	@Test
	public void test02() {
		MapBasedEvent event = MapBasedEvent.builder()
											.addProperty("a", 1)
											.addProperty("b", "B")
											.build();
		
		Assert.assertArrayEquals(new String[]{"a", "b"}, event.getPropertyNames());
		Assert.assertEquals(1, event.getProperty("a"));
		Assert.assertEquals("B", event.getProperty("b"));
	}
}
