package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubMessageResult;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.MessageCallback;
import com.microsoft.azure.sdk.iot.device.ModuleClient;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Pair;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.TwinPropertyCallBack;

import samples.com.microsoft.azure.jm.iot.tempfilter.MessageBody;

@SpringBootApplication
public class SpringBootIoTEdgeFilterSampleApp implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(SpringBootIoTEdgeFilterSampleApp.class);
	
	public SpringBootIoTEdgeFilterSampleApp() {
	}

    public static void main(String[] args) {
        SpringApplication.run(SpringBootIoTEdgeFilterSampleApp.class, args);
    }

    @Autowired
    private ModuleClient client;
    
    private final String TEMPERATURETHRESHOLDKEY = "TemperatureThreshold";
	private final int DEFAULTTEMPERATURETHRESHOLD = 25;

	@Override
	public void run(String... args) throws Exception {

		this.initModuleClient();
		
		while(true) {
			Thread.sleep(1000);
		}
	}
	
	private void initModuleClient() throws IOException {
		
		logger.debug("Initializing Edge module client");
		
		this.client.open();
		this.client.setMessageCallback("input1", new PrintAndFilterMessagesCallback(), 
				new Pair<ModuleClient, ModuleConfig>(this.client, this.GetConfiguration()));
	}
	
	/**
	 * @author jurgenma
	 *
	 */
	private static class PrintAndFilterMessagesCallback implements MessageCallback {

		private int counterValue = 0;
		
		@Override
		public IotHubMessageResult execute(Message message, Object callbackContext) {

			IotHubMessageResult result = IotHubMessageResult.COMPLETE;
			String strMsg;
			@SuppressWarnings("unchecked")
			ModuleClient moduleClient = ((Pair<ModuleClient, ModuleConfig>)callbackContext).getKey();
			@SuppressWarnings("unchecked")
			ModuleConfig mConfig = ((Pair<ModuleClient, ModuleConfig>)callbackContext).getValue();
			
			try {
				strMsg = new String(message.getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("Wrong encoding of message found", e);
				return IotHubMessageResult.REJECT;
			}
			
			logger.debug("Received message: " + ++counterValue + ", Body: [" + strMsg + "]");
			
			Gson gson = new Gson();
			MessageBody msg = gson.fromJson(strMsg, MessageBody.class);
			if (msg != null && msg.getMachine().getTemperature() > mConfig.getTemperatureThreshold()) {
				
				logger.info("Temperature " + msg.getMachine().getTemperature() +
	                    "exceeds threshold "+ mConfig.getTemperatureThreshold());
				
				Message filteredMessage = new Message(message.getBytes());
				for (int i = 0; i < message.getProperties().length; i++)
					filteredMessage.setProperty(message.getProperties()[i].getName(), message.getProperties()[i].getValue());
				filteredMessage.setProperty("MessageType", "Alert");
				
				moduleClient.sendEventAsync(filteredMessage, null, null, "alertOutput");
			}
			
			return result;
		}
	}
	
	private ModuleConfig GetConfiguration() {
		
		Map<String, Object> twin = new HashMap<String, Object>();
		
		AtomicBoolean Succeed = new AtomicBoolean(false);
		Succeed.set(false);
		try {
			this.client.startTwin(new IotHubEventCallback() {
				@Override
				public void execute(IotHubStatusCode responseStatus, Object callbackContext) {
					  if((responseStatus == IotHubStatusCode.OK) || (responseStatus == IotHubStatusCode.OK_EMPTY)) {
			                Succeed.set(true);
			            } else {
			                Succeed.set(false);
			            }
				}
			}, null, new TwinPropertyCallBack() {
				@Override
				public void TwinPropertyCallBack(Property property, Object context) {
					if (property.getKey().equals(TEMPERATURETHRESHOLDKEY)) {
						twin.put(TEMPERATURETHRESHOLDKEY, (Integer)property.getValue());
					}
				}
			}, twin);
		} catch (IllegalArgumentException | UnsupportedOperationException | IOException e) {
		}
		
		while(!Succeed.get())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		
		return new ModuleConfig((Integer)((twin.containsKey(TEMPERATURETHRESHOLDKEY) ? 
				twin.get(TEMPERATURETHRESHOLDKEY) : new Integer(DEFAULTTEMPERATURETHRESHOLD))));
	}
	
	private static class ModuleConfig {
		
		private int temperatureThreshold;

		/**
		 * @param temperatureThreshold
		 */
		public ModuleConfig(int temperatureThreshold) {
			super();
			this.temperatureThreshold = temperatureThreshold;
		}

		/**
		 * @return the temperatureThreshold
		 */
		public int getTemperatureThreshold() {
			return temperatureThreshold;
		}
	}
}
