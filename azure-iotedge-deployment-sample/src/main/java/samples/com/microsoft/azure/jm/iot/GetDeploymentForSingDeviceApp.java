/**
 * 
 */
package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwin;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

/**
 * @author jurgenma
 *
 */
public class GetDeploymentForSingDeviceApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DeviceTwin twin = null;
		
		try {
			twin = DeviceTwin.createFromConnectionString(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		DeviceTwinDevice device = new DeviceTwinDevice(args[1], args[2]);
		try {
			twin.getTwin(device);
		} catch (IllegalArgumentException | IotHubException | IOException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println(device.getDesiredProperties());
	}

}
