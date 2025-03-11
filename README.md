
Disclaimer: This project is for educational purposes only, The data used is minimal, so don’t expect anything too complex!

What It Does:
This project fetches data from an API and a CSV file, combines and processes the data using Spring Batch, and then loads the results into a MySQL database.

Creating the Spring Batch metadata tables in the database is crucial. Without them, job tracking and execution will fail, and you may encounter errors. So ensure these tables are created by running the provided schema file.

JobBuilderFactory and StepBuilderFactory are deprecated use JobBuilder and StepBuilder instead.

Features:
Retrieves data from an API.

Reads data from a CSV file.

Combines and processes data from both sources.

Loads the processed data into a MySQL database.

Automates the entire workflow using Spring Batch.
