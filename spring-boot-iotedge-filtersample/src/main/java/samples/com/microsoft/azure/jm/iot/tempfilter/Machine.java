package samples.com.microsoft.azure.jm.iot.tempfilter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Machine {

	private static final String E_TEMPERATURE_NAME = "temperature";
    @Expose(serialize = true, deserialize = true)
    @SerializedName(E_TEMPERATURE_NAME)
	private double temperature;
	
    private static final String E_PRESSURE_NAME = "pressure";
    @Expose(serialize = true, deserialize = true)
    @SerializedName(E_PRESSURE_NAME)
	private double pressure;
	
	public Machine() {
	}

	
	public Machine(double temperature, double pressure) {
		super();
		
		this.temperature = temperature;
		this.pressure = pressure;
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
	 * @return the pressure
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * @param pressure the pressure to set
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	
}
