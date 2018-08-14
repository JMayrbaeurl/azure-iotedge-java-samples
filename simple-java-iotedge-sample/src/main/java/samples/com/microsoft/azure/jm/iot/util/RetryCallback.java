package samples.com.microsoft.azure.jm.iot.util;

/**
 * Callback interface for an operation that can be retried using a
 * 
 *
 * @param <T> the type of object returned by the callback
 * @param <E> the type of exception it declares may be thrown
 *
 */
public interface RetryCallback<T> {

	/**
	 * Execute an operation with retry semantics. Operations should generally be
	 * idempotent, but implementations may choose to implement compensation
	 * semantics when an operation is retried.
	 * @param context the current retry context.
	 * @return the result of the successful operation.
	 * @throws E of type E if processing fails
	 */
	T doWithRetry(Object context, final int attemptCount) throws Exception;
}

