package testpoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class VansahLogHandler {

	public void writeErrorToV_errorFile(String CASE_KEY, int STEP_ORDER, int RESULT_KEY, String COMMENT, String RELEASE_KEY,String BUILD_KEY, String ENVIRONMENT_KEY, 
		int DATA_ROW_NUM, String DATA_COLUMN_NAME) {
		final Logger logger = LoggerFactory.getLogger("error");
		MDC.put("TestCase", CASE_KEY);
		MDC.put("StepID", String.valueOf(STEP_ORDER));
		MDC.put("Result", String.valueOf(RESULT_KEY));
		MDC.put("Release", RELEASE_KEY);
		MDC.put("Build", BUILD_KEY);
		MDC.put("Environment", ENVIRONMENT_KEY);
		MDC.put("RowID", String.valueOf(DATA_ROW_NUM));
		MDC.put("ColumnName", DATA_COLUMN_NAME);
		logger.error(COMMENT);
	}

	/*
	 * Function to writer error to V_error.txt
	 */

	public void writeToV_LogFile(String VANSAH_CASE, int VANSAH_STEPID, int RESULT_KEY, String vANSAH_COMMENT,String vANSAH_RELEASE, String vANSAH_BUILD, 
		String vANSAH_ENVIRONMENT, int DATA_ROW_NUM, String DATA_COLUMN_NAME) {
		final Logger logger = LoggerFactory.getLogger("log");
		MDC.put("TestCase", VANSAH_CASE);
		MDC.put("StepID", String.valueOf(VANSAH_STEPID));
		MDC.put("Result", String.valueOf(RESULT_KEY));
		MDC.put("Release", vANSAH_RELEASE);
		MDC.put("Build", vANSAH_BUILD);
		MDC.put("Environment", vANSAH_ENVIRONMENT);
		MDC.put("RowID", String.valueOf(DATA_ROW_NUM));
		MDC.put("ColumnName", DATA_COLUMN_NAME);
		logger.debug(vANSAH_COMMENT);
	}

	public void removeLineFromFile(String file, String lineToRemove) {
		try {
			File vansahLog = new File(file);
			if (!vansahLog.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename
			File tempFile = new File(vansahLog.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;
			while ((line = br.readLine()) != null) {
				if (!line.trim().equals(lineToRemove)) {
					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			// Delete the original file

			if (!vansahLog.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(vansahLog)) {
				System.out.println("Could not rename file");
			}

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


}
