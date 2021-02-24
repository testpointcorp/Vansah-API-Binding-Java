

public class Constants {
  

  public static final String PATH_ATF_TESTDATA = "sys_atf_parameter_set.xlsx";
  public static final String Catalogue_Contractor_File = "sample.txt";

  public static final String BROWSER = "chrome";
  public static final String BUILD = System.getProperty("Build");
  //public static final String BUILD = "99";
  public static final String RELEASE = "BINDER";
  public static final String ENVIRONMENT = "TEST";
  public static final String headless = "No";
  public static final String emailList = System.getProperty("Report");
  public static final String CYCLE = "93";
  
  
  /*
  public static final String BROWSER = System.getProperty("Browser");
  public static final String BUILD = System.getProperty("Build");
  public static final String RELEASE = "1.1";
  public static final String ENVIRONMENT = System.getProperty("Environment");
  public static final String emailList = System.getProperty("Report");
  public static final int CYCLE = 19;
  */
  
  
   
  public static final boolean passScreenShot = true;
  public static final boolean failScreenShot = true;
  public static final boolean notApplicableScreenShot = true;
  
  //Wait Timers
  public static int LongWait = 10;
  public static int veryLongWait = 120;
  public static int ShortWait = 5;
  public static int VeryShortWait = 2;
  

}