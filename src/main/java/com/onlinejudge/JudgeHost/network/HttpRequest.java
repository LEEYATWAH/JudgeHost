package com.onlinejudge.JudgeHost.network;

import com.onlinejudge.JudgeHost.exception.http.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description 网络请求的封装
 */

public class HttpRequest {
    private static final int MAX_IN_MEMORY_SIZE = 16 * 1024 * 1024;
    public static Resource getFile(String url) {
        Mono<ClientResponse> responseMono = createWebClient()
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .exchange();
        ClientResponse response = responseMono.block();
        if(response == null){
            throw new NotFoundException("B1004");
        }
        return response.bodyToMono(Resource.class).block();
    }

    private static WebClient createWebClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(HttpRequest.MAX_IN_MEMORY_SIZE))
                        .build())
                .build();
    }
}
