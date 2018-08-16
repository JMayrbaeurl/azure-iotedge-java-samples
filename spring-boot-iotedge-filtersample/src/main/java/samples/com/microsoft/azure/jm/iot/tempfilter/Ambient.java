/**
 * 
 */
package samples.com.microsoft.azure.jm.iot.tempfilter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author jurgenma
 *
 */
public final class Ambient {

	private static final String E_TEMPERATURE_NAME = "temperature";
    @Expose(serialize = true, deserialize = true)
    @SerializedName(E_TEMPERATURE_NAME)
	private double temperature;
	
    private static final String E_HUMIDITY_NAME = "humidity";
    @Expose(serialize = true, deserialize = true)
    @SerializedName(E_HUMIDITY_NAME)
	private double humidity;

    /**
	 * 
	 */
	public Ambient() {
	}

	/**
	 * @param temperature
	 * @param humidity
	 */
	public Ambient(double temperature, double humidity) {
		super();
		
		this.temperature = temperature;
		this.humidity = humidity;
	}

	/**
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the humidity
	 */
	public double getHumidity() {
		return humidity;
	}

	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
}
