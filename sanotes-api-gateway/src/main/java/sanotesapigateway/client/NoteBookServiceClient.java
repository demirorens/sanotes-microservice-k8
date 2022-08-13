package sanotesapigateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sanotesapigateway.payload.NoteBookResponse;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class NoteBookServiceClient {
    @Value("${clients.notebook-service}")
    private String hostname;// = "http://localhost:8383/"; //"http://notebook-service/";
    private final WebClient.Builder webClient;


    private Function<String, ClientRequest> setBearerTokenInHeader(ClientRequest request) {
        return token -> ClientRequest.from(request).headers(httpHeaders -> httpHeaders.setBearerAuth(token)).build();
    }

    public Mono<List<NoteBookResponse>> getUserNoteBooks() {

        Mono<List<NoteBookResponse>> noteBooks = webClient.baseUrl(hostname)
                .filter(((request, next) -> ReactiveSecurityContextHolder.getContext()
                        .map(ctx -> ((AbstractOAuth2TokenAuthenticationToken) ctx.getAuthentication()).getToken().getTokenValue())
                        .map(setBearerTokenInHeader(request))
                        .flatMap(next::exchange)))
                .build().get()
                .uri("usernotebooks")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<NoteBookResponse>>() {
                });
        return noteBooks;
    }
}
