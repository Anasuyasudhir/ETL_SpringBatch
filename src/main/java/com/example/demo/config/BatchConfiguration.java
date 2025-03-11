package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.batch.ApiItemReader;
import com.example.demo.batch.CSVItemReader;
import com.example.demo.batch.DataItemProcessor;
import com.example.demo.batch.LoaditemWriter;
import com.example.demo.model.Data;



@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	private final JobRepository jobRepository;//manages jobs and steps-automatically provided by springboot
	private final PlatformTransactionManager transactionManager;//manages db transaction during batch processing

	public BatchConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
	}

	@Bean
	MySQLMaxValueIncrementer jobIncrementer(DataSource dataSource) {//generate unique ids to batch jobs
		MySQLMaxValueIncrementer incrementer = new MySQLMaxValueIncrementer();
		incrementer.setDataSource(dataSource);
		incrementer.setIncrementerName("BATCH_JOB_SEQ");
		incrementer.setColumnName("ID");
		return incrementer;
	}

	@Bean
	CSVItemReader csvItemReader() {//Creates a bean for the CSVItemReader class, which reads data from a CSV file.input for batch
		return new CSVItemReader();
	}

	@Bean
	ItemProcessor<Data, Data> dataItemProcessor(ApiItemReader apiItemReader) throws Exception {
		return new DataItemProcessor(apiItemReader);//Creates bean for DataItemProcessor class which combines data from csv and api
	}

	@Bean
	ItemWriter<Data> loadItemWriter(LoaditemWriter loadItemWriter) {
		return loadItemWriter;//creates bean for LoadItemWriter, which writes data to db
	}

	@Bean
	Job importDataJob(Step step1) {//creates Job with name importDataJob and calls step and executes
		return new JobBuilder("importDataJob", jobRepository).start(step1).build();
	}

	@Bean
	Step step1(CSVItemReader csvItemReader, ItemProcessor<Data, Data> dataItemProcessor, ItemWriter<Data> loadItemWriter) {
		return new StepBuilder("step1", jobRepository).<Data, Data>chunk(10, transactionManager).reader(csvItemReader)
				.processor(dataItemProcessor).writer(loadItemWriter).build();//Reads, processes, and writes data in chunks of 10 items at a time.
	}


}
