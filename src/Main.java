import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class main{
    public static void main(String[] args) throws Exception{
        String APIKey = System.getenv("OPENWEATHER_API_KEY");
        if (APIKey == null || APIKey.isEmpty()) {
            throw new IllegalStateException("API key environment variable is not defined");
        }

        String uriStr = "https://api.openweathermap.org/data/2.5/weather?units=imperial&lat=39.0437&lon=-77.4874&appid="+APIKey;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(uriStr))
                .header("Accept", "application/json").GET().build();
        String str = "";
        try {
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            str=resp.body();
            System.out.println("Status: "+ resp.statusCode());
            System.out.println("Response: " + resp.body());
            String strNew = reformat(str);
            System.out.println("Formatted Response: \n" + strNew);
            emailsender.sendEmail(strNew);
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
    public static String reformat(String str) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode r = mapper.readTree(str);
        String city = r.get("name").asText();
        double temp = r.get("main").get("temp").asDouble();
        double feels = r.get("main").get("feels_like").asDouble();
        double max = r.get("main").get("temp_max").asDouble();
        String weather = r.get("weather").get(0).get("main").asText();
        String detailedW = r.get("weather").get(0).get("description").asText();
        return "City: " + city + "\n Temperature: " + temp + "\n Feels Like: " + feels + "\n Max Temp: " + max + "\n Weather: " + weather + "\n Detailed weather: " + detailedW;
    }
}