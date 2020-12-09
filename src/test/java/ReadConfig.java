
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class ReadConfig {
	
	private String sVQToken;
	private String sVansahInstance;
	
	Properties VNSProperties;
	
	InputStream reader = null;
	public ReadConfig()
	{
		VNSProperties = new Properties();
		try{
		reader = new FileInputStream("config.vns");
		VNSProperties.load(reader);
		sVQToken = VNSProperties.getProperty("sVansahToken");
		sVansahInstance  = VNSProperties.getProperty("sVansahInstance");
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}finally{
			if(reader !=null)
			{
				try{
				reader.close();
				}catch(IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
	public String getsVQToken() {
		return sVQToken;
	}
	public String getsVansahInstance() {
		return sVansahInstance;
	}
	
	public static void main(String args[])
	{
		
		System.out.println(new ReadConfig().getsVQToken());
		System.out.println(new ReadConfig().getsVansahInstance());
	}

}
