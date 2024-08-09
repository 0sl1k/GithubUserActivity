import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Please enter a username in GitHub");

        }

        String username = args[0];
        String urlString = "https://api.github.com/users/" + username + "/events";

        try {
            URL url  = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if(responseCode == 404){
                System.out.println("User not found");
                return;
            }else if(responseCode != 200){
                System.out.println("Connection on GitHub API failed");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while((inputLine = reader.readLine())!=null){
                content.append(inputLine);
            }
            reader.close();

            parseAndDisplayActivity(content.toString());


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }



    }

    private static void parseAndDisplayActivity(String jsonResponse){
        String[] events = jsonResponse.split("\\},\\{");
        for(String event : events){
            if(event.contains("\"type\":\"PushEvent\"")){
                System.out.println("push event");
            }else if(event.contains("\"type\":\"CreateEvent\"")){
                System.out.println("create event:");
            }
        }

    }
}