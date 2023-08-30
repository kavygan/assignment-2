import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherProgram {

    private static final String API_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) throws IOException {
        JSONObject weatherData = getWeatherData();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String choice;

        while (true) {
            System.out.println("1. Get Temperature");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            choice = reader.readLine();

            if ("0".equals(choice)) {
                System.out.println("Exiting the program.");
                break;
            } else if ("1".equals(choice) || "2".equals(choice) || "3".equals(choice)) {
                System.out.print("Enter the date with time (YYYY-MM-DD HH:MM:SS): ");
                String targetTime = reader.readLine();
                JSONObject entry = findEntryByTime(weatherData, targetTime);

                if (entry != null) {
                    switch (choice) {
                        case "1":
                            double temperature = entry.getJSONObject("main").getDouble("temp");
                            System.out.println("Temperature: " + temperature + "°C");
                            break;
                        case "2":
                            double windSpeed = entry.getJSONObject("wind").getDouble("speed");
                            System.out.println("Wind Speed: " + windSpeed + " m/s");
                            break;
                        case "3":
                            double pressure = entry.getJSONObject("main").getDouble("pressure");
                            System.out.println("Pressure: " + pressure + " hPa");
                            break;
                    }
                } else {
                    System.out.println("Data not available for the specified time.");
                }
            } else {
                System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }

    private static JSONObject getWeatherData() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        return new JSONObject(response.toString());
    }

    private static JSONObject findEntryByTime(JSONObject data, String targetTime) {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject entry = list.getJSONObject(i);
            if (entry.getString("dt_txt").equals(targetTime)) {
                return entry;
            }
        }
        return null;
    }
}
