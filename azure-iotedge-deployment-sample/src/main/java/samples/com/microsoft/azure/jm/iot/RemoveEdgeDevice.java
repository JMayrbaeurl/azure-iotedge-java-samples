/**
 * 
 */
package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

/**
 * @author jurgenma
 *
 */
public class RemoveEdgeDevice {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		RegistryManager manager = null;
		try {
			manager = RegistryManager.createFromConnectionString(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Device edgeDevice;
		try {
			edgeDevice = manager.getDevice(args[1]);
		} catch (JsonSyntaxException | IOException | IotHubException e1) {
			e1.printStackTrace();
			return;
		}
		
		try {
			manager.removeDevice(edgeDevice);
		} catch (IllegalArgumentException | IOException | IotHubException e) {
			e.printStackTrace();
		}
	}

}
