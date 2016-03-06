package com.ucm.ilsa.veterinaria.batch;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.collect.Maps;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;
import com.ucm.ilsa.veterinaria.repository.RiskRepository;

//@Configuration
//@EnableBatchProcessing
public class RiskBatchConfiguration {
	
	@Autowired
	private RiskRepository repositoryRisk;
	
	// tag::readerwriterprocessor[]
    @Bean
    public ItemReader<Risk> reader() {
        RepositoryItemReader<Risk> reader = new RepositoryItemReader<Risk>();
        reader.setRepository(repositoryRisk);
        reader.setMethodName("findAll");
        Map<String,Direction> sort = Maps.newHashMap();
        sort.put("title",Direction.ASC);
        reader.setSort(sort);
        return reader;
    }

    @Bean
    public ItemProcessor<Risk, Risk> processor() {
        return new RiskItemProcessor();
    }

    @Bean
    public ItemWriter<Risk> writer() {
        RepositoryItemWriter<Risk> writer = new RepositoryItemWriter<Risk>();
        writer.setRepository(repositoryRisk);
        writer.setMethodName("save");
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1, JobExecutionListener listener) {
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Risk> reader,
            ItemWriter<Risk> writer, ItemProcessor<Risk, Risk> processor) {
        return stepBuilderFactory.get("step1")
                .<Risk, Risk> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // end::jobstep[]


}
