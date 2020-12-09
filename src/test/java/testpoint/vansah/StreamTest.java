package testpoint.vansah;

import testpoint.Host;
import testpoint.ReadConfig;
import testpoint.VansahLogHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
/*
 * StreamTest Class is designed to perform different function like
 * 1--- 
 */
public class StreamTest {

	private String VANSAH_URI;
	private String VANSAH_PACKAGE;
	private String VANSAH_CASE;
	private String VANSAH_REQUIREMENT;
	private String VANSAH_RELEASE;
	private String VANSAH_BUILD;
	private String VANSAH_ENVIRONMENT;
	private String VANSAH_RESULT;
	private String VANSAH_COMMENT;
	private String VANSAH_AGENT = "";
	private String VANSAH_APPEND;
	private String VANSAH_TOKEN;
	private String VSAM_TOKEN;
	private String VSAM_PROJECTCODE;
	private int HTTPS_RESPONSE_CODE;
	private String HTTPS_RESULT;
	private String GROUP_NAME;
	private static final long LIMIT = 10000000000L;
	private static long last = 0L;
	private static Sigar sigar;
	private static Mem memory;
	private static CpuPerc cpu;
	private Boolean counterIsStopped = Boolean.valueOf(false);
	private HashMap<String, Long> startCounter;
	private HashMap<String, Double> responseT;
	private HashMap<String, Long> serialID;
	private String lastStoppedCounter = "";
	private long startTime;
	private long endTime;
	private Long tempResponse;
	public static Double responseTime;
	private ReadConfig configReader;
	private Host computer;
	VansahLogHandler vlh;
	
		public StreamTest()
		{
				
			vlh = new VansahLogHandler();
			configReader = new ReadConfig();
			computer = new Host();			
			startCounter  = new HashMap<String, Long>();
			responseT = new HashMap<String, Double>();
			serialID = new HashMap<String, Long>();
		}
	/*
	 * Method to record the starting time of transaction through counter
	 * @param counterName provides unique name for counter.
	 * Time is recorded hashmap data structure.
	 */
	
		public void start_synthetic(String counterName) {
			this.startTime = System.nanoTime();
			this.startCounter.put(counterName, Long.valueOf(this.startTime));
			this.serialID.put(counterName, Long.valueOf(get_SERIAL_ID()));
		}

		public void Start_SiteMon_Synthetic(String transactionName, String monitorCode) {
			this.startTime = System.nanoTime();
			this.startCounter.put(transactionName, Long.valueOf(this.startTime));
			this.serialID.put(transactionName, Long.valueOf(get_SERIAL_ID()));
		}
	
		
		public String getCurrentDate() {
	        //07/01/2015 06:24:13
	        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	       // String dateStr = sdf.format(new Date());
	       // return dateStr;
	        Date date = new Date();
	        String cDate = sdf.format(date);
	        return cDate;
	    }
		
		private String getDateAndTime()
		{			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	        String dateStr = sdf.format(new Date());
	      //  System.out.println(dateStr);
	        return dateStr;	
		}
	/*Method to set the Agent Name for VSAM 	
		@param sAgentName which is equivalent as VANSAH_AGENT
	*/
		
		public void setVANSAH_AGENT(String sAgent){
			
			VANSAH_AGENT = sAgent;
		}
	/*
	 * Method to records the total response time for counter
	 * @param counterName holds the unique time stamp
	 *///6355418299
	
		public void stop_synthetic(String counterName)
		{	
			
			sigar = new Sigar();
			try {
				memory = sigar.getMem();
				cpu = sigar.getCpuPerc();
			} catch (SigarException ex) {
				ex.printStackTrace();
			}

			this.VSAM_TOKEN = this.configReader.getVansahToken();
			String USER_PROFILE;
			if (this.startCounter.containsKey(counterName)) {
				this.endTime = System.nanoTime();
				String TP_STOP = getDateAndTime().replace(" ", "%20");
				USER_PROFILE = this.computer.getUserProfile();

				this.tempResponse = Long.valueOf(this.endTime - ((Long) this.startCounter.get(counterName)).longValue());
				responseTime = Double.valueOf(this.tempResponse.longValue() / 1000000000.0D);

				this.responseT.put(counterName, responseTime);
				this.lastStoppedCounter = counterName;

				this.VANSAH_URI = this.configReader.getsVansahInstance();
				String PID = String.valueOf(this.serialID.get(counterName));

				if (this.VANSAH_AGENT.isEmpty()) {
					if (this.configReader.getsAgentName().equals(""))
						this.VANSAH_AGENT = this.computer.getComputerName();
					else {
						this.VANSAH_AGENT = this.configReader.getsAgentName();
					}
				}

				long actualUserMemory = memory.getActualUsed() / 1024L / 1024L;

				double userCpuPerc = cpu.getUser();

				String cpuPerc = String.valueOf(userCpuPerc);

				String memoryUsage = String.valueOf(actualUserMemory);

				String counterName1 = counterName.replace(" ", "%20");
				String URI = "https://" + this.VANSAH_URI + ".vansah.net/atsi/urlsave/save.php?&PID=" + PID + "&RESPONSE="
						+ responseTime + "&AGENT=" + this.VANSAH_AGENT + "&TP_STOP=" + TP_STOP + "&LOGDESC=&COUNTERNAME="
						+ counterName1 + "&TYPE=0&RESULT=0&THRESHOLD=0&USERPROFILE=" + USER_PROFILE + "&VIDEO=&CPU="
						+ cpuPerc + "&MEM=" + memoryUsage + "&PROCESS=0&TOKEN=" + this.VSAM_TOKEN;

				System.out.println(connectToVansah(URI));
			} else {
				Object[] options = {"OK"};

				USER_PROFILE = String.valueOf(JOptionPane.showOptionDialog(null, "Counter Name Mismatch", "Invalid Counter Name", 0, 2,
						null, options, options[0]));
			}			
		}	
		/*
		 * Method to send log to Vansah
		 * @param packageName represent package name in vansah
		 * @param requirement represent requirement in Vansah
		 * @param testcase represent Testcase in Vansah
		 * @param release represent release in vansah
		 * @param build represent build property in vansah
		 * @param environment represents environment for testcase
		 * @param resultStatus represents status of testcase execution
		 * @param comment represents any comment passed during execution 
		 */
		public void Stop_SiteMon_Synthetic(String transactionName, String monitorCode) {
			try {
				memory = sigar.getMem();
				cpu = sigar.getCpuPerc();
			} catch (SigarException ex) {
				ex.printStackTrace();
			}

			this.VSAM_TOKEN = this.configReader.getVansahToken();
			this.VSAM_PROJECTCODE = this.configReader.getVansahToken();
			String PID;
			if (this.startCounter.containsKey(transactionName)) {
				this.endTime = System.nanoTime();
				String TP_STOP = getDateAndTime().replace(" ", "%20");
				String USER_PROFILE = this.computer.getUserProfile();

				this.tempResponse = Long
						.valueOf(this.endTime - ((Long) this.startCounter.get(transactionName)).longValue());
				responseTime = Double.valueOf(this.tempResponse.longValue() / 1000000000.0D);

				this.responseT.put(transactionName, responseTime);
				this.lastStoppedCounter = transactionName;

				this.VANSAH_URI = this.configReader.getsVansahInstance();
				PID = String.valueOf(this.serialID.get(transactionName));

				if (this.VANSAH_AGENT.isEmpty()) {
					if (this.configReader.getsAgentName().equals(""))
						this.VANSAH_AGENT = this.computer.getComputerName();
					else {
						this.VANSAH_AGENT = this.configReader.getsAgentName();
					}
				}

				double totalSysMemory = memory.getTotal() / 1024.0D / 1024.0D;

				System.out.println("Total System memory: " + totalSysMemory);

				double actualUserMemory = memory.getActualUsed() / 1024.0D / 1024.0D;
				System.out.println("Total System memory: " + actualUserMemory);

				double usedMemPercentage = actualUserMemory * 100.0D / totalSysMemory;
				double userCpuPerc = cpu.getUser() * 100.0D;

				Double memoryUsageinGB = Double
						.valueOf(BigDecimal.valueOf(usedMemPercentage).setScale(2, RoundingMode.HALF_UP).doubleValue());

				Double CpuUsageinPerc = Double
						.valueOf(BigDecimal.valueOf(userCpuPerc).setScale(2, RoundingMode.HALF_UP).doubleValue());

				System.out.println("Memory: " + memoryUsageinGB + " % ");

				System.out.println("CPU: " + CpuUsageinPerc + " %");

				String cpuPerc = String.valueOf(CpuUsageinPerc);

				String memoryUsage = String.valueOf(memoryUsageinGB);

				String counterName1 = transactionName.replace(" ", "%20");
				String URI = "https://" + this.VANSAH_URI + ".vansah.net/atsi/urlsave/save.php?&PID=" + PID + "&RESPONSE="
						+ responseTime + "&AGENT=" + this.VANSAH_AGENT + "&TP_STOP=" + TP_STOP + "&LOGDESC=&COUNTERNAME="
						+ counterName1 + "&TYPE=0&RESULT=0&THRESHOLD=0&USERPROFILE=" + USER_PROFILE + "&VIDEO=&CPU="
						+ cpuPerc + "&MEM=" + memoryUsage + "&PROCESS=0&TOKEN=" + this.VSAM_TOKEN;

				System.out.println(connectToVansah(URI));
				URI = "https://sitemonitor.vansah.net/atsi/urlsave/transaction.php?&PID=" + PID + "&RESPONSE="
						+ responseTime + "&AGENT=" + this.VANSAH_AGENT + "&TP_STOP=" + TP_STOP + "&LOGDESC=&COUNTERNAME="
						+ counterName1 + "&TYPE=0&RESULT=0&THRESHOLD=0&USERPROFILE=" + USER_PROFILE + "&VIDEO=&CPU="
						+ cpuPerc + "&MEM=" + memoryUsage + "&PROCESS=" + monitorCode + "&TOKEN=" + this.VSAM_PROJECTCODE;

				System.out.println(connectToVansah(URI));
			} else {
				Object[] options = {"OK"};

				PID = String.valueOf(JOptionPane.showOptionDialog(null, "Counter Name Mismatch", "Invalid Counter Name", 0, 2, null,
						options, options[0]));
			}
		}
		private String getVansahResult(String result)
		{
			if(result.equalsIgnoreCase("pass"))
			return "2";
			if(result.equalsIgnoreCase("fail"))
			return "1";
			if(result.equalsIgnoreCase("N/A"))
			return "0"; 
			return "0";
		}
		
	/**	
		public void sendUpdateLog(String packageName, String testcase, String release, String environment, String resultStatus, String comment)
		{
			
			VANSAH_PACKAGE = packageName.replace(" ", "%20");
			VANSAH_CASE = testcase.replace(" ", "%20");
			VANSAH_RELEASE = release.replace(" ", "%20");
			VANSAH_ENVIRONMENT = environment.replace(" ", "%20");
			VANSAH_RESULT = resultStatus.replace(" ", "%20");
			VANSAH_COMMENT = comment.replace(" ", "%20");			
			VANSAH_URI = configReader.getsVansahInstance();
			if(configReader.getsAgentName().equals(""))
			{
				VANSAH_AGENT = computer.getComputerName();
			}else
			{
				VANSAH_AGENT = configReader.getsAgentName();
			}
			
			VANSAH_TOKEN = configReader.getsVQToken();
		
			VANSAH_RESULT = getVansahResult(resultStatus);
						
			String URI = "https://"+VANSAH_URI+".vansah.net/atsi/add_testlog.php?&PACKAGE="+VANSAH_PACKAGE+"&CASE="+VANSAH_CASE+"&RELEASE="+VANSAH_RELEASE+
					"&ENVIRONMENT="+VANSAH_ENVIRONMENT+"&RESULT="+VANSAH_RESULT+"&COMMENT="+VANSAH_COMMENT+
					"&AGENT="+VANSAH_AGENT+"&APPEND="+"&REQUIREMENT="+"&BUILD="+"&TOKEN="+VANSAH_TOKEN;
			String result = connectToVansah(URI);
			//System.out.println(URI);
			System.out.println("Vansah result: "+ result);
		
		}
	**/	
	//********************Host Alert Function****************************************************************//
	//	https://testpoint.vansah.net/atsi/host_alert.php?&COMMENT=dfg&AGENT=AUT-001&TOKEN=00b837385bb20bc5e2faf0900ca55b63
		
		public String hostAlert(String comment, int iAlertType)
		{
			String IALERT_TYPE = String.valueOf(iAlertType);
			this.VANSAH_COMMENT = comment.replace(" ", "%20");
			this.VANSAH_URI = this.configReader.getsVansahInstance();
			if (this.VANSAH_AGENT.isEmpty()) {
				if (this.configReader.getsAgentName().equals(""))
					this.VANSAH_AGENT = this.computer.getComputerName();
				else {
					this.VANSAH_AGENT = this.configReader.getsAgentName();
				}
			}
			this.VANSAH_TOKEN = this.configReader.getVansahToken();

			String URI = "https://" + this.VANSAH_URI + ".vansah.net/atsi/host_alert.php?&COMMENT=" + this.VANSAH_COMMENT
					+ "&AGENT=" + this.VANSAH_AGENT + "&ALERT=" + IALERT_TYPE + "&TOKEN=" + this.VANSAH_TOKEN;

			return connectToVansah(URI);
		}
		
		//*********************************Host Alert Function Ends********************************************//
		public void sendUpdateLog(String packageName,String requirement, String testcase, String release, String build, String environment, String resultStatus, String comment,String append)
		{	
			
			this.VANSAH_PACKAGE = packageName.replace(" ", "%20");
			this.VANSAH_REQUIREMENT = requirement.replace(" ", "%20");
			this.VANSAH_CASE = testcase.replace(" ", "%20");
			this.VANSAH_RELEASE = release.replace(" ", "%20");
			this.VANSAH_ENVIRONMENT = environment.replace(" ", "%20");
			this.VANSAH_RESULT = resultStatus.replace(" ", "%20");
			this.VANSAH_COMMENT = comment.replace(" ", "%20");
			this.VANSAH_BUILD = build.replace(" ", "%20");
			this.VANSAH_APPEND = append.replace(" ", "%20");
			this.VANSAH_URI = this.configReader.getsVansahInstance();
			if (this.VANSAH_AGENT.isEmpty()) {
				if (this.configReader.getsAgentName().equals("")) {
					String c_name = this.computer.getComputerName();
					this.VANSAH_AGENT = c_name.replace(" ", "%20");
				} else {
					String c_name = this.configReader.getsAgentName();
					this.VANSAH_AGENT = c_name.replace(" ", "%20");
				}
			}
			this.VANSAH_TOKEN = this.configReader.getVansahToken();

			this.VANSAH_RESULT = getVansahResult(resultStatus);

			String URI = "https://" + this.VANSAH_URI + ".vansah.net/atsi/add_testlog.php?&PACKAGE=" + this.VANSAH_PACKAGE
					+ "&CASE=" + this.VANSAH_CASE + "&RELEASE=" + this.VANSAH_RELEASE + "&ENVIRONMENT="
					+ this.VANSAH_ENVIRONMENT + "&RESULT=" + this.VANSAH_RESULT + "&COMMENT=" + this.VANSAH_COMMENT
					+ "&AGENT=" + this.VANSAH_AGENT + "&APPEND=" + this.VANSAH_APPEND + "&REQUIREMENT="
					+ this.VANSAH_REQUIREMENT + "&BUILD=" + this.VANSAH_BUILD + "&TOKEN=" + this.VANSAH_TOKEN;
			String result = connectToVansah(URI);

			System.out.println("Vansah result: " + result);
		}	
		/*
		 * Function to make Connection to Vansah for sending request through GET Method
		 * @param URI is passed while making URL connection
		 */
	private String connectToVansah(String URI)
	{
		 if(configReader.getsUpdateVansah().equals("0"))
		{
			vlh.writeToV_LogFile(URI, HTTPS_RESPONSE_CODE, URI, URI, URI, URI, URI, URI, HTTPS_RESPONSE_CODE, URI);
			HTTPS_RESULT = "#STATUS_OFF";
			return HTTPS_RESULT;
		}
		else
		{
		
		HttpURLConnection urlConnection;
        try {
        	
        	//Detecting if the system using any proxy setting.
        	String hostAddr = configReader.getsHostAddr();
        	String portNo = configReader.getsPortNo();
  			      if (hostAddr.equals("www.host.com") && portNo.equals("0")) {
  			    	  
  			    	 System.out.println("No proxy");
  			    	 urlConnection = (HttpURLConnection) ((new URL(URI).openConnection()));
  			      }
  			      else {
  			    	  System.out.println("Proxy Server");
  			    	  
  			    	Proxy proxy1 =  new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostAddr, Integer.parseInt(portNo)));
  			    	 urlConnection = (HttpURLConnection) ((new URL(URI).openConnection(proxy1)));
  			        
  			      }
        	
        	//Proxy proxy =  new Proxy(Proxy.Type.HTTP, new InetSocketAddress("flxproxy.flexirent.com", 8080));
        	
        	
        	//System.out.println(URI);
         //  urlConnection = (HttpURLConnection) ((new URL(URI).openConnection(proxy)));
           // urlConnection = (HttpURLConnection) ((new URL(URI).openConnection()));
            urlConnection.setRequestMethod("GET");
           
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
            //System.out.println("Result = "+HTTPS_RESULT);
            if(HTTPS_RESULT.equalsIgnoreCase("true"))
            	HTTPS_RESULT = "OK";
            else{
            	HTTPS_RESULT = "#ERROR_02";
            	vlh.writeErrorToV_errorFile(URI, HTTPS_RESPONSE_CODE, inputLine, inputLine, inputLine, inputLine, inputLine, inputLine, HTTPS_RESPONSE_CODE, inputLine);
            }
           
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
        	System.out.println("Server proxy one");
        }catch(UnknownHostException ex)
        {
        	//System.out.println("No Internet Connection");
        	vlh.writeErrorToV_errorFile(URI, HTTPS_RESPONSE_CODE, URI, URI, URI, URI, URI, URI, HTTPS_RESPONSE_CODE, URI);
        	HTTPS_RESULT = "#ERROR_01";
        }
        catch (IOException e) {
           // e.printStackTrace();
        System.out.println("Server under proxy");
        //	vlh.writeErrorToV_errorFile(URI);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
        System.out.println("Response From : Server"+ HTTPS_RESULT);
        return HTTPS_RESULT;
        
		}
	}
		
	/*
	 * Method to send logs in vansah
	 * @param comment comment
	 * @result for PASS or FAIL result
	 * NOTE: The method must be called after stop
	 */
	public String savePrefLog(String comment, String resultStatus )
	{
		this.VANSAH_COMMENT = comment.replace(" ", "%20");
		this.VANSAH_RESULT = resultStatus;
		this.VSAM_TOKEN = this.configReader.getVansahToken();
		this.VANSAH_URI = this.configReader.getsVansahInstance();
		String PID = String.valueOf(this.serialID.get(this.lastStoppedCounter));

		String COUNTER_NAME = this.lastStoppedCounter.replace(" ", "%20");
		if (this.VANSAH_AGENT.isEmpty()) {
			if (this.configReader.getsAgentName().equals(""))
				this.VANSAH_AGENT = this.computer.getComputerName();
			else {
				this.VANSAH_AGENT = this.configReader.getsAgentName();
			}
		}
		this.VANSAH_RESULT = getVansahResult(resultStatus);

		String URI = "https://" + this.VANSAH_URI + ".vansah.net/atsi/urlsave/saveComment.php?&PID=" + PID + "&AGENT="
				+ this.VANSAH_AGENT + "&COMMENT=" + this.VANSAH_COMMENT + "&COUNTERNAME=" + COUNTER_NAME + "&RESULT="
				+ this.VANSAH_RESULT + "&TOKEN=" + this.VSAM_TOKEN;

		String result = connectToVansah(URI);

		this.lastStoppedCounter = "";
		return result;
			
	}	
	/*
	 * Method to get last status update from vansah
	 */
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
	
		private long get_SERIAL_ID()
		{
			long serial_id = System.currentTimeMillis() % LIMIT;
			  if ( serial_id <= last ) {
				  serial_id = (last + 1) % LIMIT;
			  }
			  return last = serial_id;
		}
		
	public void setProperty(String propertyName, String value){
		configReader.initialiseAgent(propertyName, value);
	}

	//Function to get response of counter
	public Double getLastCounterResponse()
	{
		return responseTime;
	}
	
		public static void main(String args[])
		{
			
			
		//System.out.println("	Response "+st.hostAlert("Alert Test From java API Ignore this type 0",0));
		//st.sendUpdateLog("Test case five", "New API Requirement1", "New feature inrtoduce2","5.0","5.0.1","java","0","Testing for new API","0" );
			
			//st.setProperty("sUpdateVansah", "0");
	
		//System.out.println(st.getHttpsStatus());
		
		}
	
}