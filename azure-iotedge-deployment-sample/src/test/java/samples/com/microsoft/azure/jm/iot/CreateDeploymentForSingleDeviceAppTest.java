package samples.com.microsoft.azure.jm.iot;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.microsoft.azure.sdk.iot.service.ConfigurationContent;

public class CreateDeploymentForSingleDeviceAppTest {

	@Test
	public void testReadDeploymentConfigurationFromFile() throws IOException {

		ConfigurationContent contents = CreateDeploymentForSingleDeviceApp.readDeploymentConfigurationFromFile(
				"C:\\Dev\\iotedge\\iotedge-java-samples\\azure-iotedge-deployment-sample\\config\\deployment.json");
		
		assertNotNull("Could not read deployment configuration", contents);
		assertTrue("Could not read deployment configuration. Unexpected device contents", contents.getDeviceContent() == null);
		assertNotNull("Could not read deployment configuration. No module contents", contents.getModulesContent());
	}

}
