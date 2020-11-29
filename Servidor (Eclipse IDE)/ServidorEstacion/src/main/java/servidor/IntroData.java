/*
 * Clase encargada de insertar datos en la base de datos
 */
package servidor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class IntroData {
    private Connection connection;
    private Statement stmt;
    
    public IntroData(Connection connection, Statement stmt) {
        this.connection = connection;
        this.stmt = stmt;
    }
    
    public void insertDatos(String temperatura, String humedad, String presion, String velocidad, String calidadAire){
        try 
        {
            stmt.executeUpdate("insert into datos values(default,'"+temperatura+"','"+humedad+"','"+presion+"','"+velocidad+"','"+calidadAire+"',"+"default)");
        }catch (SQLException ex) {
            System.out.println("Insertion failed");
            System.out.println("Fail executing update"+ex);
        }
    }
    
    //Me queda la de humedad, presion, velocidad, calidad del aire
    /*public void insertEstadistica(String media, String moda, String desviacion)
    {
        try
        {
            stmt.executeUpdate("insert into estadistica values(default,'"+estadistica.calcularModa(estadistica.comprobarRepetidos("Temperatura"), "Temperatura")+"','"+estadistica.calcularMedia("Temperatura")+"','"+estadistica.calcularMediana("Temperatura")+"','"+estadistica.calcularDesviacion("Temperatura")+"')");
        }catch (SQLException ex) {
            System.out.println("Insertion failed");
            System.out.println("Fail executing update"+ex);
        }
    }*/
    
    public double calcularCO2()
    {
    	double rs=0;
    	double ro=0;
    	double CO2=Math.pow(115.26*(rs/ro),-2.887);
    	return CO2;
    }
}
