package servidor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PublisherUbicacion extends Thread {
	String messageString; 
	
	public PublisherUbicacion(String lat, String longi) throws MqttException
	{
		messageString = lat+","+longi;
		
	}
	public void run()
	{
		try {
			mandarUbicacion();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
  public void mandarUbicacion() throws MqttException {
	  
	  String broker = "tcp://localhost:1883";
      String topic="ubicacion";
      boolean retained = false;
      
    try {
	    
	    MemoryPersistence  persistence = new MemoryPersistence();
	    MqttClient client = new MqttClient(broker, MqttClient.generateClientId(), persistence);
	    MqttConnectOptions opts = new MqttConnectOptions();
	    
	    MqttMessage message = new MqttMessage();
	    message.setRetained(retained);
	    client.connect(opts);
	    client.publish(topic, messageString.getBytes(),0,false);
	    System.out.println("\tMessage '"+ messageString +"' to 'ubicacion'");
	    client.disconnect();
	   
	   
    } 
	catch(MqttException me) {
        me.printStackTrace();
    }
      }

}
