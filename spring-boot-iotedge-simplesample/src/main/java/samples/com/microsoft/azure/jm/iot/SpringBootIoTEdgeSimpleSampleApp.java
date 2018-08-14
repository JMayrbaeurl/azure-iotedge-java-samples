package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.ModuleClient;

@SpringBootApplication
public class SpringBootIoTEdgeSimpleSampleApp implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(SpringBootIoTEdgeSimpleSampleApp.class);
	
	@Autowired
	private ModuleClient client;
	
    public static void main(String[] args) {
        SpringApplication.run(SpringBootIoTEdgeSimpleSampleApp.class, args);
    }

	@Override
	public void run(String... args) {
		
		try {
			this.client.open();
		} catch (IOException e1) {
			logger.error("Exception creating ModuleClient from env: ", e1);
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
