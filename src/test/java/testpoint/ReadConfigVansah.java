package testpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ReadConfigVansah {

	private String sMaxLogResponse;
	private String sVansahConnectionType;
	private String sRegScreenShotsDirectory;
	private String sVansahInstance;
	private String sUpdateVansah;
	private String sDevMode;
	private String sAgentName;
	private String sHostAddr;
	private String sPortNo;
	private String sCurrentSysDate;
	private String CONFIG = "Vansah\\config.vns";
	Properties VNSProperties;
	ConfigSetup configuration;
	InputStream reader = null;
	OutputStream writer = null;
	private String sProjectIdentifier;
	private String sUserToken;
	private String sVansahToken;

	public ReadConfigVansah() {
		this.VNSProperties = new Properties();
		try {
			this.configuration = new ConfigSetup();
			this.reader = new FileInputStream(this.CONFIG);
			this.VNSProperties.load(this.reader);
			this.sMaxLogResponse = this.VNSProperties.getProperty("sMaxLogResponse");
			this.sVansahConnectionType = this.VNSProperties.getProperty("sVansahConnectionType");
			this.sRegScreenShotsDirectory = this.VNSProperties.getProperty("sRegScreenShotsDirectory");
			this.sUpdateVansah = this.VNSProperties.getProperty("sUpdateVansah");
			this.sDevMode = this.VNSProperties.getProperty("sDevMode");
			this.sAgentName = this.VNSProperties.getProperty("sAgentName");
			this.sUserToken = this.VNSProperties.getProperty("sUserToken");
			this.sVansahInstance = this.VNSProperties.getProperty("sVansahInstance");
			this.sProjectIdentifier = this.VNSProperties.getProperty("sProjectIdentifier");
			this.sHostAddr = this.VNSProperties.getProperty("sHostAddr");
			this.sPortNo = this.VNSProperties.getProperty("sPortNo");
			this.sCurrentSysDate = this.VNSProperties.getProperty("sCurrentSysDate");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (this.reader != null)
				try {
					this.reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		}
	}

	public void initializeCurrentDate() {
	}

	public void initialiseAgent(String propertyName, String value) {
		try {
			this.VNSProperties.setProperty(propertyName, value);
			File f = new File(this.CONFIG);
			this.writer = new FileOutputStream(f);
			this.VNSProperties.store(this.writer, "[Settings]");
			this.writer.close();
			this.reader = new FileInputStream(this.CONFIG);
			this.VNSProperties.load(this.reader);
			this.reader.close();
			this.sVansahInstance = this.VNSProperties.getProperty("sVansahInstance");
			this.sMaxLogResponse = this.VNSProperties.getProperty("sMaxLogResponse");
			this.sVansahConnectionType = this.VNSProperties.getProperty("sVansahConnectionType");
			this.sUserToken = this.VNSProperties.getProperty("sUserToken");
			this.sProjectIdentifier = this.VNSProperties.getProperty("sProjectIdentifier");
			this.sRegScreenShotsDirectory = this.VNSProperties.getProperty("sRegScreenShotsDirectory");
			this.sUpdateVansah = this.VNSProperties.getProperty("sUpdateVansah");
			this.sDevMode = this.VNSProperties.getProperty("sDevMode");
			this.sAgentName = this.VNSProperties.getProperty("sAgentName");
			this.sHostAddr = this.VNSProperties.getProperty("sHostAddr");
			this.sPortNo = this.VNSProperties.getProperty("sPortNo");
			this.sCurrentSysDate = this.VNSProperties.getProperty("sCurrentSysDate");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (this.writer != null)
				try {
					this.writer.close();
				} catch (IOException ex) {
					System.out.println("Unable to close Writer!!!");
				}
		}
	}

	public String getsAgentName() {
		return this.sAgentName;
	}

	public void setsAgentName(String sAgentName) {
		this.sAgentName = sAgentName;
	}

	public String getUserToken() {
		return this.sUserToken;
	}

	public String getProjectIdentifier() {
		return this.sProjectIdentifier;
	}
	
	public String getsVansahInstance() {
		return this.sVansahInstance;
	}
	

	public String getsUpdateVansah() {
		return this.sUpdateVansah;
	}

	public String getsRegScreenShotsDirectory() {
		return this.sRegScreenShotsDirectory;
	}

	public String getsMaxLogResponse() {
		return this.sMaxLogResponse;
	}

	public String getsVansahConnectionType() {
		return this.sVansahConnectionType;
	}

	public String getsDevMode() {
		return this.sDevMode;
	}

	public String getsHostAddr() {
		return this.sHostAddr;
	}

	public String getsPortNo() {
		return this.sPortNo;
	}

	public String getCurrentSysDate() {
		return this.sCurrentSysDate;
	}	
}
