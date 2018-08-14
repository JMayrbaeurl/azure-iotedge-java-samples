package samples.com.microsoft.azure.jm.iot;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.ModuleClient;

@SpringBootApplication
@RestController
public class SpringBootIoTEdgeSampleApp {

    @RequestMapping("/")
    public String home() {
        return "Hello IoT Edge World";
    }
    
    @RequestMapping("/sendMessage")
    public String sendMessage() {
    	
    	if(this.client != null) {
    		try {
				this.client.open();
			} catch (IOException e) {
				return "IOException on opening connection to Edge Hub: " + e.getMessage();
			}
    		this.client.sendEventAsync(new Message("Message from Spring boot application"), null, null, "output1");
    		return "Message was sent to Edge Hub";
    	} else
    		return "No connection to Edge Hub available";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootIoTEdgeSampleApp.class, args);
    }

    @Autowired
    private ModuleClient client;
}
