package com.enessimsek.earthquakeproject.controller;

import com.enessimsek.earthquakeproject.entity.Earthquake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/earth")
public class EarthquakeController {

    @GetMapping("/{count}/{country}")
    public List<Earthquake> getAllData(@PathVariable Long count, @PathVariable("country") String country) throws MalformedURLException {
        LocalDate date = LocalDate.now().minusDays(count); //startime
        LocalDate dateNow = LocalDate.now(); //endtime
        String urlMain = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" +
                date.toString() +
                "&endtime=" +
                dateNow.toString();

        URL url = new URL(urlMain);  // can throw MalformedURLEx

        String jsonResponse = null;
        try {

            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            System.out.println("An error occurred while making the HTTP request : " + e.getMessage());
        }

        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);


        return earthquakes.stream().filter(earthquake -> earthquake.getLocation().contains(country)).collect(Collectors.toList());

    }

    private List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        if (earthquakeJSON.isEmpty()) {
            return null;
        }
        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < earthquakeArray.length(); i++) {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                JSONObject properties = currentEarthquake.getJSONObject("properties");

                float magnitude = properties.getFloat("mag");

                String url = properties.getString("url");
                String location = "";

                try {
                    location = properties.getString("place");
                } catch (Exception e) {
                    System.out.println("There is an error in the location information. Wrong url: " + url);
                }

                long time = properties.getLong("time");


                Earthquake earthquake = new Earthquake(magnitude, location, time
                        , url);
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            System.out.println("An error occurred while binding the json results to the earthquake object Ex: " + e.getMessage());
        }
        return earthquakes;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                System.out.println("Error response code : " + urlConnection.getResponseCode());


            }

        } catch (IOException e) {
            System.out.println("An error occurred while retrieving earthquake results:" + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}
