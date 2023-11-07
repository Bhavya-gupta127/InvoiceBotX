# PapyrusNebulae 2023 Document Cloud Hackathon: Round 1 - Invoice Data Extraction

This repository contains my solution for Round 1 of the PapyrusNebulae 2023 Document Cloud Hackathon. The goal of this round was to extract information from PDF invoices using the Adobe PDF Services Extract API.

## Problem Description

The problem entailed extracting data from PDF invoices using the Adobe PDF Services Extract API. The provided SampleInvoicesAndData folder contained two sample invoices, and the Extracted Data folder contained the ExtractedData.csv file, representing the desired format for extracted data. The TestDataSet folder included 100 sample invoices, and I extracted data from these invoices in the same format as the ExtractedData.csv file.

## Solution Approach

I implemented the solution using the Java SDK provided by Adobe for the Extract API. The program utilizes the SDK to make API requests and extract data from PDF invoices. The invoice data is then processed and formatted into the required CSV file format.


## Getting Started

To run the code and replicate the solution, follow these steps:

1. Clone this repository to your local machine or download the source code.
2. Ensure that you have Java, Maven and the required dependencies installed.
3. Update the necessary configurations, such as API credentials, in the code if required (For sake of submission I am attaching my credentials).
4. Resource folder contains given testcases you can add more PDF's there to test.
5. Run the program, and it will extract the invoice data and generate the output in the desired format.


## Prerequisites

- Java Development Kit (JDK)
- Adobe PDF Services API credentials file (`pdfservices-api-credentials.json`)
- `opencsv` library

## Code Organization

The code is organized into modules to ensure good quality and readability. Here is a brief overview of the code structure:

- `src/`: This directory contains the source code for the invoice data extraction program.
- `src/main.java`: The main entry point of the program.
- `src/invoice_extractor.java`: Contains the implementation of the invoice extraction logic.
- `src/utils.java`: Contains utility functions used for file handling and data manipulation.
- `output/ExtractedData.csv`: It is the required output file required


## Submission

To submit my solution for the hackathon, I am providing a link containing the code: https://drive.google.com/drive/folders/13b5m2YL0LouuzYh4mKRNCvwNxdQLpMUY?usp=sharing

I have ensured that the code is properly documented and organized for better understanding. If you have any questions or need further clarification, please feel free to reach out.

Thank you for considering my submission.

## About Me

I'm Bhavya Gupta (20ucs052@lnmiit.ac.in), a passionate and versatile developer with expertise in full-stack web development. I love creating seamless user experiences and solving complex problems with technology. Excited to collaborate, learn, and deliver innovative solutions in this hackathon. Let's build something incredible together!
