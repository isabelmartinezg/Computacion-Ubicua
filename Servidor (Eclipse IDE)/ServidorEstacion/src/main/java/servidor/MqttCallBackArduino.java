package servidor;

import java.sql.*;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallBackArduino implements MqttCallback {

	
	Connection connection = null;
	Statement stmt = null;
    ResultSet rs = null;
    boolean ubicacion = true;
	
    String url = "jdbc:mysql://localhost:3306/ubicua?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    String user = "root";
    String password = "123456789%";
    
    
    
  public void connectionLost(Throwable throwable) {
    System.out.println("Connection to MQTT broker lost!");
  }

  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
	  try {
          Class.forName("com.mysql.cj.jdbc.Driver");
      } catch (Exception e) {
          System.out.println("Fail loading Driver");
          e.printStackTrace();
      }
	  String datos = new String(mqttMessage.getPayload());
	  ArrayList <String> misDatos = new ArrayList <String>();
	  String dato="";
	  for(int i = 0; i<=datos.length()-1;i++)
	  {
		  char c = datos.charAt(i);
		  if(c == ' ')
		  {
			  misDatos.add(dato);
			  dato = "";
		  }else if(i == datos.length()-1) {
			  dato=dato+c;
			  misDatos.add(dato);
			  
		  }else
		  {
			  dato=dato+c;
		  }
  		}
	  
      connection = DriverManager.getConnection(url,user,password);
      stmt = connection.createStatement(); 
      IntroData data = new IntroData(connection,stmt);
      if(Float.parseFloat(misDatos.get(6))>100.00)
      {
    	  misDatos.add(6, "0");
      }
       
      data.insertDatos(misDatos.get(0),misDatos.get(1), misDatos.get(2), misDatos.get(6), misDatos.get(3));
      connection.close();
    System.out.println("Message received:\t"+ new String(mqttMessage.getPayload()) );
    Publisher pub = new Publisher(misDatos);
    pub.start();
    PublisherEstadisticas pub1 = new PublisherEstadisticas();
    pub1.start();
    PublisherGraficos pub2 = new PublisherGraficos();
    pub2.start();
    PublisherUbicacion pub3 = new PublisherUbicacion(misDatos.get(4),misDatos.get(5));
    pub3.start();
  }

  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
  }
}
