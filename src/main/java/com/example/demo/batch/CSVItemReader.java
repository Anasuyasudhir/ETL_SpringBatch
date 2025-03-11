package com.example.demo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import com.example.demo.model.Data;

public class CSVItemReader extends FlatFileItemReader<Data> {
	//Extending Spring Batch's FlatFileItemReader which is prebuilt class for reading files CSV and mapping them to Object(here Data) 
	private static final Logger logger = LoggerFactory.getLogger(CSVItemReader.class);

	public CSVItemReader() {
		{
			logger.info("Initializing CSVItemReader...");
			setResource(new ClassPathResource("data.csv")); //location of data csv file
			setLinesToSkip(1);//skipping the header in the csv file
			setLineMapper(new DefaultLineMapper<>() {
				{
					setLineTokenizer(new DelimitedLineTokenizer() {//mapping line by line in csv to Object Data , delimiter splits line using delimiter here ','
						{
							setNames("id", "value");
							logger.info("Line tokenizer set with field names: id, value.");
						}
					});
					setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {//BeanWrapperFieldSetMapper maps the tokens(fields) to Data Object
						{
							setTargetType(Data.class);
							logger.info("FieldSetMapper configured for Data class.");
						}
					});
				}
			});
		}

	}
}
