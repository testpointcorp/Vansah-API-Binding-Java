import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Provide the classes necessary to  
 * update a particular testcase in  Vansah by providing the  
 * test case ID, Result status and Comment 
 * @version 1.0
 */

public class UpdateVansahTestStatus {
	private String VANSAH_URI;
	private String VANSAH_PACKAGE;
	private String VANSAH_CASE;
	private String VANSAH_TYPE;
	private String VANSAH_RELEASE;
	private String VANSAH_BUILD;
	private String VANSAH_ENVIRONMENT;
	private String VANSAH_RESULT;
	private String VANSAH_COMMENT;
	private String VANSAH_AGENT;
	private String VANSAH_TOKEN;
	
	private String result;
	
	
	private IntializeVansah VANSAH = IntializeVansah.getInstance( );

	
	 /**
	 * Method to add two number
	 * @param a int value
	 * @param b int value
	 * @return a+b
	 */
	public Boolean UpdateResults(String testcaseID, String testStatus, String testComment)
	{
	    
		VANSAH_PACKAGE= VANSAH.getPackage();
		VANSAH_TYPE=VANSAH.getType();
		VANSAH_RELEASE=VANSAH.getRelease();
		VANSAH_BUILD=VANSAH.getBuild();
		VANSAH_ENVIRONMENT=VANSAH.getEnvironment();
		VANSAH_AGENT=VANSAH.getAgent();
		VANSAH_TOKEN=VANSAH.getToken();
		
		VANSAH_CASE= testcaseID;
		if(testStatus.equalsIgnoreCase("pass"))
			VANSAH_RESULT ="1";
		if(testStatus.equalsIgnoreCase("fail"))
			VANSAH_RESULT ="2";
		if(testStatus.equalsIgnoreCase("N/A"))
			VANSAH_RESULT ="0";
		VANSAH_COMMENT=testComment.replace(" ", "%20");
			
		
			String URI = "https://testpoint.vansah.net/atsi/save_to_testlog.php?&PACKAGE="+VANSAH_PACKAGE+"&CASE="+VANSAH_CASE+"&TYPE="+VANSAH_TYPE+
					"&RELEASE="+VANSAH_RELEASE+"&BUILD="+VANSAH_BUILD+"&ENVIRONMENT="+VANSAH_ENVIRONMENT+"&RESULT="+VANSAH_RESULT+"&COMMENT="+VANSAH_COMMENT+"&AGENT="+VANSAH_AGENT+"&TOKEN="+VANSAH_TOKEN;
			System.out.println(" URI is :"+  URI);
	        HttpURLConnection urlConnection;
	        try {
	            //Connect
	            urlConnection = (HttpURLConnection) ((new URL(URI).openConnection()));
	            urlConnection.setRequestMethod("GET");
	            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
	            int responseCode = urlConnection.getResponseCode();
	            System.out.println(" Response Code is "+ responseCode);
	        
	            BufferedReader in = new BufferedReader(
	                    new InputStreamReader(urlConnection.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	            result = response.toString();
	            System.out.println(" Value of Result is :" +result);

	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }


		return true;
		
	    }
	
	
	public String UpdateResultsInVansah(String clientName, String packageName,String type,String release,String build,String environment, String agent, String token,  String testcaseID, String resultStatus, String testComment)
	{
	    
		
		
		VANSAH_PACKAGE= packageName;
		VANSAH_TYPE=type;
		VANSAH_RELEASE=release;
		VANSAH_BUILD=build;
		VANSAH_ENVIRONMENT=environment;
		VANSAH_AGENT=agent;
		VANSAH_TOKEN=token;
		VANSAH_URI=clientName;
		
		VANSAH_CASE= testcaseID;
		if(resultStatus.equalsIgnoreCase("pass"))
			VANSAH_RESULT ="1";
		if(resultStatus.equalsIgnoreCase("fail"))
			VANSAH_RESULT ="2";
		if(resultStatus.equalsIgnoreCase("N/A"))
			VANSAH_RESULT ="0";
		VANSAH_COMMENT=testComment.replace(" ", "%20");
			
		
			String URI = "https://"+VANSAH_URI+".vansah.net/atsi/save_to_testlog.php?&PACKAGE="+VANSAH_PACKAGE+"&CASE="+VANSAH_CASE+"&TYPE="+VANSAH_TYPE+
					"&RELEASE="+VANSAH_RELEASE+"&BUILD="+VANSAH_BUILD+"&ENVIRONMENT="+VANSAH_ENVIRONMENT+"&RESULT="+VANSAH_RESULT+"&COMMENT="+VANSAH_COMMENT+"&AGENT="+VANSAH_AGENT+"&TOKEN="+VANSAH_TOKEN;
		
	        HttpURLConnection urlConnection;
	        try {
	            //Connect
	            urlConnection = (HttpURLConnection) ((new URL(URI).openConnection()));
	            urlConnection.setRequestMethod("GET");
	            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
	            int responseCode = urlConnection.getResponseCode();
	            BufferedReader in = new BufferedReader(
	                    new InputStreamReader(urlConnection.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	            result = response.toString();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

		return result;
		
	    }

}
