package servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PublisherEstadisticas extends Thread {
	String messageString; 
	Estadistica estadistica;
	Connection connection = null;
	Statement stmt = null;
    ResultSet rs = null;
    String url = "jdbc:mysql://localhost:3306/ubicua?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    String user = "root";
    String password = "123456789%";
	public PublisherEstadisticas() throws MqttException
	{
		
		
	}
	public void run()
	{
		try {
			mandarDatosMovil();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
  public void mandarDatosMovil() throws MqttException, SQLException {
	  connection = DriverManager.getConnection(url,user,password);
      stmt = connection.createStatement(); 
      estadistica = new Estadistica (connection,stmt);
	  String broker = "tcp://localhost:1883";
      messageString = estadistica.calcularEstadisticas();
      String topic="datosUsuario";
      boolean retained = false;
    
  
    	  
      
    try {
	    
	    MemoryPersistence  persistence = new MemoryPersistence();
	    MqttClient client = new MqttClient(broker, MqttClient.generateClientId(), persistence);
	    MqttConnectOptions opts = new MqttConnectOptions();
	    
	    MqttMessage message = new MqttMessage();
	    message.setRetained(retained);
	    client.connect(opts);
	    client.publish(topic, messageString.getBytes(),0,false);
	    System.out.println("\tMessage '"+ messageString +"' to 'datosUsuario'");
	    client.disconnect();
	    
	   
	   
    } 
	catch(MqttException me) {
        me.printStackTrace();
    }
      }

}
