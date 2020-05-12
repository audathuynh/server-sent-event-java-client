package org.maxsure.rabbitmq.rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class Program implements CommandLineRunner {

    private final Processor processor;

    public Program(Processor processor) {
        this.processor = Preconditions.checkNotNull(processor, "processor");
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Running the app...");

        String topic;
        if (args.length > 0) {
            topic = args[0].trim();
        } else {
            topic = "a.*";
        }
        try {
            processor.process(topic);
        } catch (Exception e) {
            log.error("Error when processing...", e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Program.class, args);
    }

}
