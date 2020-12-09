import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class VansahInterface {
	
	
	private  String VANSAH_URI;
	private  String VANSAH_PACKAGE;
	private  String VANSAH_CASE;
	private  String VANSAH_TYPE;
	private  String VANSAH_RELEASE;
	private  String VANSAH_BUILD;
	private  String VANSAH_ENVIRONMENT;
	private  String VANSAH_RESULT;
	private  String VANSAH_COMMENT;
	private  String VANSAH_AGENT;
	private  String VANSAH_TOKEN;
	private int HTTPS_RESPONSE_CODE;
	private String  HTTPS_RESULT;
	
	static VansahInterface vansahinterface=null;
	
	 /**
	 * Default Constructor 
	 */
	//Default constructor if using use the UpdateResultsInVansah(String, String ,String ,String ,String ,String , String , String ,  String , String , String) to update results
	public VansahInterface()
	{
		
	}
	
	 /**

	 * Singleton overloaded constructor
	 * @param clientName String value
	 * @param packageName String value
	 * @param type String value
	 * @param release String value
	 * @param build String value
	 * @param environment String value
	 * @param agent String value
	 * @param token String value
	 */
	protected VansahInterface(String clientName, String packageName,String type,String release,String build,String environment, String agent, String token)
	{
		VANSAH_URI=clientName;
		VANSAH_PACKAGE= packageName;
		VANSAH_TYPE=type;
		VANSAH_RELEASE=release;
		VANSAH_BUILD=build;
		VANSAH_ENVIRONMENT=environment;
		VANSAH_AGENT=agent;
		VANSAH_TOKEN=token;	
	}
	
		/**
		 * Method to get singelton Instance of VansahInterface Object
		 * @param clientName String value
		 * @param packageName String value
		 * @param type String value
		 * @param release String value
		 * @param build String value
		 * @param environment String value
		 * @param agent String value
		 * @param token String value
		 * @return VansahInterface object
		 */
	public static VansahInterface getInstance(String clientName, String packageName,String type,String release,String build,String environment, String agent, String token)
	{
		if(vansahinterface==null)
		{
			
			vansahinterface=new VansahInterface(clientName, packageName, type, release, build, environment,  agent,  token);
			
		}
		return vansahinterface;
		
	}
	
	
	public String UpdateSendLog(String packageName, String testCase, String release, String environment, String result, String comment, String  requirement, String build, String append )
	{
		
		return "";
	}
	
	/**
	 * Method to update a test case in Vansah 
	 * @param clientName String value
	 * @param packageName String value
	 * @param type String value
	 * @param release String value
	 * @param build String value
	 * @param environment String value
	 * @param agent String value
	 * @param token String value
	 * @param testcaseID String value
	 * @param resultStatus String value
	 * @param testComment String value
	 * @return HTTPS_RESULT String
	 */
	public String UpdateResultsInVansah(String clientName, String packageName,String type,String release,String build,String environment, String agent, String token,  String testcaseID, String resultStatus, String testComment)
	{	
		VANSAH_PACKAGE= packageName;
		VANSAH_TYPE=type;
		VANSAH_RELEASE=release;
		VANSAH_BUILD=build;
		VANSAH_ENVIRONMENT=environment;
		VANSAH_AGENT=agent;
		VANSAH_TOKEN=token;

		
		VANSAH_CASE= testcaseID;
		if(resultStatus.equalsIgnoreCase("pass"))
			VANSAH_RESULT ="1";
		if(resultStatus.equalsIgnoreCase("fail"))
			VANSAH_RESULT ="2";
		if(resultStatus.equalsIgnoreCase("N/A"))
			VANSAH_RESULT ="0";
		VANSAH_COMMENT=testComment.replace(" ", "%20");
			
		//sURL=https://testpoint.vansah.net/atsi/urlsave/save.php?&PID=1455598767&RESPONSE=12.579&
		//AGENT=VSAM.TP-001&TP_STOP=16/02/2016 15:59:40&LOGDESC=&COUNTERNAME=Login-VANSAH&TYPE=0&RESULT=0&THRESHOLD=0&
		//USERPROFILE=BIKASH.KHANAL&VIDEO=&CPU=0&MEM=0&PROCESS=0&TOKEN=FLEXATSI15
		
		/*
		 * sURL=https://testpoint.vansah.net/atsi/urlsave/save.php?&PID=1455759144&RESPONSE=0.795&
		 * AGENT=VMH17741&TP_STOP=18/02/2016 12:32:24&LOGDESC=&COUNTERNAME=Load-VANSAH&TYPE=0&RESULT=0&
		 * THRESHOLD=0&USERPROFILE=BIKASH.KHANAL&VIDEO=&CPU=0&MEM=0&PROCESS=0&TOKEN=FLEXATSI15
		 */
		
		String URI = "https://"+VANSAH_URI+".vansah.net/atsi/save_to_testlog.php?&PACKAGE="+VANSAH_PACKAGE+"&CASE="+VANSAH_CASE+"&TYPE="+VANSAH_TYPE+
					"&RELEASE="+VANSAH_RELEASE+"&BUILD="+VANSAH_BUILD+"&ENVIRONMENT="+VANSAH_ENVIRONMENT+"&RESULT="+VANSAH_RESULT+"&COMMENT="
				+VANSAH_COMMENT+"&AGENT="+VANSAH_AGENT+"&TOKEN="+VANSAH_TOKEN;
		System.out.println("Vansa URI :" + URI);
	       HttpURLConnection urlConnection;
	        try {
	            //Connect
	            urlConnection = (HttpURLConnection) ((new URL(URI).openConnection()));
	            urlConnection.setRequestMethod("GET");
	            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
	            HTTPS_RESPONSE_CODE = urlConnection.getResponseCode();
	         
	            BufferedReader in = new BufferedReader(
	                    new InputStreamReader(urlConnection.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	            HTTPS_RESULT=response.toString(); 
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

		return HTTPS_RESULT;
		
	    }
	
	/**
	 * Method to update a test case in Vansah
	 * @param testcaseID String value
	 * @param resultStatus String value
	 * @param testComment String value
	 * @return HTTPS_RESULT String
	 */
	
	public String UpdateResultsInVansah(String testcaseID, String resultStatus, String testComment)
	{

		
		VANSAH_CASE= testcaseID;
		if(resultStatus.equalsIgnoreCase("pass"))
			VANSAH_RESULT ="1";
		if(resultStatus.equalsIgnoreCase("fail"))
			VANSAH_RESULT ="2";
		if(resultStatus.equalsIgnoreCase("N/A"))
			VANSAH_RESULT ="0";
		VANSAH_COMMENT=testComment.replace(" ", "%20");
			
		
			String URI1 = "https://"+VANSAH_URI+".vansah.net/atsi/save_to_testlog.php?&PACKAGE="+VANSAH_PACKAGE+"&CASE="+VANSAH_CASE+"&TYPE="+VANSAH_TYPE+
					"&RELEASE="+VANSAH_RELEASE+"&BUILD="+VANSAH_BUILD+"&ENVIRONMENT="+VANSAH_ENVIRONMENT+"&RESULT="+VANSAH_RESULT+"&COMMENT="+VANSAH_COMMENT+"&AGENT="+VANSAH_AGENT+"&TOKEN="+VANSAH_TOKEN;
		
			
			//https://testpoint.vansah.net/atsi/add_testlog.php?&PACKAGE=Vansah-AUT&CASE=Verify Login&RELEASE=R5.0&ENVIRONMENT=SYS&RESULT=2&COMMENT=User logged Out&AGENT=VMH17741&APPEND=1&REQUIREMENT=User can login&BUILD=9.5.6.0.1&TOKEN=d87c7570abf1e66d74bfc5a5c7a4a66b
	       
			HttpURLConnection urlConnection;
	        try {
	        	String URI = URLEncoder.encode(URI1, "UTF-8");
	            //https get method with required parameters
	            urlConnection = (HttpURLConnection) ((new URL(URI).openConnection()));
	            urlConnection.setRequestMethod("GET");
	           // urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
	            HTTPS_RESPONSE_CODE = urlConnection.getResponseCode();
	         
	            BufferedReader in = new BufferedReader(
	                    new InputStreamReader(urlConnection.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	            HTTPS_RESULT=response.toString(); 
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

		return HTTPS_RESULT;
		
	    }

	/**
	 * Method to get the last https response code from Vansah
	 * @return HTTPS_RESPONSE_CODE int
	 */
	
	/*
	 * sQAURL=https://testpoint.vansah.net/atsi/add_testlog.php?&PACKAGE=Vansah-AUT&CASE=Verify Login&RELEASE=R5.0&
	 * ENVIRONMENT=SYS&RESULT=2&COMMENT=User logged Out&AGENT=VMH17741&APPEND=1&REQUIREMENT=User can login&BUILD=9.5.6.0.1&
	 * TOKEN=d87c7570abf1e66d74bfc5a5c7a4a66b
	 */
	/**
	 * Method to update a test case in Vansah
	 * @param packageName String value
	 * @param result String value
	 * @param testComment String value
	 * @return HTTPS_RESULT String
	 */
	
	public void updateSendLog(String packageName, String release, String environment, String result, String comment)
	{
		
	}
	
	public int getHttpsStatus()
	{
		return HTTPS_RESPONSE_CODE;
		
	}
	
	/**
	 * Method to get the last update response from Vansah
	 * @return HTTPS_RESULT String
	 */
	public String getUpdateStatus()
	{
		return HTTPS_RESULT;
	}
}
