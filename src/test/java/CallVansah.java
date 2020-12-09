
public class CallVansah {


	public static void main(String[] args)
	{
	
		String VANSAH_URI="testpoint";
		String VANSAH_PACKAGE="53";
	    String VANSAH_CASE="261";
		String VANSAH_TYPE="2";
		String VANSAH_RELEASE="4.9";
		String VANSAH_BUILD="4.9-7BD8182";
		String VANSAH_ENVIRONMENT="UAT";
		String VANSAH_RESULT="pass";
		String VANSAH_COMMENT="Testing";
		String VANSAH_AGENT="AGENT2";
		String VANSAH_TOKEN="FLEXATSI15";
		int HTTPS_RESPONSE_CODE;
		String  HTTPS_RESULT;
		
		
		//Type 1 for inoking Vansah instance and updating test case result
		
		VansahInterface vi= new VansahInterface();
		HTTPS_RESULT = vi.UpdateResultsInVansah(VANSAH_URI, VANSAH_PACKAGE, VANSAH_TYPE, VANSAH_RELEASE, VANSAH_BUILD, VANSAH_ENVIRONMENT, VANSAH_AGENT, VANSAH_TOKEN, VANSAH_CASE, VANSAH_RESULT, VANSAH_COMMENT);
		System.out.println("Vansah Reuslt is " + HTTPS_RESULT);
		
		
		//Type 2 for inoking Vansah instance and updating test case result
		
		VansahInterface vic= VansahInterface.getInstance(VANSAH_URI, VANSAH_PACKAGE, VANSAH_TYPE, VANSAH_RELEASE, VANSAH_BUILD, VANSAH_ENVIRONMENT, VANSAH_AGENT, VANSAH_TOKEN);
		HTTPS_RESULT=vic.UpdateResultsInVansah(VANSAH_CASE, VANSAH_RESULT, VANSAH_COMMENT);
		System.out.println("Vansah Reuslt is " + HTTPS_RESULT);
		

	}
	
}
	
	