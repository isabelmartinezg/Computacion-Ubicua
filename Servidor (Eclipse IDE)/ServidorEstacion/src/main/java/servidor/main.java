package servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.paho.client.mqttv3.MqttException;

public class main {

	public static void main(String[] args) throws SQLException {
		
		Connection connection = null;
		Statement stmt = null;
	    ResultSet rs = null;
		
	    String url = "jdbc:mysql://localhost:3306/ubicua?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	    String user = "root";
	    String password = "123456789%";
		try {
	          Class.forName("com.mysql.cj.jdbc.Driver");
	      } catch (Exception e) {
	          System.out.println("Fail loading Driver");
	          e.printStackTrace();
	      }
		  
		
		SubscriberArduino subscriberArduino= new SubscriberArduino();
		subscriberArduino.start();

	}

}
