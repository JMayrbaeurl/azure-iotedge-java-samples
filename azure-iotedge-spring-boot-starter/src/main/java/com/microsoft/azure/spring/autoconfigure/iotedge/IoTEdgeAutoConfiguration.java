package com.microsoft.azure.spring.autoconfigure.iotedge;

import java.net.URISyntaxException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.ModuleClient;
import com.microsoft.azure.sdk.iot.device.exceptions.ModuleClientException;

@Configuration
@ConditionalOnClass(ModuleClient.class)
@EnableConfigurationProperties(IoTEdgeProperties.class)
public class IoTEdgeAutoConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(IoTEdgeAutoConfiguration.class);
	
	@Autowired
	private IoTEdgeProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public ModuleClient moduleClient() throws ModuleClientException, IllegalArgumentException, UnsupportedOperationException, URISyntaxException {
		
		LOG.info("Creating ModuleClient instance for IoT Edge Hub");
	
		try {
			if (this.properties.getConnectionString() != null)
				return this.doCreateModuleClientFromConnectionString();
			else
				return this.doCreateModuleClientWithDefaultTransport();
		} catch (ModuleClientException clientEx) {
			
			LOG.error("Exception on creating ModulClient", clientEx);
			
			throw clientEx;
		}
	}
	
	private String getTransportType() {
		
		return ((this.properties != null) && (this.properties.getTransportType() != null)) ? 
				this.properties.getTransportType() : IoTEdgeProperties.DEFAULT_TRANSPORTTYPE;
	}
	
	private ModuleClient createModuleClientFromConnectionString() throws IllegalArgumentException, UnsupportedOperationException, ModuleClientException, URISyntaxException {
		return new ModuleClient(this.properties.getConnectionString(),
				IotHubClientProtocol.valueOf(this.getTransportType()));
	}
	
	protected ModuleClient doCreateModuleClientFromConnectionString() throws ModuleClientException {
		
		RetryTemplate template = this.createRetryTemplate();
		
		ModuleClient client = template.execute(new RetryCallback<ModuleClient, ModuleClientException>() {
		    public ModuleClient doWithRetry(RetryContext context) throws ModuleClientException {
		        // business logic here
		    	try {
					return createModuleClientFromConnectionString();
				} catch (IllegalArgumentException | UnsupportedOperationException | URISyntaxException e) {
					
					LOG.error("Exception on creating Module Client from connection string", e);
					
					throw new ModuleClientException("Exception on creating Module Client from connection string", e);
				}
		    }
		});
		
		return client;
	}	
	
	private ModuleClient createModuleClientWithDefaultTransport() throws ModuleClientException {
		
		return ModuleClient.createFromEnvironment(
				IotHubClientProtocol.valueOf(this.getTransportType())); 
	}
	
	protected ModuleClient doCreateModuleClientWithDefaultTransport() throws ModuleClientException {
		
		RetryTemplate template = this.createRetryTemplate();
		
		ModuleClient client = template.execute(new RetryCallback<ModuleClient, ModuleClientException>() {
		    public ModuleClient doWithRetry(RetryContext context) throws ModuleClientException {
		        // business logic here
		    	return createModuleClientWithDefaultTransport();
		    }
		});
		
		return client;
	}
	
	protected RetryTemplate createRetryTemplate() {
	
		int maxAttempts = this.properties != null ? 
				this.properties.getMaxAttemptsForRetry() : IoTEdgeProperties.DEFAULT_MAXATTEMPTSFORRETRY;
		
		// Set the max attempts including the initial attempt before retrying
		// and retry on all exceptions (this is the default):
		SimpleRetryPolicy policy = new SimpleRetryPolicy(maxAttempts, 
				Collections.singletonMap(ModuleClientException.class, true));

		// Use the policy...
		RetryTemplate template = new RetryTemplate();
		template.setRetryPolicy(policy);

		ExponentialRandomBackOffPolicy backOffPolicy = new ExponentialRandomBackOffPolicy();
		backOffPolicy.setInitialInterval(this.properties != null ? this.properties.getInitialIntervalForRetry() : IoTEdgeProperties.DEFAULT_INITIALINTERVALFORRETRY);
		backOffPolicy.setMultiplier(this.properties != null ? this.properties.getMultiplierForRetry() : IoTEdgeProperties.DEFAULT_MULTIPLIERFORRETRY);
		backOffPolicy.setMaxInterval(this.properties != null ? this.properties.getMaxIntervalForRetry() : IoTEdgeProperties.DEFAULT_MAXINTERVALFORRETRY);
		template.setBackOffPolicy(backOffPolicy);
		
		return template;
	}
}
