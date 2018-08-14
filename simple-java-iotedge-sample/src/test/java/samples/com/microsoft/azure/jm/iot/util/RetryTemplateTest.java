package samples.com.microsoft.azure.jm.iot.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class RetryTemplateTest {

	@Test
	public void testFirstTryOK() {

		final String MESSAGE_STRING = "This is a test";
		
		RetryTemplate retryPolicy = new RetryTemplate(3);
		try {
			final String toCompare = retryPolicy.execute(new RetryCallback<String>() {
				@Override
				public String doWithRetry(Object context, int i) throws Exception {
					return MESSAGE_STRING;
				}
			}, null);
			assertEquals(MESSAGE_STRING, toCompare);
		} catch (Exception e) {
			fail("Failed with exception: " + e.getMessage());
		}
	}

	@Test
	public void testAlwaysFailing() {
		
		RetryTemplate retryPolicy = new RetryTemplate(3);
		try {
			retryPolicy.execute(new RetryCallback<String>() {
				@Override
				public String doWithRetry(Object context, int i) throws Exception {
					throw new IllegalStateException("Will never work");
				}
			}, null);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalStateException);
		}
	}
	
	@Test
	public void testFailTillLastAttempt() {
		
		RetryTemplate retryPolicy = new RetryTemplate(3);
		try {
			retryPolicy.execute(new RetryCallback<String>() {
				@Override
				public String doWithRetry(Object context, int i) throws Exception {
					if (i < 3)
						throw new IllegalStateException("Will never work");
					else
						return "Heureka";
				}
			}, null);
		} catch (Exception e) {
			fail("Failed with exception: " + e.getMessage());
		}
	}
}
