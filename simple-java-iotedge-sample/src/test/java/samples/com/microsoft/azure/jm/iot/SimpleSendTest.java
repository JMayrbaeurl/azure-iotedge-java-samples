package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.junit.Test;

import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.ModuleClient;
import com.microsoft.azure.sdk.iot.device.exceptions.ModuleClientException;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;

@Ignore
public class SimpleSendTest {

	@Test
	public void test() throws IllegalArgumentException, UnsupportedOperationException, ModuleClientException, URISyntaxException, IOException, InterruptedException {

		String connectionString = "<Your connection string for the module>";
		ModuleClient client = new ModuleClient(connectionString, IotHubClientProtocol.MQTT);
		client.registerConnectionStatusChangeCallback(new IotHubConnectionStatusChangeCallback() {
			
			@Override
			public void execute(IotHubConnectionStatus status, IotHubConnectionStatusChangeReason statusChangeReason,
					Throwable throwable, Object callbackContext) {
				System.out.println("IoT Hub connection status changed: " + status.name());
			}
		}, null);
		client.open();
		
		Message message = new Message("Sent from Java module");
		client.sendEventAsync(message, new IotHubEventCallback() {
			@Override
			public void execute(IotHubStatusCode responseStatus, Object callbackContext) {
				System.out.println("IoT Hub responded with " + responseStatus.name());
			}
		}, message, "output1");
		
		Thread.sleep(2000);
		client.close();
	}
}
