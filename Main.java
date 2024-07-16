import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;

public class Main {
    public static void main(String[] args) {
        String password = "JavascriptIsBetterThanJava!";
        Scanner playerInput = new Scanner(System.in);
        System.out.println("Please enter the password to obtain access:");

        if (playerInput.nextLine().equals(password)) {
            Random generator = new Random();
            int[] waytosay = new int[3];
            for (int i = 0; i < waytosay.length; i++) {
                waytosay[i] = generator.nextInt(2);  // Randomly choose between two ways of asking for input
            }

            String[] prompts = new String[]{"noun (a person, place, or thing)", "verb (an action)", "place (a location)"};
            String[] responses = new String[3];

            for (int i = 0; i < waytosay.length; i++) {
                switch (waytosay[i]) {
                    case 0 -> System.out.println("Give me a " + prompts[i] + ": ");
                    case 1 -> System.out.println("I'm gonna need a " + prompts[i] + ": ");
                    default -> System.out.println("Could you please provide me with a " + prompts[i] + ": ");
                }
                responses[i] = playerInput.nextLine();
            }

            String story = "Once there was a " + responses[0] + " that loved to " + responses[1] + " at the " + responses[2] + ".";
            String correctedStory = correctGrammar(story);
            System.out.println(correctedStory);
        } else {
            System.out.println("Incorrect password.");
        }
    }

    private static String correctGrammar(String text) {
        try {
            String apiKey = "Ez3pszQ6PftONKGv";  // Replace with your actual API key
            String encodedText = URLEncoder.encode(text, "UTF-8");
            URL url = new URL("https://api.textgears.com/correct?text=" + encodedText + "&key=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Extract the corrected text from the JSON response
                String jsonResponse = response.toString();
                jsonResponse = jsonResponse.substring(jsonResponse.indexOf("\"corrected\":") + 13);
                jsonResponse = jsonResponse.substring(0, jsonResponse.indexOf("\"") - 1);
                return jsonResponse;
            } else {
                return "HTTP request failed. Response Code: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to correct grammar.";
        }
    }
}
