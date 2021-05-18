package testpoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigSetup {

	File theDir;
	Properties VNSProperties;
	InputStream reader;
	FileOutputStream output;
	String file = "Vansah\\config.vns";

	public ConfigSetup() {
		setUp();
	}
	// Function which creates Vansah Folder in the Project root directory.
	// Then it created config.vns (Vansah configuration file") if it does not
	// exist.

	private void setUp() {
		BufferedReader br = null;
		theDir = new File(file);
		// Check if the directory and file exists
		if (!theDir.exists()) {
			theDir.getParentFile().mkdirs();
			try {
				FileWriter writer = new FileWriter(theDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writeToConfig();

		} else {

			try {
				// Initialize the buffer reader to read the file.
				br = new BufferedReader(new FileReader(file));
				// Check if the file is empty. if yes then write property to
				// file.
				if (br.readLine() == null) {
					writeToConfig();
				}
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Method to write to configuration file once created. It does not change if
	 * the file is already present if folder structure Vansah/config.vns
	 */

	private void writeToConfig() {
		this.VNSProperties = new Properties();
		try {
			this.reader = new FileInputStream(this.file);
			this.VNSProperties.load(this.reader);
			this.VNSProperties.setProperty("sDevMode", "1");
			this.VNSProperties.setProperty("sVansahConnectionType", "1");
			this.VNSProperties.setProperty("sVSAMToken", "FLEXG2015");
			this.VNSProperties.setProperty("sVQToken", "d87c7570abf1e66d74bfc5a5c7a4a66b");
			this.VNSProperties.setProperty("sVSAMProjectCode", "");
			this.VNSProperties.setProperty("sVansahInstance", "flexigroup");
			this.VNSProperties.setProperty("sMaxLogResponse", "120");
			this.VNSProperties.setProperty("sUpdateVansah", "0");
			this.VNSProperties.setProperty("sAgentName", "");
			this.VNSProperties.setProperty("sRegScreenShotsDirectory", "");
			this.VNSProperties.setProperty("sHostAddr", "www.host.com");
			this.VNSProperties.setProperty("sPortNo", "0");
		} catch (IOException localIOException1) {
		} finally {
			if (this.reader != null) {
				try {
					this.reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		try {
			this.output = new FileOutputStream(this.theDir);
			this.VNSProperties.store(this.output, "[Settings]");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (this.output != null)
				try {
					this.output.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		}
	}

	public static void main(String args[]) {
		ConfigSetup con = new ConfigSetup();
		con.setUp();
	}

}
