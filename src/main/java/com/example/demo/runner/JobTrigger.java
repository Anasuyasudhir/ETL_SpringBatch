package com.example.demo.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobTrigger implements CommandLineRunner {
    //commanLineRunner executes Job automatically when the application starts.
    @Autowired
    private JobLauncher jobLauncher;//batch job is triggered using joblauncher provided by spring batch

    @Autowired
    private Job importDataJob;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Triggering the batch job...");
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // Unique job instance
                .toJobParameters();

        jobLauncher.run(importDataJob, params);
        System.out.println("Batch job completed.");
    }
}



//can remove commandLineRunner and use scheduler and enable scheduling to define cronn time so we can specify at what time interval should the job run