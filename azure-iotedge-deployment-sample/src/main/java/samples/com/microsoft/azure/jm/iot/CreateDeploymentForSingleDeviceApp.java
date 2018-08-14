package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.microsoft.azure.sdk.iot.deps.serializer.ConfigurationContentParser;
import com.microsoft.azure.sdk.iot.service.ConfigurationContent;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

public class CreateDeploymentForSingleDeviceApp {
	
	private static final Logger logger = LogManager.getLogger(CreateDeploymentForSingleDeviceApp.class);
	
	public CreateDeploymentForSingleDeviceApp() {}

	/**
	 * @param args[0] : IoT Hub connection string for accessing RegistryManager
	 * @param args[1] : Device ID
	 * @param args[2] : File path to deployment manifest json file
	 */
	public static void main(String[] args) {
		
		RegistryManager mgr = null;
		
		try {
			mgr = RegistryManager.createFromConnectionString(args[0]);
		} catch (IOException e) {
			logger.error("Exception on creating RegistryManager instance from connection string '"
					+ args[0] + "'", e);
			return;
		}
		
		try {
			ConfigurationContent content = readDeploymentConfigurationFromFile(args[2]);
			mgr.applyConfigurationContentOnDevice(args[1], content);
		} catch (IOException e) {
			logger.error("Exception on reading deployment manifest file or setting configuration on device", e);
			return;
		} catch (IotHubException e) {
			logger.error("Exception on reading deployment manifest file or setting configuration on device", e);
			return;
		}
	}

	protected static ConfigurationContent readDeploymentConfigurationFromFile(String filepath) throws IOException {
		
		byte[] encoded = Files.readAllBytes(Paths.get(filepath));
		String jsonString = new String(encoded, Charset.forName("UTF-8"));
		
		ConfigurationContentParser parser = new ConfigurationContentParser(jsonString);
		ConfigurationContent result = new ConfigurationContent();
		result.setDeviceContent(parser.getDeviceContent());
		result.setModulesContent(parser.getModulesContent());
		
		return result;
	}
}
