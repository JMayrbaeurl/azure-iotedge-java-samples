/**
 * 
 */
package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.microsoft.azure.sdk.iot.deps.twin.DeviceCapabilities;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.DeviceStatus;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.auth.AuthenticationType;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

/**
 * @author jurgenma
 *
 */
public class CreateEdgeDeviceApp {

	public static void main(String[] args) {
		
		RegistryManager manager = null;
		try {
			manager = RegistryManager.createFromConnectionString(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Device edgeDevice = Device.createDevice(args[1], AuthenticationType.SAS);
		edgeDevice.setStatus(DeviceStatus.Enabled); 
		DeviceCapabilities caps = new DeviceCapabilities();
		caps.setIotEdge(Boolean.TRUE);
		edgeDevice.setCapabilities(caps);
		try {
			edgeDevice = manager.addDevice(edgeDevice);
		} catch (JsonSyntaxException | IOException | IotHubException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			manager.removeDevice(edgeDevice);
		} catch (IllegalArgumentException | IOException | IotHubException e) {
			e.printStackTrace();
		}
	}
}
