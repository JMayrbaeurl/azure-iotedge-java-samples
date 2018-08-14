package samples.com.microsoft.azure.jm.iot.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author jurgenma
 *
 */
public class RetryTemplate {

	private final int numberOfAttempts;

	private final Logger logger = LogManager.getLogger(RetryTemplate.class);

	public RetryTemplate(int numberOfAttempts) {
		super();
		this.numberOfAttempts = numberOfAttempts;
	}

	/**
	 * @param retryCallback
	 * @return
	 * @throws E
	 */
	public final <T> T execute(RetryCallback<T> retryCallback, Object context)
			throws Exception {
		
		if (retryCallback == null)
			throw new IllegalArgumentException("Parameter 'retryCallback' must not be null");
		
		return doExecute(retryCallback, context);
	}

	/**
	 * @param retryCallback
	 * @return
	 * @throws E
	 */
	protected <T> T doExecute(RetryCallback<T> retryCallback, Object context)
			throws Exception
	{
		if (retryCallback == null)
			throw new IllegalArgumentException("Parameter 'retryCallback' must not be null");

		T result = null;
		Exception lastEx = null;
		
		boolean needsAnotherTry = true;
		int retryCounter = 1;
		while(needsAnotherTry && retryCounter <= this.numberOfAttempts) {

			logger.info("Retry number " + retryCounter);
			
			try {
				result = retryCallback.doWithRetry(context, retryCounter);
				needsAnotherTry = false;
				lastEx = null;
			} catch (Exception ex) {
				retryCounter++;
				lastEx = ex;
			}
		}
		
		if (lastEx != null)
			throw lastEx;
		
		return result;
	}

	/**
	 * @return the numberOfAttempts
	 */
	public final int getNumberOfAttempts() {
		return numberOfAttempts;
	}
}
