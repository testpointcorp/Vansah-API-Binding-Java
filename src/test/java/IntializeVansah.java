
public class IntializeVansah {
	

	private String VANSHA_PACKAGE;
	private String VANSHA_CASE;
	private String VANSHA_TYPE;
	private String VANSHA_RELEASE;
	private String VANSHA_BUILD;
	private String VANSHA_ENVIRONMENT;
	private String VANSHA_RESULT;
	private String VANSHA_COMMENT;
	private String VANSHA_AGENT;
	private String VANSHA_TOKEN;
	//Singelton class for intializing Vansha
	
	private static IntializeVansah intializevansha = null;
	
	protected IntializeVansah()
	{
		
	}
	
	 public static IntializeVansah getInstance( ) {
		 
		 if(intializevansha == null) {
			 intializevansha = new IntializeVansah();
	        }
	      return intializevansha;
	   }
	
	public void setPackage(String VANSHA_PACKAGE)
	{
		this.VANSHA_PACKAGE= VANSHA_PACKAGE;
		
	}
	
	public void setCase(String VANSHA_CASE)
	{
		this.VANSHA_CASE= VANSHA_CASE;
		
	}
	
	public void setType(String VANSHA_TYPE)
	{
		this.VANSHA_TYPE= VANSHA_TYPE;
		
	}
	
	public void setRelease(String VANSHA_RELEASE)
	{
		this.VANSHA_RELEASE= VANSHA_RELEASE;
		
	}
	

	public void setBuild(String VANSHA_BUILD)
	{
		this.VANSHA_BUILD= VANSHA_BUILD;
		
	}
	
	public void setEnvironment(String VANSHA_ENVIRONMENT)
	{
		this.VANSHA_ENVIRONMENT= VANSHA_ENVIRONMENT;
		
	}
	
	public void setResult(String VANSHA_RESULT)
	{
		this.VANSHA_RESULT= VANSHA_RESULT;
		
	}
	
	public void setComment(String VANSHA_COMMENT)
	{
		this.VANSHA_COMMENT= VANSHA_COMMENT;
		
	}
	
	public void setAgent(String VANSHA_AGENT)
	{
		this.VANSHA_AGENT= VANSHA_AGENT;
		
	}
	
	public void setToken(String VANSHA_TOKEN)
	{
		this.VANSHA_TOKEN= VANSHA_TOKEN;
		
	}
	
	//********Get Methods
	
	public String getPackage()
	{
		return VANSHA_PACKAGE;
		
	}
	
	public String getCase()
	{
		return VANSHA_CASE;
		
	}
	
	public String getType()
	{
		return VANSHA_TYPE;
		
	}
	
	public String getRelease()
	{
		return VANSHA_RELEASE;
		
	}
	

	public String getBuild()
	{
		return VANSHA_BUILD;
		
	}
	
	public String getEnvironment()
	{
		return VANSHA_ENVIRONMENT;
		
	}
	
	public String getResult()
	{
		return VANSHA_RESULT;
		
	}
	
	public String getComment()
	{
		return VANSHA_COMMENT;
		
	}
	
	public String getAgent()
	{
		return VANSHA_AGENT;
		
	}
	
	public String getToken()
	{
		return VANSHA_TOKEN;
		
	}


}
