/*
  Clase encargada de realizar estadisticas sacando los datos disponibles en la base de datos
  Calcula la media, mediana, moda y desviacion
 */
package servidor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Estadistica {
    private Connection connection;
    private Statement stmt;
    
    
    public Estadistica(Connection connection, Statement stmt) {
        this.connection = connection;
        this.stmt = stmt;
    }
    
   
    
    
    public String calcularModa(ArrayList<Integer> contadores,ArrayList<String> datos)
    {
        int max = 0;
        int posicion=-1; 
        for (int j=0;j<contadores.size();j++)
        {
            if (contadores.get(j)>max)
            {
                max=contadores.get(j);
                posicion=j;
            }
        }
        if (posicion==-1){
            return "No hay moda";
        }else
        {
            return datos.get(posicion);
        }
    }
    
    public boolean esPar(int i)
    {
        if (i%2==0)
        {
            return true;
        }
        return false;
    }
     
    public ArrayList<Integer> comprobarRepetidos(ArrayList<String> datos)
    {
        int cont =0;
        ArrayList<Integer> contadores = new ArrayList<>();
            for (int i=0; i<datos.size();i++)
            {
                for (int j=0;j<datos.size();j++)
                {
                    if ( Float.parseFloat(datos.get(i))== Float.parseFloat(datos.get(j)) && (j!=i))
                    {
                        cont++;
                    }
                }
                contadores.add(cont);
                cont=0;
        }
        return contadores;
    }
    
    public String calcularEstadisticas() throws SQLException
    {
    	
    	String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    	Date currentDate = new Date();
    	ArrayList<String> datosMedia = new ArrayList<String>();
    	ArrayList<String> datosModa = new ArrayList<String>();
    	DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
        Date diaAnterior = new Date();
        Date semanaAnterior = new Date();
        Date mesAnterior = new Date();
        Date yearAnterior = new Date();

        String mediaT="";
        String modaT="";
        String mediaH="";
        String modaH="";
        String mensaje="";
        
     // Get current date
        

        // convert date to localdatetime
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
       


    	
    		semanaAnterior = Date.from(localDateTime.minusDays(7).atZone(ZoneId.systemDefault()).toInstant());
    	
    		diaAnterior = Date.from(localDateTime.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
    		mesAnterior = Date.from(localDateTime.minusDays(30).atZone(ZoneId.systemDefault()).toInstant());
    		yearAnterior = Date.from(localDateTime.minusYears(1).atZone(ZoneId.systemDefault()).toInstant());
    		
    		
    		
    	for(int i = 0;i<4;i++)
    	{
    		if(i==0)
    		{
    			mediaT = mediaEnFechaAnterior(diaAnterior,"temperatura");
    			modaT = modaEnFecha(diaAnterior,"temperatura");
    			mediaH = mediaEnFechaAnterior(diaAnterior,"humedad");
    			modaH = modaEnFecha(diaAnterior,"humedad");
    		}else if(i==1)
    		{
    			mediaT = mediaEnFechaAnterior(semanaAnterior,"temperatura");
    			modaT = modaEnFecha(semanaAnterior,"temperatura");
    			mediaH = mediaEnFechaAnterior(semanaAnterior,"humedad");
    			modaH = modaEnFecha(semanaAnterior,"humedad");
    		}
    		else if(i==2)
    		{
    			mediaT = mediaEnFechaAnterior(mesAnterior,"temperatura");
    			modaT = modaEnFecha(mesAnterior,"temperatura");
    			mediaH = mediaEnFechaAnterior(mesAnterior,"humedad");
    			modaH = modaEnFecha(mesAnterior,"humedad");
    		}
    		else if(i==3)
    		{
    			mediaT = mediaEnFechaAnterior(yearAnterior,"temperatura");
    			modaT = modaEnFecha(yearAnterior,"temperatura");
    			mediaH = mediaEnFechaAnterior(yearAnterior,"humedad");
    			modaH = modaEnFecha(yearAnterior,"humedad");
    		}
    		
    		mensaje+=mediaT+"t,"+modaT+"t,"+mediaH+"h,"+modaH+"h;";
    	}
    	return mensaje;
               
    }
    public String mediasSemana() throws SQLException
    {
    	String mensaje="";
    	String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    	Date currentDate = new Date();
    	ArrayList<String> datosMedia = new ArrayList<String>();
    	ArrayList<String> datosModa = new ArrayList<String>();
    	DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
        Date dia1 = new Date();
        Date dia2 = new Date();
        Date dia3 = new Date();
        Date dia4= new Date();
        Date dia5= new Date();
        Date dia6= new Date();
        Date dia7= new Date();
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        dia1 = Date.from(localDateTime.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        dia2 = Date.from(localDateTime.minusDays(2).atZone(ZoneId.systemDefault()).toInstant());
        dia3 = Date.from(localDateTime.minusDays(3).atZone(ZoneId.systemDefault()).toInstant());
        dia4 = Date.from(localDateTime.minusDays(4).atZone(ZoneId.systemDefault()).toInstant());
        dia5 = Date.from(localDateTime.minusDays(5).atZone(ZoneId.systemDefault()).toInstant());
        dia6 = Date.from(localDateTime.minusDays(6).atZone(ZoneId.systemDefault()).toInstant());
        dia7 = Date.from(localDateTime.minusDays(7).atZone(ZoneId.systemDefault()).toInstant());
        
        String mediaT="";
		String mediaH="";
        
        for(int i = 0;i<7;i++)
    	{
    		if(i==0)
    		{
    			mediaT = mediaDia(dia1,"temperatura");
    			mediaH = mediaDia(dia1,"humedad");
    			
    		}else if(i==1)
    		{
    			mediaT = mediaDia(dia2,"temperatura");
    			mediaH = mediaDia(dia2,"humedad");
    		}
    		else if(i==2)
    		{
    			mediaT = mediaDia(dia3,"temperatura");
    			mediaH = mediaDia(dia3,"humedad");
    		}
    		else if(i==3)
    		{
    			mediaT = mediaDia(dia4,"temperatura");
    			mediaH = mediaDia(dia4,"humedad");
    		}else if(i==4)
    		{
    			mediaT = mediaDia(dia5,"temperatura");
    			mediaH = mediaDia(dia5,"humedad");
    		}
    		else if(i==5)
    		{
    			mediaT = mediaDia(dia6,"temperatura");
    			mediaH = mediaDia(dia6,"humedad");
    		}
    		else if(i==6)
    		{
    			mediaT = mediaDia(dia7,"temperatura");
    			mediaH = mediaDia(dia7,"humedad");
    		}
    		
    		mensaje+=mediaT+"t,"+mediaH+"h;";
    	}
    	
    	return mensaje;
    	
    }
    @SuppressWarnings("deprecation")
	public String mediaDia(Date Fecha,String dato) throws SQLException
    {
    	
    	String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    	DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
        float media;
        Date currentDate = new Date();
        ArrayList<String> datosMedia = new ArrayList<String>();
        ResultSet rs;
        rs = stmt.executeQuery("select "+ dato +" from datos where fecha BETWEEN '"+
        		dateFormat.format(Fecha)+"' AND '"+dateFormat.format(currentDate)+"';");
        
        while(rs.next())
        {
            datosMedia.add(rs.getString(1));
        }
        float suma = 0;
        for(int i =0;i<datosMedia.size();i++)
        {
        	suma+=Float.parseFloat(datosMedia.get(i));
        }
       
        
	   media = suma/datosMedia.size();
	   char c;
	   String mediaS="";
	   int contadorDecimales = 0;
	   int posicionComa = 0;
	  for(int j = 0;j<String.valueOf(media).length();j++)
	  {
		  c = String.valueOf(media).charAt(j);
		  if(c == ',')
		  {
			  c = '.';
			  posicionComa = j;
		  }
		  if(j>posicionComa) 
		  {
			  contadorDecimales++;
		  }
		  if(contadorDecimales<2)
		  {
			  mediaS+=c;
		  }	 
		  
	  }
	   return mediaS;
    }
    public String mediaEnFechaAnterior(Date Fecha,String dato) throws SQLException
    {
    	String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    	DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
        Date currentDate = new Date();
        ArrayList<String> datosMedia = new ArrayList<String>();
        float media;
        ResultSet rs;
    	rs = stmt.executeQuery("select "+ dato +" from datos where fecha BETWEEN '"+
        		dateFormat.format(Fecha)+"' AND '"+dateFormat.format(currentDate)+"';");
                while(rs.next())
                {
                    datosMedia.add(rs.getString(1));
                }
                float suma = 0;
                for(int i =0;i<datosMedia.size();i++)
                {
                	suma+=Float.parseFloat(datosMedia.get(i));
                }
               
                
           media = suma/datosMedia.size();
           
           
           char c;
           String mediaS="";
           int contadorDecimales = 0;
           int posicionComa = 0;
          for(int j = 0;j<String.valueOf(media).length();j++)
          {
        	  c = String.valueOf(media).charAt(j);
        	  if(c == ',')
        	  {
        		  c = '.';
        		  posicionComa = j;
        	  }
        	  if(j>posicionComa) 
        	  {
        		  contadorDecimales++;
        	  }
        	  if(contadorDecimales<2)
        	  {
        		  mediaS+=c;
        	  }	 
        	  
          }
          if(mediaS.equals("Na"))
			{
				return "0";
			}else
			{
				return mediaS;
			}
           
    }
    
    public String modaEnFecha(Date Fecha,String dato) throws SQLException
    {
    	String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    	DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
        Date currentDate = new Date();
        ArrayList<String> datosModa = new ArrayList<String>();
        ResultSet rs;
    	rs = stmt.executeQuery("select "+ dato +" from datos where fecha BETWEEN '"+
        		dateFormat.format(Fecha)+"' AND '"+dateFormat.format(currentDate)+"';");
                while(rs.next())
                {
                    datosModa.add(rs.getString(1));
                    
                }
              
                
                return calcularModa(comprobarRepetidos(datosModa),datosModa);
    	
    }
}
