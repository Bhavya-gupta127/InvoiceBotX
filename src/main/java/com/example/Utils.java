package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Utils {
    public static void main(String[] args) {
        try {
            String zipFilePath;
            // Extract the JSON file from the zipped folder
            if (args.length == 0) {
                System.out.println("Please provide the JSON string as an argument.");
                return;
            }
            zipFilePath = args[0];
            String jsonFileName = "structuredData.json";
            File jsonFile = extractJsonFileFromZip(zipFilePath, jsonFileName);

            if (jsonFile != null) {
                // Create an instance of ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();

                // Read JSON data from the extracted file
                JsonNode rootNode = objectMapper.readTree(jsonFile);

                // Traverse the JSON structure
                Map<String, Object> dataMap = new HashMap<>();
                traverseJson(rootNode, dataMap);

                Map<String, Map<String, String>> keywords = getKeywordsMap(dataMap);

                // Finding required data
                // Define variables for different fields
                String topkey;
                String bottomkey;
                String leftkey;
                String rightkey;
                String toppos;
                String bottompos;
                String leftpos;
                String rightpos;
                String temp;
                int page;
                ArrayList<String> parsedData;


                // Invoice number
                topkey = "Invoice#";
                bottomkey = "Issue date";
                leftkey = "BILL TO";
                rightkey = "NULL";
                toppos = "top";
                bottompos = "bottom";
                leftpos = "right";
                rightpos = "top";
                page = 0;
                String invoiceNum = getData(leftkey, bottomkey, rightkey, topkey, leftpos, bottompos, rightpos, toppos, page, dataMap, keywords);

                invoiceNum = invoiceregex(invoiceNum);
                temp = "";
                for (int j = 0; j < invoiceNum.length(); j++) {
                    if (invoiceNum.charAt(j) != ';' && invoiceNum.charAt(j) != ' ')
                        temp += invoiceNum.charAt(j);
                }
                invoiceNum = temp;

                // Issue date
                topkey = "Invoice#";
                bottomkey = "PAYMENT";
                leftkey = "DETAILS";
                rightkey = "NULL";
                toppos = "top";
                bottompos = "top";
                leftpos = "right";
                rightpos = "top";
                page = 0;
                String issuedate = getData(leftkey, bottomkey, rightkey, topkey, leftpos, bottompos, rightpos, toppos, page, dataMap, keywords);
                issuedate = issuedateregex(issuedate);

                // Due date
                topkey = "PAYMENT";
                bottomkey = "RATE";
                leftkey = "DETAILS";
                rightkey = "NULL";
                toppos = "top";
                bottompos = "top";
                leftpos = "right";
                rightpos = "top";
                page = 0;
                String duedate = getData(leftkey, bottomkey, rightkey, topkey, leftpos, bottompos, rightpos, toppos, page, dataMap, keywords);
                duedate = issuedateregex(duedate);

                // Details
                topkey = "DETAILS";
                bottomkey = "RATE";
                leftkey = "BILL TO";
                rightkey = "PAYMENT";
                toppos = "top";
                bottompos = "top";
                leftpos = "right";
                rightpos = "left";
                page = 0;
                String details = getData(leftkey, bottomkey, rightkey, topkey, leftpos, bottompos, rightpos, toppos, page, dataMap, keywords);
                parsedData = splitString(details, ";");
                temp = "";
                for (int i = 0; i < parsedData.size(); i++)
                    temp += parsedData.get(i);
                details = detailsregex(temp);


                //Bussiness
                String bussinessCity = "Jamestown";
                String bussinessCountry = "Tennessee, USA";
                String bussinessStreetAddress = "3741 Glory Road";
                String bussinessZip = "38556";
                String bussinessName = "NearBy Electronics";
                String bussinessdesc = "We are here to serve you better. Reach out to us in case of any concern or feedbacks.";
                String tax = "10";

                // Customer data
                topkey = "BILL TO";
                bottomkey = "ITEM";
                leftkey = "NULL";
                rightkey = "DETAILS";
                toppos = "top";
                bottompos = "top";
                leftpos = "right";
                rightpos = "left"; //default
                page = 0;

                String customerData = getData(leftkey, bottomkey, rightkey, topkey, leftpos, bottompos, rightpos, toppos, page, dataMap, keywords);
                parsedData = splitString(customerData, ";");
                String customerString = "";
                for (int i = 0; i < parsedData.size(); i++)
                    customerString += parsedData.get(i) + " ";

                parsedData = splitString(customerString, " ");

                int i = 2;
                String customerName = parsedData.get(i++) + " " + parsedData.get(i++);
                String customerMailId = parsedData.get(i++);
                if (!customerMailId.endsWith(".com"))
                    customerMailId += parsedData.get(i++);
                String customerPhoneNumber = parsedData.get(i++);
                String customerAddress1 = "";
                String customerAddress2 = "";

                while (i < parsedData.size() && (customerAddress1.length() + parsedData.get(i).length() < 19)) {
                    customerAddress1 += parsedData.get(i) + " ";
                    i++;
                }

                while (i < parsedData.size() && customerAddress2.length() <= 23) {
                    customerAddress2 += parsedData.get(i) + " ";
                    i++;
                }

//                Billing Details =>Name, Quantity & Rate
                // Name
                topkey = "ITEM";
                bottomkey = "NULL";
                leftkey = "NULL";
                rightkey = "QTY";
                toppos = "bottom";
                bottompos = "top";
                leftpos = "right";
                rightpos = "left";
                page = 0;
                String invoiceBillName = getData(leftkey, bottomkey, rightkey, topkey, leftpos, bottompos, rightpos, toppos, page, dataMap, keywords);
                ArrayList<String> ArrBillName = splitString(invoiceBillName, ";");

                // QTY
                topkey = "QTY";
                bottomkey = "NULL";
                leftkey = "ITEM";
                rightkey = "RATE";
                toppos = "bottom";
                bottompos = "top";
                leftpos = "right";
                rightpos = "left"; //efdefault
                page = 0;

                String invoiceBillQTY = getData(leftkey, bottomkey, rightkey, topkey, leftpos, bottompos, rightpos, toppos, page, dataMap, keywords);
                ArrayList<String> ArrBillQTY = splitString(invoiceBillQTY, ";");

                // Rate
                topkey = "ITEM";
                bottomkey = "NULL";
                leftkey = "QTY";
                rightkey = "AMOUNT";
                toppos = "bottom";
                bottompos = "top";
                leftpos = "right";
                rightpos = "left"; //default
                page = 0;

                String invoiceBillRate = getData(leftkey, bottomkey, rightkey, topkey, leftpos, bottompos, rightpos, toppos, page, dataMap, keywords);
                ArrayList<String> ArrBillRate = splitString(invoiceBillRate, ";");


                for (i = 0; i < ArrBillQTY.size(); i++) {
                    if (!ArrBillName.get(i).isEmpty() && !ArrBillQTY.get(i).isEmpty() && !ArrBillRate.get(i).isEmpty()) {
//                        push to csv file
                        List<String> rowData1 = new ArrayList<>();
                        rowData1.add(bussinessCity);
                        rowData1.add(bussinessCountry);
                        rowData1.add(bussinessdesc);
                        rowData1.add(bussinessName);
                        rowData1.add(bussinessStreetAddress);
                        rowData1.add(bussinessZip);
                        rowData1.add(customerAddress1);
                        rowData1.add(customerAddress2);
                        rowData1.add(customerMailId);
                        rowData1.add(customerName);
                        rowData1.add(customerPhoneNumber);
                        rowData1.add(ArrBillName.get(i));
                        rowData1.add(ArrBillQTY.get(i));
                        rowData1.add(ArrBillRate.get(i));
                        rowData1.add(details);
                        rowData1.add(duedate);
                        rowData1.add(issuedate);
                        rowData1.add(invoiceNum);
                        rowData1.add(tax);
                        addRowToCsv(rowData1);

                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract the JSON file from the zipped folder
    private static File extractJsonFileFromZip(String zipFilePath, String jsonFileName) throws IOException {
        File zipFile = new File(zipFilePath);
        ZipFile archive = new ZipFile(zipFile);
        ZipEntry entry = archive.getEntry(jsonFileName);

        if (entry != null) {
            // Create a temporary file to extract the JSON file
            File tempFile = File.createTempFile(jsonFileName, ".json");

            try (InputStream inputStream = archive.getInputStream(entry);
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            archive.close();
            return tempFile;
        } else {
            System.out.println("JSON file not found in the zip folder.");
            archive.close();
            return null;
        }
    }

    // Helper method to traverse the JSON structure and store the data in a map
    private static void traverseJson(JsonNode node, Map<String, Object> dataMap) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();

                if (fieldName.equals("elements")) {
                    // Traverse the 'elements' array
                    traverseElementsArray(fieldValue, dataMap);
                } else {
                    // Continue traversing other objects and arrays
                    traverseJson(fieldValue, dataMap);
                }
            });
        }
    }

    private static void traverseElementsArray(JsonNode elementsArray, Map<String, Object> dataMap) {
        elementsArray.forEach(element -> {
            if (element.isObject()) {
                JsonNode textNode = element.get("Text");
                JsonNode pageNode = element.get("Page");
                JsonNode pathNode = element.get("Path");
                JsonNode fontNode = element.path("Font");

                if (textNode != null && textNode.isTextual() && fontNode.isObject()) {
                    String text = textNode.asText();
                    JsonNode boundsNode = element.get("Bounds");
                    int weight = fontNode.path("weight").asInt();
                    int page = pageNode.asInt();
                    String path = pathNode.asText();
                    if (boundsNode != null && boundsNode.isArray()) {
                        // Extract the bounds array
                        double[] bounds = extractBounds(boundsNode);
                        double left, bottom, right, top;
                        left = bounds[0];
                        bottom = bounds[1];
                        right = bounds[2];
                        top = bounds[3];
                        // Store the extracted data in the map
                        dataMap.put(path, new Data(text, left, bottom, right, top, weight, page, path));
                    }
                }
            }
        });
    }

    // Helper method to extract keywords and their positions from the data map
    public static Map<String, Map<String, String>> getKeywordsMap(Map<String, Object> data) {
        Map<String, Map<String, String>> keywords = new HashMap<>();

        List<String> keywordList = Arrays.asList(
                "Invoice#",
                "Issue date",
                "BILL TO",
                "DETAILS",
                "PAYMENT",
                "ITEM",
                "QTY",
                "RATE",
                "AMOUNT",
                "Tax %",
                "NearBy Electronics",
                "Subtotal",
                "Total Due"
        );

        // Iterate over the data map
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Check if the value is an instance of Data
            if (value instanceof Data) {
                Data dataObject = (Data) value;

                // Check if the text contains any of the keywords
                for (String keyword : keywordList) {
                    if (dataObject.getText().contains(keyword)) {
                        String text = dataObject.getText();
                        String left = String.valueOf(dataObject.getLeft());
                        String bottom = String.valueOf(dataObject.getBottom());
                        String right = String.valueOf(dataObject.getRight());
                        String top = String.valueOf(dataObject.getTop());

                        Map<String, String> coordinates = new HashMap<>();
                        coordinates.put("left", left);
                        coordinates.put("bottom", bottom);
                        coordinates.put("right", right);
                        coordinates.put("top", top);

                        // Add the text and coordinates to the keywords map
                        keywords.put(keyword, coordinates);
                    }
                }
            }
        }
        return keywords;
    }

    private static void addRowToCsv(List<String> rowData) {
        String CSVPath = "output/ExtractedData.csv";
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(CSVPath, true), StandardCharsets.UTF_8))) {
            String[] data = rowData.toArray(new String[0]);
            writer.writeNext(data);
            System.out.println("Row Added: Data extraction and CSV generation complete.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to perform custom data extraction using regular expressions
    private static String detailsregex(String details) {
        details = details.replaceAll("DETAILS", "");
        return details;
    }

    private static String issuedateregex(String issuedate) {
        Pattern pattern = Pattern.compile("\\b\\d{2}-\\d{2}-\\d{4}\\b");
        Matcher matcher = pattern.matcher(issuedate);

        // Find the matching date and extract it
        if (matcher.find()) {
            issuedate = matcher.group();
        }

        String inputDate = issuedate;
        String[] dateParts = inputDate.split("-");

        if (dateParts.length == 3) {
            String day = dateParts[0];
            String month = dateParts[1];
            String year = dateParts[2];
            if (day.startsWith("0")) {
                day = day.substring(1);
            }
            if (month.startsWith("0")) {
                month = month.substring(1);
            }
            // Create the output date string in the desired format
            String outputDate = day + "/" + month + "/" + year;

            issuedate = outputDate;
        } else {
            issuedate = "Notfoundissuedate";
        }
        return issuedate;
    }

    private static String invoiceregex(String invoiceNum) {
        Pattern pattern = Pattern.compile("Invoice#\\s*(.*?)\\s*Issue date");
        Matcher matcher = pattern.matcher(invoiceNum);

        // Find the matching text and extract it
        if (matcher.find()) {
            invoiceNum = matcher.group(1);
        }

        return invoiceNum;
    }

    // Helper method to extract data based on the given keys and positions
    private static String getData(String leftkey, String bottomkey, String rightkey, String topkey, String leftpos, String bottompos, String rightpos, String toppos, int page, Map<String, Object> dataMap, Map<String, Map<String, String>> keywords) {

        String s = "";
        if ((topkey == "NULL" || keywords.containsKey(topkey)) && (bottomkey == "NULL" || keywords.containsKey(bottomkey)) && (leftkey == "NULL" || keywords.containsKey(leftkey)) && (rightkey == "NULL" || keywords.containsKey(rightkey))) {
            double top = 0, bottom = 0, left = 0, right = 0;
            if (topkey != "NULL")
                top = Double.parseDouble(keywords.get(topkey).get(toppos));
            if (bottomkey != "NULL")
                bottom = Double.parseDouble(keywords.get(bottomkey).get(bottompos));
            if (leftkey != "NULL")
                left = Double.parseDouble(keywords.get(leftkey).get(leftpos));
            if (rightkey != "NULL")
                right = Double.parseDouble(keywords.get(rightkey).get(rightpos));
            ;
            ArrayList<Pair<Double, String>> v = new ArrayList<>();
            // Traverse the dataMap and print elements that satisfy the condition
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                Object value = entry.getValue();
//                String key = entry.getKey();
                String key = ((Data) value).text;

                Data data = (Data) value;
                if ((topkey == "NULL" || data.getTop() <= top) && (bottomkey == "NULL" || data.getBottom() >= bottom) && (leftkey == "NULL" || data.getLeft() >= left) && (rightkey == "NULL" || data.getRight() <= right) && (page == -1 || data.getPage() == page)) {
                    v.add(new Pair<>(data.getTop(), key));
                }
            }
            Collections.sort(v, Collections.reverseOrder());
            int i = 0;
            for (Pair<Double, String> pair : v) {
                s += pair.getSecond() + " ; ";
            }
        }
        return s;
    }

    private static double[] extractBounds(JsonNode boundsNode) {
        int size = boundsNode.size();
        double[] bounds = new double[size];

        for (int i = 0; i < size; i++) {
            bounds[i] = boundsNode.get(i).asDouble();
        }
        return bounds;
    }

    //    Splits the input string into lines using the specified delimiter and returns an ArrayList of parsed lines.
    private static ArrayList<String> splitString(String inputString, String delimiter) {
        List<String> lines = Arrays.asList(inputString.split(delimiter));
        ArrayList<String> parsedData = new ArrayList<>(lines.size());

        for (String line : lines) {
            parsedData.add(line.trim());
        }
        return parsedData;
    }


    public static class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {
        private T1 first;
        private T2 second;

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 getFirst() {
            return first;
        }

        public T2 getSecond() {
            return second;
        }

        @Override
        public int compareTo(Pair<T1, T2> other) {
            return Double.compare((Double) this.first, (Double) other.first);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    public static class Data {
         String text,path;
         double left,bottom,right,top;
         int weight,page;

        public Data(String text, double left, double bottom, double right, double top, int weight, int page, String path) {
            this.text = text;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
            this.top = top;
            this.weight = weight;
            this.page=page;
            this.path=path;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "text='" + text + '\'' +
                    ", left=" + left +
                    ", bottom=" + bottom+
                    ", right=" + right+
                    ", top=" + top +
                    ", weight=" + weight +
                    ", page=" + page +
                    ", path=" + path +
                    '}';
        }
        // Getters and setters

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public double getLeft() {
            return left;
        }

        public void setLeft(double left) {
            this.left = left;
        }

        public double getBottom() {
            return bottom;
        }

        public void setBottom(double bottom) {
            this.bottom = bottom;
        }

        public double getRight() {
            return right;
        }

        public void setRight(double right) {
            this.right = right;
        }

        public double getTop() {
            return top;
        }

        public void setTop(double top) {
            this.top = top;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}
