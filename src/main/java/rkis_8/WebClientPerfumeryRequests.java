package rkis_8;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**Класс, с помощью которого отправляются запросы через WebClient*/
public class WebClientPerfumeryRequests {

    WebClient webClient;

    WebClientPerfumeryRequests(WebClient webClient){
        this.webClient = webClient;
    }

    public List<Perfumery> getAllRequest(String url){
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Perfumery.class)
                .collectList()
                .block();
    }

    public Perfumery getItemRequest(String url){
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Perfumery.class)
                .block();
    }

    public void postRequest(String url, Perfumery item){
        webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Perfumery.class)
                .retrieve()
                .bodyToMono(String.class).block();
    }

    public void putRequest(String url, Perfumery item){
        webClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Perfumery.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void deleteRequest(String url)
    {
        webClient.delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class).block();
    }
}
