package servidor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class SubscriberArduino extends Thread{
	
	public SubscriberArduino()
	{
		
	}

  public void run()  {

    System.out.println("== START SUBSCRIBER ==");

    try
    {
    	MqttClient client=new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
        client.setCallback( new MqttCallBackArduino() );
        client.connect();
        client.subscribe("temperatura");
    }catch(MqttException e)
    {
    	e.printStackTrace();
    }
    

    

  }

}
