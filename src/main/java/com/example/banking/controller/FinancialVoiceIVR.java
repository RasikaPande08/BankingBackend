package com.example.banking.controller;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.example.banking.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.vosk.Model;
import org.vosk.Recognizer;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.example.banking.controller.AccountDetailsController;

public class FinancialVoiceIVR {

    static Voice voice;
    static final int MAX_RETRIES = 3;
    static AccountDetailsController accountDetailsController = new AccountDetailsController();

    public void start() throws Exception {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();

        Map<String, String> balanceMap = new HashMap<>();
        balanceMap.put("8342", "Your balance is 15,000 rupees");
        balanceMap.put("2277", "Your balance is 8,250 rupees");
        String modelPath = "C:\\Users\\neha1\\vosk-model-small-en-us-0.15";
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

        runIVR(model, mic, balanceMap);

        mic.stop();
        mic.close();
    }

    private static void runIVR(Model model, TargetDataLine mic, Map<String, String> balanceMap) throws Exception {
        speak("Welcome to the banking system.");
        Thread.sleep(1000);
        speak("Say one for balance enquiry.");
        Thread.sleep(1000);
        speak("Say two for last five transactions.");
        Thread.sleep(1000);
        speak("Say three for fund transfer.");

        int retryCount = 0;
        int option = listenForOption(model, mic);

        while (retryCount < MAX_RETRIES && (option < 1 || option > 3)) {
            speak("Invalid option. Please try again.");
            option = listenForOption(model, mic);
            retryCount++;
        }

        if (option < 1 || option > 3) {
            speak("Maximum attempts reached. Please call again later.");
            return;
        }

        switch (option) {
            case 1 -> handleBalanceEnquiry(model, mic);
            case 2 -> handleLastTransactions(model, mic, balanceMap);
            case 3 -> handleFundTransfer(model, mic);
        }
    }

    private static void handleBalanceEnquiry(Model model, TargetDataLine mic, Map<String, String> balanceMap) throws Exception {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            speak("Please say your customer ID.");
            String customerId = listenForText(model, mic);
            System.out.println("Heard customer ID: " + customerId);

            if (balanceMap.containsKey(customerId)) {
                speak("Thank you.");
                speak(balanceMap.get(customerId));
                return;
            } else {
                speak("Customer ID not found. Please try again.");
                retryCount++;
            }
        }
        speak("Maximum attempts reached. Please try again later.");
    }

    private static void handleBalanceEnquiry(Model model, TargetDataLine mic) throws Exception {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            speak("Please say your customer ID.");
            String customerId = listenForText(model, mic);
            System.out.println("Heard customer ID: " + customerId);

            if (!customerId.isBlank()) {
                speak("Thank you.");
                Account account = accountDetailsController.getBal(customerId.toString());
                String balance = account.getBalance();
                speak("Your balance is ₹" + balance);

                return;
            } else {
                speak("Customer ID not found. Please try again.");
                retryCount++;
            }
        }
        speak("Maximum attempts reached. Please try again later.");
    }

    private static void handleLastTransactions(Model model, TargetDataLine mic, Map<String, String> balanceMap) throws Exception {
        int retryCount = 0;
        boolean validCustomer = false;
        while (retryCount < MAX_RETRIES) {
            speak("Please say your customer ID.");
            String customerId = listenForText(model, mic);
            System.out.println("Heard customer ID: " + customerId);

            if (balanceMap.containsKey(customerId)) {
                validCustomer = true;
                break;
            } else {
                speak("Customer ID not found. Please try again.");
                retryCount++;
            }
        }

        if (!validCustomer) {
            speak("Maximum attempts reached. Please try again later.");
            return;
        }

        speak("Please say your pin.");
        String password = listenForText(model, mic);

        boolean isValidPassword = true;

        if (isValidPassword) {
            speak("Your last five transactions are:");
            speak("Five thousand two hundred rupees");
            speak("Three thousand seven hundred rupees");
            speak("Two thousand three hundred rupees");
            speak("Nine hundred rupees");
            speak("One lakh four thousand five hundred rupees");
        } else {
            speak("Customer ID or pin is incorrect.");
        }
    }

    private static void handleFundTransfer(Model model, TargetDataLine mic) throws Exception {
        speak("Please say your customer ID.");
        String customerId = listenForText(model, mic);
        System.out.println("Heard customer ID: " + customerId);

        speak("Please say amount.");
        String requestedAmount = listenForText(model, mic);
        System.out.println("Heard amount: " + requestedAmount);

        speak("Please say account number.");
        String recipientAccount = listenForText(model, mic);
        System.out.println("Heard recipient account: " + recipientAccount);

        speak("Please say your pin.");
        String password = listenForText(model, mic);

        int remainingBalance = 1000; // Example balance
        String clientPassword = "1234"; // Example password
        boolean isValidAccount = true;
        boolean isValidClient = true;

        if (!isValidClient) {
            speak("Client validation failed.");
        } else if (!isValidAccount) {
            speak("Invalid recipient account.");
        } else if (!password.equals(clientPassword)) {
            speak("Authentication failed. Incorrect pin.");
        } else if (remainingBalance <= Integer.parseInt(requestedAmount)) {
            speak("Insufficient balance.");
        } else {
            speak("Fund has been transferred to recipient account.");
        }
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
                    if (result.contains("one")) return 1;
                    if (result.contains("two")) return 2;
                    if (result.contains("three")) return 3;
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
        if (words == null || words.isEmpty()) return "";

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
}
