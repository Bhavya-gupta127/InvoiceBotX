package com.example;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.ExtractPDFOperation;
import com.adobe.pdfservices.operation.pdfops.options.extractpdf.ExtractElementType;
import com.adobe.pdfservices.operation.pdfops.options.extractpdf.ExtractPDFOptions;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


public class PDF_Invoice_Extractor {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PDF_Invoice_Extractor.class);

    public static void pdfToJson(String inputFilePath) {
        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            // Create an ExecutionContext using credentials.
            ExecutionContext executionContext = ExecutionContext.create(credentials);

            ExtractPDFOperation extractPDFOperation = ExtractPDFOperation.createNew();

            // Provide an input FileRef for the operation
            FileRef source = FileRef.createFromLocalFile(inputFilePath);
            extractPDFOperation.setInputFile(source);

            // Build ExtractPDF options and set them into the operation
            ExtractPDFOptions extractPDFOptions = ExtractPDFOptions.extractPdfOptionsBuilder()
                    .addElementsToExtract(Arrays.asList(ExtractElementType.TEXT))
                    .build();
            extractPDFOperation.setOptions(extractPDFOptions);

            // Execute the operation
            FileRef result = extractPDFOperation.execute(executionContext);

            // Save the result at the specified location
            String outputFilePath = createOutputFilePath();
            result.saveAs(outputFilePath);

            Utils jsonTraversalFromFile = new Utils();
            jsonTraversalFromFile.main(new String[]{outputFilePath, "Bhavya Gupta"});


        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException e) {
            LOGGER.error("Exception encountered while executing operation", e);
        }
    }

    public static void main(String[] args) {
        pdfToJson(args[0]);
    }

    //Generates a string containing a directory structure and file name for the output file.
    public static String createOutputFilePath() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        return ("output/ExtractTextInfoFromPDF/extract" + timeStamp + ".zip");
    }

}

