package com.backend;

import com.GA.GeneticAlgorithm;
import com.GA.ImageGenerator;
import com.GA.crossover.CrossoverFunction;
import com.GA.fitness.FitnessFunction;
import com.GA.fitness.adjustment.FitnessAdjustment;
import com.GA.fitness.adjustment.NormalisationAdjustment;
import com.GA.generation.GenerationFunction;
import com.GA.generation.RandomColorGeneration;
import com.GA.mutation.MutationFunction;
import com.GA.selection.SelectionFunction;
import com.application.panels.GAParametersPanel;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.utils.BitMapImage;
import com.utils.ImageUtils;
import com.utils.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BackendApplication {

    public static void main(String[] args) throws IOException {
        int portNumber = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);

        // Assign handlers to endpoints
        server.createContext("/", new RootHandler());
        server.createContext("/generate", new GenerationHandler());
        server.createContext("/filter", new FilterHandler());
        server.createContext("/ga", new GAHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:" + portNumber);
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            try (InputStream is = getClass().getResourceAsStream("rootHandlerWeb.html")) {
                response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class GenerationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if (!"GET".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1); // Method not allowed
                    return;
                }

                // Parse query parameters
                Map<String, String> params = Util.queryToMap(exchange.getRequestURI().getQuery());
                int height = Integer.parseInt(params.getOrDefault("height", "0"));
                int width = Integer.parseInt(params.getOrDefault("width", "0"));
                String type = params.getOrDefault("type", "");

                BitMapImage image = new BitMapImage(width, height);

                if(type.equalsIgnoreCase("randomBitmap"))
                    image = ImageGenerator.randomPixels(height, width);

                if(type.equalsIgnoreCase("randomColour"))
                    image = (new RandomColorGeneration()).generate(height, width).getImage();


                String json = Util.arrayToJson(image.getRgb());
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(json.getBytes());
                os.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class FilterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if (!"POST".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1); // Method not allowed
                    return;
                }

                // Read request body
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                String type = extractJsonValue(body, "type");

                String imageString = extractJsonValue(body, "image");

                int[][][] rgb = Util.parse3DArray(imageString);
                BitMapImage image = new BitMapImage(rgb);

                if(type.equalsIgnoreCase("grayscale"))
                    image = ImageUtils.grayscale(image);

                if(type.equalsIgnoreCase("invert"))
                    image = ImageUtils.invert(image);
 
                if(type.equalsIgnoreCase("smoothSoftIntensity"))
                image = ImageUtils.smoothFilter(image, 0.8, 0.025);
                
                if(type.equalsIgnoreCase("smoothMidIntensity"))
                image = ImageUtils.smoothFilter(image, 0.5, 0.0625);

                if(type.equalsIgnoreCase("smoothHardIntensity"))
                image = ImageUtils.smoothFilter(image, 0.2, 0.1);

                if(type.equalsIgnoreCase("rebalanceTheRed"))
                image = ImageUtils.rgbBalancing(image, 0.6, 0.2, 0.2);

                if(type.equalsIgnoreCase("rebalanceTheGreen"))
                image = ImageUtils.rgbBalancing(image, 0.2, 0.6, 0.2);

                if(type.equalsIgnoreCase("rebalanceTheBlue"))
                image = ImageUtils.rgbBalancing(image, 0.2, 0.2, 0.6);

                if(type.equalsIgnoreCase("redToGreen"))
                image = ImageUtils.spectralProjection(image, "Red", "Green");

                if(type.equalsIgnoreCase("greenToBlue"))
                image = ImageUtils.spectralProjection(image, "Green", "Blue");

                if(type.equalsIgnoreCase("blueToRed"))
                image = ImageUtils.spectralProjection(image, "Blue", "Red");

                if(type.equalsIgnoreCase("hueToSaturation"))
                image = ImageUtils.spectralProjection(image, "Hue", "Saturation");

                if(type.equalsIgnoreCase("saturationToLightness"))
                image = ImageUtils.spectralProjection(image, "Saturation", "Lightness");

                if(type.equalsIgnoreCase("lightnessToHue"))
                image = ImageUtils.spectralProjection(image, "Lightness", "Hue");

                String json = Util.arrayToJson(image.getRgb());
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(json.getBytes());
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return "";
        startIndex += searchKey.length();
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }
        if (startIndex < json.length() && json.charAt(startIndex) == '"') {
            startIndex++; 
            int endIndex = json.indexOf('"', startIndex);
            return endIndex != -1 ? json.substring(startIndex, endIndex) : "";
        } else {
            int endIndex = startIndex;
            while (endIndex < json.length() && 
                   (Character.isDigit(json.charAt(endIndex)) || json.charAt(endIndex) == '-' || json.charAt(endIndex) == '.')) {
                endIndex++;
            }
            return json.substring(startIndex, endIndex);
        }
    }

    static class GAHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
                return;
            }
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            int height = Integer.parseInt(extractJsonValue(body, "height"));
            int width = Integer.parseInt(extractJsonValue(body, "width"));
            int populationSize = Integer.parseInt(extractJsonValue(body, "populationSize"));
            int elite = Integer.parseInt(extractJsonValue(body, "elite"));
            int regeneration = Integer.parseInt(extractJsonValue(body, "regeneration"));
            int generations = Integer.parseInt(extractJsonValue(body, "generations"));
            String generationFunction = extractJsonValue(body, "generationFunction");
            String fitnessFunction = extractJsonValue(body, "fitnessFunction");
            String selectionFunction = extractJsonValue(body, "selectionFunction");
            String crossoverFunction = extractJsonValue(body, "crossoverFunction");
            String mutationFunction = extractJsonValue(body, "mutationFunction");
            String fitnessFunctionParams = extractJsonValue(body, "fitnessFunctionParams");

            GAParametersPanel panel = new GAParametersPanel();
            
            GenerationFunction genFunction = panel.createGenerationFunction(generationFunction, null);
            FitnessFunction fitFunction = panel.createFitnessFunction(fitnessFunction, 
                new String[]{fitnessFunctionParams});
            SelectionFunction selectFunction = panel.createSelectionFunction(selectionFunction, null);
            CrossoverFunction crossFunction = panel.createCrossoverFunction(crossoverFunction, null, 
                new Object[]{fitFunction});
            MutationFunction mutFunction = panel.createMutationFunction(mutationFunction, null, 
                new Object[]{fitFunction});
            FitnessAdjustment fitAdjection = new NormalisationAdjustment();

            GeneticAlgorithm ga = new GeneticAlgorithm(
                height, width, populationSize, elite, 0, regeneration,
                genFunction, fitFunction, selectFunction, crossFunction, mutFunction, fitAdjection
            );

            for (int i = 0; i < generations; i++) {
                ga.gaStep();
            }

            BitMapImage result = ga.getBestIndividual().getImage();
            String json = Util.arrayToJson(result.getRgb());
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, json.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
}
