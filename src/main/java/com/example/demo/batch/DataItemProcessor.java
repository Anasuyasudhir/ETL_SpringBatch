//We merge data from csv and api
package com.example.demo.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.model.Data;

@Component
public class DataItemProcessor implements ItemProcessor<Data, Data> {

	private final ApiItemReader apiItemReader;
	  //map to store API data (id -> name) for fast lookup
	private final Map<Integer, String> apiDataMap = new HashMap<>();

	public DataItemProcessor(ApiItemReader apiItemReader) {
		this.apiItemReader = apiItemReader;
	}

	 //method to load API data lazily (only when needed)
	private void preloadApiDataIfNeeded() throws Exception {
		if (apiDataMap.isEmpty()) {
			Data apiData;
			while ((apiData = apiItemReader.read()) != null) {
				apiDataMap.put(apiData.getId(), apiData.getName());//store data into map
			}
		}
	}

	@Override
	public Data process(Data csvData) throws Exception {//method is called for each record from csv
		preloadApiDataIfNeeded();//load data from api
		String name = apiDataMap.get(csvData.getId());//get name
		return (name != null) ? new Data(csvData.getId(), name, csvData.getValue()) : null;//if name is present we get id name value
	}
}
