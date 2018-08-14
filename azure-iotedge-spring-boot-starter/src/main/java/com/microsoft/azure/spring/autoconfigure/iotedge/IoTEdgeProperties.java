package com.microsoft.azure.spring.autoconfigure.iotedge;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;

@Validated
@ConfigurationProperties("azure.iotedge")
public class IoTEdgeProperties {

	public static String DEFAULT_TRANSPORTTYPE = IotHubClientProtocol.MQTT.toString();
	
	public static int DEFAULT_MAXATTEMPTSFORRETRY = 5;
	
	public static long DEFAULT_INITIALINTERVALFORRETRY = 1000;
	
	public static double DEFAULT_MULTIPLIERFORRETRY = 1.5;
	
	public static long DEFAULT_MAXINTERVALFORRETRY = 3000;

	private String connectionString;

	private String transportType = IoTEdgeProperties.DEFAULT_TRANSPORTTYPE;
	
	private int maxAttemptsForRetry = IoTEdgeProperties.DEFAULT_MAXATTEMPTSFORRETRY;
	
	private long initialIntervalForRetry = IoTEdgeProperties.DEFAULT_INITIALINTERVALFORRETRY;
	
	private double multiplierForRetry = IoTEdgeProperties.DEFAULT_MULTIPLIERFORRETRY;
	
	private long maxIntervalForRetry = IoTEdgeProperties.DEFAULT_MAXINTERVALFORRETRY;

	/**
	 * @return the connectionString
	 */
	public String getConnectionString() {
		return connectionString;
	}

	/**
	 * @param connectionString the connectionString to set
	 */
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	/**
	 * @return the transportType
	 */
	public String getTransportType() {
		return transportType;
	}

	/**
	 * @param transportType the transportType to set
	 */
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	/**
	 * @return the maxAttemptsForRetry
	 */
	public int getMaxAttemptsForRetry() {
		return maxAttemptsForRetry;
	}

	/**
	 * @param maxAttemptsForRetry the maxAttemptsForRetry to set
	 */
	public void setMaxAttemptsForRetry(int maxAttemptsForRetry) {
		this.maxAttemptsForRetry = maxAttemptsForRetry;
	}

	/**
	 * @return the initialIntervalForRetry
	 */
	public long getInitialIntervalForRetry() {
		return initialIntervalForRetry;
	}

	/**
	 * @param initialIntervalForRetry the initialIntervalForRetry to set
	 */
	public void setInitialIntervalForRetry(long initialIntervalForRetry) {
		this.initialIntervalForRetry = initialIntervalForRetry;
	}

	/**
	 * @return the multiplierForRetry
	 */
	public double getMultiplierForRetry() {
		return multiplierForRetry;
	}

	/**
	 * @param multiplierForRetry the multiplierForRetry to set
	 */
	public void setMultiplierForRetry(double multiplierForRetry) {
		this.multiplierForRetry = multiplierForRetry;
	}

	/**
	 * @return the maxIntervalForRetry
	 */
	public long getMaxIntervalForRetry() {
		return maxIntervalForRetry;
	}

	/**
	 * @param maxIntervalForRetry the maxIntervalForRetry to set
	 */
	public void setMaxIntervalForRetry(long maxIntervalForRetry) {
		this.maxIntervalForRetry = maxIntervalForRetry;
	}
}
