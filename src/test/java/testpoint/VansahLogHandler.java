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

	public void writeErrorToV_errorFile(String vANSAH_CASE, int vANSAH_STEPID, String vANSAH_RESULT,
			String vANSAH_COMMENT, String vANSAH_RELEASE, String vANSAH_BUILD, String vANSAH_ENVIRONMENT,
			String vANSAH_AGENT, int DATA_ROW_NUM, String DATA_COLUMN_NAME) {
		final Logger logger = LoggerFactory.getLogger("error");
		MDC.put("TestCase", vANSAH_CASE);
		MDC.put("StepID", String.valueOf(vANSAH_STEPID));
		MDC.put("Result", vANSAH_RESULT);
		MDC.put("Release", vANSAH_RELEASE);
		MDC.put("Build", vANSAH_BUILD);
		MDC.put("Environment", vANSAH_ENVIRONMENT);
		MDC.put("Agent", vANSAH_AGENT);
		MDC.put("RowID", String.valueOf(DATA_ROW_NUM));
		MDC.put("ColumnName", DATA_COLUMN_NAME);
		logger.error(vANSAH_COMMENT);
	}

	/*
	 * Function to writer error to V_error.txt
	 * 
	 * @param URI address
	 */

	public void writeToV_LogFile(String vANSAH_CASE, int vANSAH_STEPID, String vANSAH_RESULT, String vANSAH_COMMENT,
			String vANSAH_RELEASE, String vANSAH_BUILD, String vANSAH_ENVIRONMENT, String vANSAH_AGENT, int DATA_ROW_NUM, String DATA_COLUMN_NAME) {
		final Logger logger = LoggerFactory.getLogger("log");
		MDC.put("TestCase", vANSAH_CASE);
		MDC.put("StepID", String.valueOf(vANSAH_STEPID));
		MDC.put("Result", vANSAH_RESULT);
		MDC.put("Release", vANSAH_RELEASE);
		MDC.put("Build", vANSAH_BUILD);
		MDC.put("Environment", vANSAH_ENVIRONMENT);
		MDC.put("Agent", vANSAH_AGENT);
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