package com.API;

import com.application.panels.ConnectionScreen;
import com.utils.BitMapImage;
import com.utils.Util;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FilterConnector {

    public static final String FILTER_GRAYSCALE = "grayscale";
    public static final String FILTER_INVERT = "invert";
    public static final String FILTER_SMOOTH_SOFT = "smoothSoftIntensity";
    public static final String FILTER_SMOOTH_MEDIUM = "smoothMidIntensity";
    public static final String FILTER_SMOOTH_HARD = "smoothHardIntensity";
    public static final String FILTER_REBALANCE_RED = "rebalanceTheRed";
    public static final String FILTER_REBALANCE_GREEN = "rebalanceTheGreen";
    public static final String FILTER_REBALANCE_BLUE = "rebalanceTheBlue";
    public static final String FILTER_RED_TO_GREEN = "redToGreen";
    public static final String FILTER_GREEN_TO_BLUE = "greenToBlue";
    public static final String FILTER_BLUE_TO_RED = "blueToRed";
    public static final String FILTER_HUE_TO_SATURATION = "hueToSaturation";
    public static final String FILTER_SATURATION_TO_LIGHTNESS = "saturationToLightness";
    public static final String FILTER_LIGHTNESS_TO_HUE = "lightnessToHue";

    public static BitMapImage requestFilter(String type, BitMapImage image) throws IOException, InterruptedException {

        String remote = ConnectionScreen.getInstance().getRemote();

        String imageJson = Util.arrayToJson(image.getRgb());

        String jsonBody = String.format("{\"type\":\"" + type + "\",\"image\":\"%s\"}", imageJson);

        // Send request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(remote + "/filter"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String imageString = response.body();

        int[][][] rgb = Util.parse3DArray(imageString);

        return new BitMapImage(rgb);
    }

}
