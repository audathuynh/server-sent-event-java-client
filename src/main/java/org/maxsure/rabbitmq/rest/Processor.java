package org.maxsure.rabbitmq.rest;

import java.util.Objects;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class Processor {
    public static final String URI = "/subscribe?routing-key=";

    private final WebClient webClient;

    public Processor() {
        this.webClient = WebClient.create("http://localhost:8080/rest/v0.1");
    }

    public void process(String topic) {
        String uri = URI + topic;

        ParameterizedTypeReference<ServerSentEvent<String>> elementType =
                new ParameterizedTypeReference<ServerSentEvent<String>>() {};
        Flux<ServerSentEvent<String>> elementStream = webClient.get()
                .uri(uri)
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToFlux(elementType);
        elementStream.subscribe(
                element -> {
                    if (Objects.isNull(element)) {
                        return;
                    }

                    String data = element.data();
                    log.info("Message: {}", data);
                },
                error -> log.error("Error when processing messages from Rest APIs for RabbitMQ\n{}",
                        error),
                () -> log.info("Processing messages from Rest API completed"));
        log.info("Subscribed the topic: [{}], URI: [{}]", topic, uri);
    }

}
