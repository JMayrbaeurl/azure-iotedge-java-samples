package com.microsoft.azure.spring.autoconfigure.iotedge;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.microsoft.azure.sdk.iot.device.ModuleClient;
import static org.assertj.core.api.Assertions.assertThat;

public class IoTEdgeAutoConfigurationTest {

	@Test
	public void testSimpleCreation() {
		
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(IoTEdgeAutoConfiguration.class);
            context.refresh();

            ModuleClient client  = null;
            try {
            	client = context.getBean(ModuleClient.class);
            } catch (Exception e) {
                assertThat(e).isExactlyInstanceOf(BeanCreationException.class);
            }

            assertThat(client).isNull();
        } 
        catch (Exception e) {
            assertThat(e).isExactlyInstanceOf(BeanCreationException.class);
        }
	}

}
