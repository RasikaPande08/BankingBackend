package com.example.banking.ivr;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;

import java.util.ArrayList;
import java.util.List;

public class FinancialDemo {

	static Voice voice;
	static final int MAX_RETRIES = 3;

	@Autowired
	static HttpCallService httpCallService = new HttpCallService();
	
	static String domain = "https://financial-banking-878612543973.europe-west1.run.app/api";

	public static void main(String[] args) throws Exception {
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		voice = VoiceManager.getInstance().getVoice("kevin16");
		voice.allocate();

		String modelPath = "C:\\Users\\neha1\\vosk-model-small-en-us-0.15";
		//String modelPath = "\\BankingBackend\\model\\vosk-model-small-en-us-0.15";
		Model model = new Model(modelPath);
		AudioFormat format = new AudioFormat(16000, 16, 1, true, false);

		TargetDataLine mic;
		try {
			mic = AudioSystem.getTargetDataLine(format);
			mic.open(format);
			mic.start();
		} catch (LineUnavailableException e) {
			System.err.println("Microphone not available or format not supported: " + e.getMessage());
			return;
		}

		runBase(model, mic);

		mic.stop();
		mic.close();
	}

	private static void runBase(Model model, TargetDataLine mic) throws Exception {
		speak("Welcome to the banking system.");
		speak("Please say your customer ID.");
		String customerId = listenForText(model, mic);
		System.out.println("Heard customer ID: " + customerId);
		verifyCustomer(customerId);
		int i = 3;
		while(i>=2) {
			boolean validateUserVoice = voiceAuthentication(model, mic,i==3);

			if (validateUserVoice) {
				boolean validateUserPwd = passwordAuthentication(model, mic, customerId);
				if (validateUserPwd) {
					runIVR(model, mic, customerId);
				} else {
					speak("password verification failed");
				}
			} else {
				speak("voice verification failed");
			}
			i--;
		}
	}

	private static void runIVR(Model model, TargetDataLine mic, String customerId) throws Exception {
		speak("Say one for balance enquiry.");
		Thread.sleep(600);
		speak("Say two for last five transactions.");
		Thread.sleep(600);
		speak("Say three for fund transfer.");
		Thread.sleep(600);
		speak("Say four to Stop");
		Thread.sleep(600);
		speak("Say five to Restart");

		int retryCount = 0;
		int option = listenForOption(model, mic);

		while (retryCount < MAX_RETRIES && (option < 1 || option > 5)) {
			speak("Invalid option. Please try again.");
			option = listenForOption(model, mic);
			retryCount++;
		}

		if (option < 1 || option > 5) {
			speak("Maximum attempts reached. Please call again later.");
			return;
		}

		switch (option) {
		case 1 -> handleBalanceEnquiry(model, mic, customerId);
		case 2 -> handleLastTransactions(model, mic, customerId);
		case 3 -> handleFundTransfer(model, mic, customerId);
		case 4 -> handleFunctionToStop(model, mic);
		case 5 -> handleToRestart(model, mic, customerId);
		}
	}

	private static void handleBalanceEnquiry(Model model, TargetDataLine mic, String customerId) throws Exception {

		String url = domain+"/voice/" + customerId + "/bal";
		ResponseEntity<String> response = httpCallService.get(url, String.class);
		speak(response.getBody());

		runIVR(model, mic, customerId);

	}

	private static void handleLastTransactions(Model model, TargetDataLine mic, String customerId) throws Exception {
		String url = domain+"/voice/" + customerId + "/lastfive";
		ResponseEntity<List> response = httpCallService.get(url, List.class);

		List<String> strs = response.getBody();
		for (String s : strs) {
			speak(s);
		}
		runIVR(model, mic, customerId);
	}

	private static void handleFundTransfer(Model model, TargetDataLine mic, String customerId) throws Exception {
		speak("Please say amount.");
		String requestedAmount = listenForText(model, mic);
		System.out.println("Heard amount: " + requestedAmount);

		speak("Please say account number.");
		String recipientAccount = listenForText(model, mic);
		System.out.println("Heard recipient account: " + recipientAccount);

		speak("Please say your pin.");
		String pin = listenForText(model, mic);
		if (verifyPin(customerId, pin)) {
			String url = domain+"/voice/transfer/" + customerId + "/" + recipientAccount + "/"
					+ requestedAmount;
			ResponseEntity<String> response = httpCallService.post(url, "", String.class);
			speak(response.getBody());
			runIVR(model, mic, customerId);
		}
		speak("Please enter correct pin");
	}

	private static void speak(String text) {
		System.out.println("[Bot]: " + text);
		voice.speak(text);
	}

	private static int listenForOption(Model model, TargetDataLine mic) throws Exception {
		try (Recognizer recognizer = new Recognizer(model, 16000)) {
			byte[] buffer = new byte[4096];
			long end = System.currentTimeMillis() + 8000;
			while (System.currentTimeMillis() < end) {
				int read = mic.read(buffer, 0, buffer.length);
				if (recognizer.acceptWaveForm(buffer, read)) {
					String result = recognizer.getResult();
					System.out.println("Recognized: " + result);
					if (result.contains("one"))
						return 1;
					if (result.contains("two"))
						return 2;
					if (result.contains("three"))
						return 3;
					if (result.contains("four"))
						return 4;
					if (result.contains("five"))
						return 5;
				}
			}
		}
		return -1;
	}

	private static String listenForText(Model model, TargetDataLine mic) throws Exception {
		try (Recognizer recognizer = new Recognizer(model, 16000)) {
			byte[] buffer = new byte[4096];
			long end = System.currentTimeMillis() + 9000;
			while (System.currentTimeMillis() < end) {
				int bytesRead = mic.read(buffer, 0, buffer.length);
				if (bytesRead > 0) {
					recognizer.acceptWaveForm(buffer, bytesRead);
				}
			}
			String fullResult = recognizer.getFinalResult();
			String extracted = fullResult.replaceAll(".*\"text\"\\s*:\\s*\"(.*?)\".*", "$1");
			System.out.println("Extracted: " + extracted);
			return wordsToInt(extracted);
		}
	}

	private static String wordsToInt(String words) {
		if (words == null || words.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();
		for (String word : words.trim().toLowerCase().split("\\s+")) {
			switch (word) {
			case "zero":
				sb.append('0');
				break;
			case "one":
				sb.append('1');
				break;
			case "two":
				sb.append('2');
				break;
			case "three":
				sb.append('3');
				break;
			case "four":
				sb.append('4');
				break;
			case "five":
				sb.append('5');
				break;
			case "six":
				sb.append('6');
				break;
			case "seven":
				sb.append('7');
				break;
			case "eight":
				sb.append('8');
				break;
			case "nine":
				sb.append('9');
				break;
			default:
				break;
			}
		}

		return sb.toString();
	}

	private static void handleFunctionToStop(Model model, TargetDataLine mic) throws Exception {
		return;
	}

	private static void handleToRestart(Model model, TargetDataLine mic, String customerId) throws Exception {
		runIVR(model, mic, customerId);
	}

	private static boolean voiceAuthentication(Model model, TargetDataLine mic, boolean flag) throws Exception {
		List<String> statementOfUserList = voiceStatement();
		speak("To Authenticate yourself, Please repeat after me");
		List<String> statementList = voiceStatement();
		for (String st : statementList) {
			speak(st);
			statementOfUserList.add(listenForStatement(model, mic));
		}

		/*if (statementOfUserList.equals(statementList)) {
			return true;
		} else {
			return false;
		}*/
		return flag;
	}

	private static boolean passwordAuthentication(Model model, TargetDataLine mic, String customerId) throws Exception {
		speak("Please say your password.");
		String password = listenForText(model, mic);
		String url = domain+"/accountdetails/verify/" + customerId + "/" + password;
		ResponseEntity<String> response = httpCallService.get(url, String.class);
		return true;
	}

	private static boolean verifyPin(String customerId, String pin) throws Exception {
		String url = domain+"/accountdetails/verify/" + customerId + "/" + pin;
		ResponseEntity<String> response = httpCallService.get(url, String.class);
		return true;
	}

	private static void verifyCustomer(String customerId) throws Exception {
		String url = domain+"/accountdetails/verify/" + customerId;
		ResponseEntity<String> response = httpCallService.get(url, String.class);
		System.out.println(response.getBody());
		speak(response.getBody());
	}

	private static String listenForStatement(Model model, TargetDataLine mic) throws Exception {
		try (Recognizer recognizer = new Recognizer(model, 16000)) {
			byte[] buffer = new byte[4096];
			long end = System.currentTimeMillis() + 9000;
			while (System.currentTimeMillis() < end) {
				int bytesRead = mic.read(buffer, 0, buffer.length);
				if (bytesRead > 0) {
					recognizer.acceptWaveForm(buffer, bytesRead);
				}
			}
			String fullResult = recognizer.getFinalResult();
			String extracted = fullResult.replaceAll(".*\"text\"\\s*:\\s*\"(.*?)\".*", "$1");
			System.out.println("Extracted: " + extracted);
			return extracted;
		}
	}

	private static List<String> voiceStatement() {
		List<String> statementList = new ArrayList<>();
		statementList.add("I am user");
		statementList.add("I believe");
		statementList.add("one step at a time");
		statementList.add("I am learning");
		statementList.add("I am here");
		return statementList;
	}

}
