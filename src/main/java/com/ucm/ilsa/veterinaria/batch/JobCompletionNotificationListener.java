package com.ucm.ilsa.veterinaria.batch;


import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends
		JobExecutionListenerSupport {

	private final static Logger LOGGER = Logger
			.getLogger(JobCompletionNotificationListener.class);


	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOGGER.info("!!! JOB FINISHED! Time to verify the results");
		}
	}
}
