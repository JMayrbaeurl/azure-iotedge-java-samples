package samples.com.microsoft.azure.jm.iot.tempfilter;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.google.gson.Gson;

public class MessageBodyTest {

	private final String testdata = "{\"machine\":{\"temperature\":117.89840784605967,\"pressure\":12.039059121703},\"ambient\":{\"temperature\":20.936969953326962,\"humidity\":25},\"timeCreated\":\"2018-08-16T15:33:13.5676071Z\"}";
	
	@Test
	public void testWriteJson() {
		
		Machine machine = new Machine(1.0, 2.0);
		MessageBody msg = new MessageBody();
		msg.setMachine(machine); msg.setTimeCreated(new Date());
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(msg);
		assertNotNull(jsonString);
	}
	
	@Test
	public void testReadFromJson() {
		
		Gson gson = new Gson();
		MessageBody msg = gson.fromJson(testdata, MessageBody.class);
		assertNotNull(msg);
	}
}
