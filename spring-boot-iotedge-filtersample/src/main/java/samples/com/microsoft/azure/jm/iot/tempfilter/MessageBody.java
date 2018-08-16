package samples.com.microsoft.azure.jm.iot.tempfilter;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class MessageBody {

	private static final String E_MACHINE_NAME = "machine";
    @Expose(serialize = true, deserialize = true)
    @SerializedName(E_MACHINE_NAME)
	private Machine machine;
    
	private static final String E_AMBIENT_NAME = "ambient";
    @Expose(serialize = true, deserialize = true)
    @SerializedName(E_AMBIENT_NAME)
	private Ambient ambient;
	
    private static final String E_TIMECREATED_NAME = "timeCreated";
    @Expose(serialize = true, deserialize = true)
    @SerializedName(E_TIMECREATED_NAME)
	private Date timeCreated;
	
	public MessageBody() {
	}

	/**
	 * @param machine
	 * @param ambient
	 * @param timeCreated
	 */
	public MessageBody(Machine machine, Ambient ambient, Date timeCreated) {
		super();
		this.machine = machine;
		this.ambient = ambient;
		this.timeCreated = timeCreated;
	}

	/**
	 * @return the machine
	 */
	public Machine getMachine() {
		return machine;
	}

	/**
	 * @param machine the machine to set
	 */
	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	/**
	 * @return the timeCreated
	 */
	public Date getTimeCreated() {
		return timeCreated;
	}

	/**
	 * @param timeCreated the timeCreated to set
	 */
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	/**
	 * @return the ambient
	 */
	public Ambient getAmbient() {
		return ambient;
	}

	/**
	 * @param ambient the ambient to set
	 */
	public void setAmbient(Ambient ambient) {
		this.ambient = ambient;
	}
	
}
