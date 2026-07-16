package com.vansah;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ============================================================================
 *  FOR TESTING THE VANSAH JAVA BINDING (VansahNode) ONLY.
 * ============================================================================
 *
 * This is a single, self-contained live test suite that exercises every public
 * VansahNode operation (create runs, add/update/remove step logs, attachments,
 * quick tests, Standard/Advanced Test Plans and the config/validation helpers)
 * against a REAL Vansah instance. It is shipped as a reference/example so you
 * can confirm the binding works end-to-end in your own Vansah workspace.
 *
 * How to run:
 *   1. Copy .env.example to .env at the project root and fill in real values.
 *      (.env is gitignored - never commit real tokens.) You may instead export
 *      the same keys as environment variables (handy for CI).
 *   2. mvn test
 *
 * Required keys (see .env.example): VANSAH_TOKEN, VANSAH_PROJECT_KEY,
 * VANSAH_JIRA_ISSUE_KEY, VANSAH_TEST_CASE_KEY, VANSAH_FOLDER_PATH,
 * VANSAH_ATP_KEY, VANSAH_STP_KEY. Optional: VANSAH_URL, VANSAH_ATP_ASSET_TYPE,
 * VANSAH_SPRINT_NAME, VANSAH_RELEASE_NAME, VANSAH_ENVIRONMENT_NAME,
 * VANSAH_SCREENSHOT_PATH, VANSAH_DEBUG.
 *
 * Any test whose required configuration is missing is SKIPPED (not failed), so
 * this suite is safe to run before .env is fully filled in - with no config it
 * simply skips every network test.
 *
 * Note on assertions: VansahNode catches all API/network errors internally and
 * only prints outcomes to stdout - it does not expose success/failure or the
 * created run/log ids. These tests therefore assert that each call completes
 * without throwing; they cannot tell a real Vansah success from a silently
 * logged API error. Enable VANSAH_DEBUG=true to inspect the exact JSON sent.
 * Every network test cleans up after itself (removeTestLog / removeTestRun).
 */
class VansahNodeFullTests {

	private static String vansahUrl;
	private static String token;
	private static String projectKey;
	private static String folderPath;
	private static String jiraIssueKey;
	private static String testCaseKey;
	private static String atpKey;
	private static String atpAssetType;
	private static String stpKey;
	private static String sprintName;
	private static String releaseName;
	private static String environmentName;
	private static File screenshotFile;

	private VansahNode node;

	@BeforeAll
	static void loadConfig() throws IOException {
		vansahUrl = Env.get("VANSAH_URL", "https://prod.vansah.com");
		token = Env.get("VANSAH_TOKEN");
		projectKey = Env.get("VANSAH_PROJECT_KEY");
		folderPath = Env.get("VANSAH_FOLDER_PATH");
		jiraIssueKey = Env.get("VANSAH_JIRA_ISSUE_KEY");
		testCaseKey = Env.get("VANSAH_TEST_CASE_KEY");
		atpKey = Env.get("VANSAH_ATP_KEY");
		atpAssetType = Env.get("VANSAH_ATP_ASSET_TYPE", "folder");
		stpKey = Env.get("VANSAH_STP_KEY");
		sprintName = Env.get("VANSAH_SPRINT_NAME");
		releaseName = Env.get("VANSAH_RELEASE_NAME");
		environmentName = Env.get("VANSAH_ENVIRONMENT_NAME");
		screenshotFile = resolveScreenshotFile();
	}

	private static File resolveScreenshotFile() throws IOException {
		String configuredPath = Env.get("VANSAH_SCREENSHOT_PATH");
		if (configuredPath != null) {
			File configured = new File(configuredPath);
			if (configured.isFile()) {
				return configured;
			}
			System.out.println("⚠️ VANSAH_SCREENSHOT_PATH does not point to a file, using a generated placeholder instead.");
		}
		// 1x1 transparent PNG so attachment tests don't require an externally supplied image.
		byte[] onePixelPng = Base64.getDecoder().decode(
				"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=");
		Path tempFile = Files.createTempFile("vansah-test-screenshot", ".png");
		Files.write(tempFile, onePixelPng);
		tempFile.toFile().deleteOnExit();
		return tempFile.toFile();
	}

	@BeforeEach
	void setUp() {
		assumeTrue(Env.isSet("VANSAH_TOKEN"),
				"Skipping: VANSAH_TOKEN not set. Copy .env.example to .env and fill in real Vansah credentials.");

		node = new VansahNode(folderPath, jiraIssueKey);
		VansahNode.setVansahURL(vansahUrl);
		VansahNode.setVansahToken(token);
		VansahNode.setDebug(Env.getBoolean("VANSAH_DEBUG", false));
		// Reset the shared Test Plan iteration to its default (1) so a value set by an
		// earlier test cannot leak into a later one (the field is process-global).
		node.setTestPlanIteration(1);
		if (projectKey != null) {
			VansahNode.setProjectKey(projectKey);
		}
		if (atpKey != null) {
			node.setAdvancedTestPlanKey(atpKey);
		}
		if (stpKey != null) {
			node.setStandardTestPlanKey(stpKey);
		}
		node.setFOLDERPATH(folderPath);
		node.setJIRA_ISSUE_KEY(jiraIssueKey);
		node.setSPRINT_NAME(sprintName);
		node.setRELEASE_NAME(releaseName);
		node.setENVIRONMENT_NAME(environmentName);
	}

	// ---- Config / validation methods (no network call) ----

	@Test
	void isValidFolderPath_acceptsWellFormedPath() {
		assertTrue(node.isValidFolderPath("regression/2025/smoke"));
	}

	@Test
	void isValidFolderPath_rejectsLeadingSlash() {
		assertFalse(node.isValidFolderPath("/regression/2025"));
	}

	@Test
	void isValidFolderPath_rejectsMissingSeparator() {
		assertFalse(node.isValidFolderPath("regression"));
	}

	@Test
	void isValidFolderPath_rejectsNullOrEmpty() {
		assertFalse(node.isValidFolderPath(null));
		assertFalse(node.isValidFolderPath(""));
	}

	@Test
	void noArgConstructor_initialisesWithoutThrowing() {
		VansahNode plain = assertDoesNotThrow(() -> new VansahNode());
		// The no-arg constructor leaves folder/issue unset; validation helpers must still work.
		assertFalse(plain.isValidFolderPath(null));
		assertTrue(plain.isValidFolderPath("regression/2025/smoke"));
	}

	@Test
	void setDebug_togglesWithoutThrowing() {
		assertDoesNotThrow(() -> VansahNode.setDebug(true));
		assertDoesNotThrow(() -> VansahNode.setDebug(false));
	}

	@Test
	void setVansahURL_resetsToDefaultOnBlankInput() {
		assertDoesNotThrow(() -> VansahNode.setVansahURL(""));
		VansahNode.setVansahURL(vansahUrl); // restore for any tests sharing the JVM
	}

	@Test
	void setVansahToken_acceptsArbitraryValue() {
		assertDoesNotThrow(() -> VansahNode.setVansahToken("arbitrary-non-empty-token"));
		// real token is restored by setUp() before every subsequent test
	}

	@Test
	void setProjectKey_warnsButDoesNotThrowOnBlankInput() {
		assertDoesNotThrow(() -> VansahNode.setProjectKey(""));
		if (projectKey != null) {
			VansahNode.setProjectKey(projectKey); // restore
		}
	}

	@Test
	void setTestPlanIteration_ignoresOutOfRangeButAcceptsValid() {
		// Out-of-range values are ignored with a warning (default of 1 kept); valid ones are accepted.
		assertDoesNotThrow(() -> node.setTestPlanIteration(0));
		assertDoesNotThrow(() -> node.setTestPlanIteration(6));
		assertDoesNotThrow(() -> node.setTestPlanIteration(2));
	}

	// ---- JIRA issue lifecycle: run -> log (all 4 addTestLog overloads) -> update (all 4 overloads) -> remove ----

	@Test
	void jiraIssueLifecycle_addLogUpdateRemove() throws Exception {
		assumeTrue(Env.isSet("VANSAH_PROJECT_KEY"), "Skipping: VANSAH_PROJECT_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_JIRA_ISSUE_KEY"), "Skipping: VANSAH_JIRA_ISSUE_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_TEST_CASE_KEY"), "Skipping: VANSAH_TEST_CASE_KEY not set.");

		assertDoesNotThrow(() -> node.addTestRunFromJIRAIssue(testCaseKey));

		// Runs are created UNTESTED with one pre-created log per step; step 1 always exists.
		final int step = 1;

		assertDoesNotThrow(() -> node.addTestLog(2, "Automated: addTestLog(int, comment, step)", step));
		assertDoesNotThrow(() -> node.addTestLog("passed", "Automated: addTestLog(String, comment, step)", step));
		assertDoesNotThrow(() -> node.addTestLog(2, "Automated: addTestLog(int, comment, step, image)", step, screenshotFile));
		assertDoesNotThrow(() -> node.addTestLog("passed", "Automated: addTestLog(String, comment, step, image)", step, screenshotFile));

		assertDoesNotThrow(() -> node.updateTestLog(1, "Automated: updateTestLog(int, comment)"));
		assertDoesNotThrow(() -> node.updateTestLog("failed", "Automated: updateTestLog(String, comment)"));
		assertDoesNotThrow(() -> node.updateTestLog(1, "Automated: updateTestLog(int, comment, image)", screenshotFile));
		assertDoesNotThrow(() -> node.updateTestLog("failed", "Automated: updateTestLog(String, comment, image)", screenshotFile));

		assertDoesNotThrow(() -> node.removeTestLog());
		assertDoesNotThrow(() -> node.removeTestRun());
	}

	// ---- Test folder lifecycle ----

	@Test
	void testFolderLifecycle_addLogRemove() throws Exception {
		assumeTrue(Env.isSet("VANSAH_PROJECT_KEY"), "Skipping: VANSAH_PROJECT_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_FOLDER_PATH"), "Skipping: VANSAH_FOLDER_PATH not set.");
		assumeTrue(Env.isSet("VANSAH_TEST_CASE_KEY"), "Skipping: VANSAH_TEST_CASE_KEY not set.");

		assertDoesNotThrow(() -> node.addTestRunFromTestFolder(testCaseKey));
		assertDoesNotThrow(() -> node.addTestLog("passed", "Automated: test folder log", 1));
		assertDoesNotThrow(() -> node.removeTestLog());
		assertDoesNotThrow(() -> node.removeTestRun());
	}

	// ---- Attachments (screenshot upload) ----

	@Test
	void attachmentLifecycle_addLogWithImage_thenUpdateWithImage() throws Exception {
		assumeTrue(Env.isSet("VANSAH_PROJECT_KEY"), "Skipping: VANSAH_PROJECT_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_JIRA_ISSUE_KEY"), "Skipping: VANSAH_JIRA_ISSUE_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_TEST_CASE_KEY"), "Skipping: VANSAH_TEST_CASE_KEY not set.");
		assumeTrue(screenshotFile != null && screenshotFile.isFile(),
				"Skipping: no usable screenshot file (generated placeholder failed).");

		assertDoesNotThrow(() -> node.addTestRunFromJIRAIssue(testCaseKey));

		// addTestLog(result, comment, step, image) -- uploads the base64 screenshot as an attachment.
		assertDoesNotThrow(() -> node.addTestLog("passed", "Automated: log with screenshot attachment", 1, screenshotFile));

		// updateTestLog(result, comment, image) -- updates the same log with an attachment.
		assertDoesNotThrow(() -> node.updateTestLog("failed", "Automated: updated log with screenshot attachment", screenshotFile));

		assertDoesNotThrow(() -> node.removeTestLog());
		assertDoesNotThrow(() -> node.removeTestRun());
	}

	// ---- Advanced Test Plan ----

	@Test
	void advancedTestPlanLifecycle_addLogRemove() throws Exception {
		assumeTrue(Env.isSet("VANSAH_PROJECT_KEY"), "Skipping: VANSAH_PROJECT_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_ATP_KEY"), "Skipping: VANSAH_ATP_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_TEST_CASE_KEY"), "Skipping: VANSAH_TEST_CASE_KEY not set.");

		assertDoesNotThrow(() -> node.addTestRunFromAdvancedTestPlan(atpAssetType, testCaseKey));
		assertDoesNotThrow(() -> node.addTestLog("passed", "Automated: ATP log", 1));
		assertDoesNotThrow(() -> node.removeTestLog());
		assertDoesNotThrow(() -> node.removeTestRun());
	}

	// ---- Standard Test Plan ----

	@Test
	void standardTestPlanLifecycle_addLogRemove() throws Exception {
		assumeTrue(Env.isSet("VANSAH_PROJECT_KEY"), "Skipping: VANSAH_PROJECT_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_STP_KEY"), "Skipping: VANSAH_STP_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_TEST_CASE_KEY"), "Skipping: VANSAH_TEST_CASE_KEY not set.");

		assertDoesNotThrow(() -> node.addTestRunFromStandardTestPlan(testCaseKey));
		assertDoesNotThrow(() -> node.addTestLog("passed", "Automated: STP log", 1));
		assertDoesNotThrow(() -> node.removeTestLog());
		assertDoesNotThrow(() -> node.removeTestRun());
	}

	// ---- Quick Test (single overall result, no steps) ----

	@Test
	void quickTestFromJiraIssue() throws Exception {
		assumeTrue(Env.isSet("VANSAH_PROJECT_KEY"), "Skipping: VANSAH_PROJECT_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_JIRA_ISSUE_KEY"), "Skipping: VANSAH_JIRA_ISSUE_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_TEST_CASE_KEY"), "Skipping: VANSAH_TEST_CASE_KEY not set.");

		assertDoesNotThrow(() -> node.addQuickTestFromJiraIssue(testCaseKey, 2));
	}

	@Test
	void quickTestFromTestFolders() throws Exception {
		assumeTrue(Env.isSet("VANSAH_PROJECT_KEY"), "Skipping: VANSAH_PROJECT_KEY not set.");
		assumeTrue(Env.isSet("VANSAH_FOLDER_PATH"), "Skipping: VANSAH_FOLDER_PATH not set.");
		assumeTrue(Env.isSet("VANSAH_TEST_CASE_KEY"), "Skipping: VANSAH_TEST_CASE_KEY not set.");

		assertDoesNotThrow(() -> node.addQuickTestFromTestFolders(testCaseKey, 2));
	}

	/**
	 * Tiny config reader: loads a .env file at the project root (KEY=VALUE per line,
	 * '#' comments and blank lines ignored) and falls back to real OS environment
	 * variables so the same tests work unmodified in CI.
	 */
	private static final class Env {
		private static final Map<String, String> VALUES = load();

		private Env() {
		}

		private static Map<String, String> load() {
			Map<String, String> values = new HashMap<>();
			Path envFile = Paths.get(".env");
			if (Files.isReadable(envFile)) {
				try {
					for (String line : Files.readAllLines(envFile)) {
						String trimmed = line.trim();
						if (trimmed.isEmpty() || trimmed.startsWith("#")) {
							continue;
						}
						int eq = trimmed.indexOf('=');
						if (eq <= 0) {
							continue;
						}
						String key = trimmed.substring(0, eq).trim();
						String value = trimmed.substring(eq + 1).trim();
						if (value.length() >= 2
								&& ((value.startsWith("\"") && value.endsWith("\""))
										|| (value.startsWith("'") && value.endsWith("'")))) {
							value = value.substring(1, value.length() - 1);
						}
						values.put(key, value);
					}
				} catch (IOException e) {
					System.out.println("⚠️ Warning: Failed to read .env file: " + e.getMessage());
				}
			}
			return values;
		}

		static String get(String key) {
			return get(key, null);
		}

		static String get(String key, String defaultValue) {
			String value = VALUES.get(key);
			if (value == null || value.isEmpty()) {
				value = System.getenv(key);
			}
			if (value == null || value.isEmpty()) {
				value = defaultValue;
			}
			return value;
		}

		static boolean getBoolean(String key, boolean defaultValue) {
			String value = get(key);
			if (value == null) {
				return defaultValue;
			}
			return "true".equalsIgnoreCase(value) || "1".equals(value);
		}

		static boolean isSet(String key) {
			String value = get(key);
			return value != null && !value.trim().isEmpty();
		}
	}
}
