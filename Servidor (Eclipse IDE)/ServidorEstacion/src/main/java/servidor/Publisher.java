package servidor;

import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Publisher extends Thread {


	String messageString; 
	public Publisher(ArrayList<String> mensaje) throws MqttException
	{
		messageString = mensaje.get(0)+","+ mensaje.get(1)+","+ mensaje.get(2)+","+mensaje.get(3)+","+mensaje.get(6); 
		
	}
	
	public void run()
	{
		try {
			mandarDatosMovil();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
  public void mandarDatosMovil() throws MqttException {
	  String broker = "tcp://localhost:1883";
      
      String topic="datosMovil";
      boolean retained = false;
    
    try {
	   
	    MemoryPersistence  persistence = new MemoryPersistence();
	    MqttClient client = new MqttClient(broker, MqttClient.generateClientId(), persistence);
	    MqttConnectOptions opts = new MqttConnectOptions();
	    
	    MqttMessage message = new MqttMessage();
	    message.setRetained(retained);
	    client.connect(opts);
	    client.publish("datosMovil", messageString.getBytes(),0,false);
	    
	    
	
	    System.out.println("\tMessage '"+ messageString +"' to 'datosMovil'");
	
	    client.disconnect();
	
	  
    } 
	catch(MqttException me) {
        me.printStackTrace();
    }

	}

  }

