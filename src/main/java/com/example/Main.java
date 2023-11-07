package com.example;

import com.opencsv.CSVWriter;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {

        createCsvFile(); //create CSV file

        //Traverse all output files
        PDF_Invoice_Extractor extractTextInfoFromPDF = new PDF_Invoice_Extractor();

        for (int i = 0; i < 100; i++) {
            String[] inputFilePath = new String[]{"src/main/resources/output" + i + ".pdf"};
            extractTextInfoFromPDF.main(inputFilePath);
        }
    }

    //Create CSV File with desired header
    private static void createCsvFile() {
        String CSVPath = "output/ExtractedData.csv";

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(CSVPath), StandardCharsets.UTF_8);
             CSVWriter csvWriter = new CSVWriter(writer)) {
            // Create the header row
            String[] header = {
                    "Bussiness__City",
                    "Bussiness__Country",
                    "Bussiness__Description",
                    "Bussiness__Name",
                    "Bussiness__StreetAddress",
                    "Bussiness__Zipcode",
                    "Customer__Address__line1",
                    "Customer__Address__line2",
                    "Customer__Email",
                    "Customer__Name",
                    "Customer__PhoneNumber",
                    "Invoice__BillDetails__Name",
                    "Invoice__BillDetails__Quantity",
                    "Invoice__BillDetails__Rate",
                    "Invoice__Description",
                    "Invoice__DueDate",
                    "Invoice__IssueDate",
                    "Invoice__Number",
                    "Invoice__Tax"
            };
            csvWriter.writeNext(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
