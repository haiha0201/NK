package com.learning.nk.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Elasticsearch {
    @Bean
    public ElasticsearchClient esElasticsearchClient() {
        RestClient restClient = RestClient.builder(new HttpHost("localhost" , 9200))
                .build();

        ElasticsearchTransport transport =
                new RestClientTransport(restClient , new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}