package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.ModuleClient;
import com.microsoft.azure.sdk.iot.device.exceptions.ModuleClientException;

import samples.com.microsoft.azure.jm.iot.util.RetryCallback;
import samples.com.microsoft.azure.jm.iot.util.RetryTemplate;

public class SimpleJavaIoTEdgeApp {

	private static final Logger logger = LogManager.getLogger(SimpleJavaIoTEdgeApp.class);
	
	public SimpleJavaIoTEdgeApp() {}

	public static void main(String[] args) {

		ModuleClient client = null;
		
		logger.info("Simple Java module app started");
		
		try {
			client = SimpleJavaIoTEdgeApp.createModuleClientWithRetry();
			client.open();
		} catch (ModuleClientException e) {
			logger.error("Exception creating ModuleClient from env: ", e);
			return;
		} catch (IOException e) {
			logger.error("Exception open ModuleClient: ", e);
			return;
		}
		
		while(true) {
			
			logger.info("Now sending message to Edge hub");
			
			Message msg = new Message("Sent from Java module");
			EventCallback callback = new EventCallback();
			client.sendEventAsync(msg, callback, msg, "output1");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
		
		try {
			client.close();
		} catch (IOException e) {
			logger.error("Exception on closing ModuleClient", e);
		}
	}
	
	/**
	 * @return
	 * @throws ModuleClientException 
	 */
	protected static ModuleClient createModuleClientWithRetry() throws ModuleClientException {
		
		ModuleClient result = null;
		
		RetryTemplate retryPolicy = new RetryTemplate(3);
		try {
			result = retryPolicy.execute(new RetryCallback<ModuleClient>() {

				@Override
				public ModuleClient doWithRetry(Object context, int attemptCount) throws Exception {
					
					logger.info("Attempt number " + attemptCount + " to create ModuleClient");
					
					return ModuleClient.createFromEnvironment(IotHubClientProtocol.MQTT);
				}
			}, null);
		} catch (Exception e) {
			
			logger.error("Failed to create ModuleClient after 3 attempts", e);
			
			throw (ModuleClientException)e;
		}
		
		return result;
	}

    private static class EventCallback implements IotHubEventCallback
    {
        public void execute(IotHubStatusCode status, Object context)
        {
            Message msg = (Message) context;

            logger.info("IoT Hub responded to message "+ msg.getMessageId()  + " with status " + status.name());

            if (status==IotHubStatusCode.MESSAGE_CANCELLED_ONCLOSE)
            {
                logger.error("IoT Hub responded with cancelled on close");
            }
        }
    }
}
