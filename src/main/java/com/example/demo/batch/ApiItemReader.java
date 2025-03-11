package com.example.demo.batch;

import java.util.Arrays;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Data;

@Component
public class ApiItemReader implements ItemReader<Data>{ //ItemReader helps to read data from API
	 private static final Logger logger = LoggerFactory.getLogger(ApiItemReader.class);
	    //Can create URL using mockapi or other similar services and it's better to place it in env. Or create normal API using REST.
	    private final String URL = "<<<Insert Url here>>>";
	    private Iterator<Data> dataIterator;

	    @Override
	    public Data read() throws Exception {
	        try {
	            if (dataIterator == null) {
	                logger.info("Fetching all data from API: {}", URL);
	                RestTemplate restTemplate = new RestTemplate();
	                Data[] response = restTemplate.getForObject(URL, Data[].class);//getting the data from url

	                if (response == null || response.length == 0) {
	                    logger.warn("API returned no data!");
	                    return null;
	                }

	                dataIterator = Arrays.asList(response).iterator();// load data to dataIterator
	                logger.info("Successfully loaded {} records from API.", response.length);
	            }

	            if (dataIterator.hasNext()) {
	                Data nextData = dataIterator.next();
	                logger.debug("Returning data: {}", nextData);
	                return nextData;
	            }

	            return null;
	        } catch (RestClientException e) {
	            logger.error("Failed to fetch data from API: {}", URL, e);
	            throw new Exception("API fetch error", e);
	        }
	    }
	}
