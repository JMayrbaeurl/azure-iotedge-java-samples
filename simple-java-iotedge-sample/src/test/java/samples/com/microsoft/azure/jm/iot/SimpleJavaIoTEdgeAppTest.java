package samples.com.microsoft.azure.jm.iot;

import org.junit.Test;

import com.microsoft.azure.sdk.iot.device.exceptions.ModuleClientException;

public class SimpleJavaIoTEdgeAppTest {

	@Test(expected = ModuleClientException.class)
	public void test() throws ModuleClientException {

		SimpleJavaIoTEdgeApp.createModuleClientWithRetry();
	}

}
