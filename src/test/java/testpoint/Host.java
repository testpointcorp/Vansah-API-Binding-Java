package testpoint;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
public class Host
{
	
	private String computerName = "unknown";
	
	public Host()
	{
		
	}
	public String getUserProfile()
	{
		Map<String, String> map = System.getenv();
		String userName = map.get("USERNAME");
		return userName;
	}
	public String getUserName()
	{
		Map<String, String> map = System.getenv();
		String userName = map.get("USERNAME");
		return userName;
	}
	
	public String getComputerName()
	{
		try{
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			computerName = addr.getHostName();
		}
		catch(UnknownHostException ex)
		{
			System.out.println("Host Name Cannot Be Resolved!!");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return computerName;
	}
	
	public static void main(String[] args) {
		
		System.out.println(new Host().getComputerName());
		System.out.println(new Host().getUserName());

	}

}
