package com.example.demo.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.demo.model.Data;

@Component
public class LoaditemWriter implements ItemWriter<Data> {

	private final DataSource datasource;

	public LoaditemWriter(DataSource datasource) {
		super();
		this.datasource = datasource;
	}

	@Override
	public void write(Chunk<? extends Data> chunk) throws Exception {//this method is called for each chuck of data that is to be processed
		try (Connection connection = datasource.getConnection()) {//tries to establish datasource Connection 
			String sql = "INSERT INTO my_table (id, name, value) VALUES (?, ?, ?)";
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				for (Data item : chunk) {
					{
						System.out.println("Writing to DB: " + item);
						ps.setInt(1, item.getId());
						ps.setString(2, item.getName());
						ps.setDouble(3, item.getValue());
						ps.addBatch();
					}
					ps.executeBatch();//execute all the statements in batch
				}
			}
		}

	}
}