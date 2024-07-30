package com.graviton.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graviton.model.PackageDetails;
import com.graviton.model.Purchase;
import com.graviton.model.Service;
import com.graviton.model.Usage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading JSON files and converting them to Java objects.
 */
public class FileReader {

    /**
     * Loads package data from a JSON file and converts it into a list of {@link PackageDetails}.
     *
     * @param pricingInputFile The name of the JSON file containing package data.
     * @return A list of {@link PackageDetails}.
     */
    public static List<PackageDetails> loadPackageData(String pricingInputFile) {
        Map<String, Object> data = readFile(pricingInputFile);
        List<Map<String, Object>> packages = (List<Map<String, Object>>) data.get("packages");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(packages, new TypeReference<List<PackageDetails>>() {});
    }

    /**
     * Loads service data from a JSON file and converts it into a list of {@link Service}.
     *
     * @param serviceInputFile The name of the JSON file containing service data.
     * @return A list of {@link Service}.
     */
    public static List<Service> loadServiceData(String serviceInputFile) {
        Map<String, Object> data = readFile(serviceInputFile);
        List<Map<String, Object>> services = (List<Map<String, Object>>) data.get("services");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(services, new TypeReference<List<Service>>() {});
    }

    /**
     * Loads purchase data from a JSON file and converts it into a list of {@link Purchase}.
     *
     * @param purchaseInputFile The name of the JSON file containing purchase data.
     * @return A list of {@link Purchase}.
     */
    public static List<Purchase> loadPurchaseData(String purchaseInputFile) {
        Map<String, Object> data = readFile(purchaseInputFile);
        List<Map<String, Object>> purchases = (List<Map<String, Object>>) data.get("purchase");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(purchases, new TypeReference<List<Purchase>>() {});
    }

    /**
     * Loads usage data from a JSON file and converts it into a list of {@link Usage}.
     *
     * @param usageInputFile The name of the JSON file containing usage data.
     * @return A list of {@link Usage}.
     */
    public static List<Usage> loadUsagesData(String usageInputFile) {
        Map<String, Object> data = readFile(usageInputFile);
        List<Map<String, Object>> usages = (List<Map<String, Object>>) data.get("usages");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(usages, new TypeReference<List<Usage>>() {});
    }

    /**
     * Reads a JSON file and maps its content into a {@link Map}.
     *
     * @param inputFile The name of the JSON file to read.
     * @return A {@link Map} containing the JSON data.
     * @throws RuntimeException If the file is not found or an error occurs while reading the file.
     */
    public static Map<String, Object> readFile(String inputFile) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(inputFile)) {
            if (inputStream == null) {
                throw new RuntimeException("File not found: " + inputFile);
            }
            return mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + inputFile, e);
        }
    }
}
