package com.API;

import com.application.panels.ConnectionScreen;
import com.utils.BitMapImage;
import com.utils.Util;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GAConnector {
    
    public static BitMapImage requestGA(
            int height, int width,
            int populationSize, int elite, int regeneration, int generations,
            String generationFunction, String fitnessFunction,
            String selectionFunction, String crossoverFunction, String mutationFunction,
            String fitnessFunctionParams) throws IOException, InterruptedException {
        
        String remote = ConnectionScreen.getInstance().getRemote();
        
        String jsonBody = String.format(
            "{\"height\":%d,\"width\":%d,\"populationSize\":%d,\"elite\":%d,\"regeneration\":%d,\"generations\":%d," +
            "\"generationFunction\":\"%s\",\"fitnessFunction\":\"%s\",\"selectionFunction\":\"%s\"," +
            "\"crossoverFunction\":\"%s\",\"mutationFunction\":\"%s\",\"fitnessFunctionParams\":\"%s\"}",
            height, width, populationSize, elite, regeneration, generations,
            generationFunction, fitnessFunction, selectionFunction, crossoverFunction, mutationFunction,
            fitnessFunctionParams
        );
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(remote + "/ga"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("error");
        }
        
        int[][][] rgb = Util.parse3DArray(response.body());
        
        if (rgb.length == 0) {
            throw new IOException("no image data");
        }
        
        return new BitMapImage(rgb);
    }
}