package sanotesapigateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sanotesapigateway.client.NoteBookServiceClient;
import sanotesapigateway.client.NoteServiceClient;
import sanotesapigateway.client.TagServiceClient;
import sanotesapigateway.client.UserServiceClient;
import sanotesapigateway.config.CurrentUser;
import sanotesapigateway.payload.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway")
public class ApiGatewayController {

    private final NoteServiceClient noteServiceClient;

    private final NoteBookServiceClient noteBookServiceClient;

    private final TagServiceClient tagServiceClient;

    private final UserServiceClient userServiceClient;

    private final ReactiveCircuitBreakerFactory cbFactory;

    @GetMapping("useritems")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(summary = "Get User Items",
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"useritems"})
    public Mono<UserItemsResponse> getUserItems(
            @Parameter(hidden = true)
            @CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {

        Mono<UserItemsResponse> userItemsResponse = userServiceClient.getUser()
                .transform(it -> {
                    ReactiveCircuitBreaker cb = cbFactory.create("userServiceCB");
                    return cb.run(it,
                            throwable -> Mono.just(new UserItemsResponse("User service can not respond at the moment please try again later." + getTime())));
                });
        Mono<List<TagResponse>> tags = tagServiceClient.getUserTags()
                .transform(it -> {
                    ReactiveCircuitBreaker cb = cbFactory.create("tagServiceCB");
                    ArrayList<TagResponse> fallBack = new ArrayList<>();
                    fallBack.add(new TagResponse("Tag service can not respond at the moment please try again later." + getTime()));
                    return cb.run(it,
                            throwable -> Mono.just(fallBack));
                });
        Mono<List<NoteBookResponse>> noteBooks = noteBookServiceClient.getUserNoteBooks()
                .transform(it -> {
                    ReactiveCircuitBreaker cb = cbFactory.create("noteBookServiceCB");
                    ArrayList<NoteBookResponse> fallBack = new ArrayList<>();
                    fallBack.add(new NoteBookResponse("Note Book service can not respond at the moment please try again later." + getTime()));
                    return cb.run(it, throwable -> Mono.just(fallBack));
                });
        noteBooks = noteBooks.flatMapMany(noteBookResponses -> {
            return Flux.fromIterable(noteBookResponses)
                    .flatMap(noteBookResponse -> {
                        return noteServiceClient.getNoteBookNotes(noteBookResponse.getId()==null? "": noteBookResponse.getId().toString())
                                .transform(it -> {
                                    ReactiveCircuitBreaker cb = cbFactory.create("noteServiceCB");
                                    ArrayList<NoteResponse> fallBack = new ArrayList<>();
                                    fallBack.add(new NoteResponse("Note service can not respond at the moment please try again later." + getTime()));
                                    return cb.run(it, throwable -> Mono.just(fallBack));
                                })
                                .zipWith(Mono.just(noteBookResponse)).map(tuple -> {
                                    var result = tuple.getT2();
                                    result.setNotes(tuple.getT1());
                                    return result;
                                });
                    });
        }).collectList();

        userItemsResponse = userItemsResponse.zipWith(tags)
                .map(tuple -> {
                    UserItemsResponse userItemsResponse1 = tuple.getT1();
                    List<TagResponse> tags1 = tuple.getT2();
                    userItemsResponse1.setTags(tags1);
                    return userItemsResponse1;
                });
        userItemsResponse = userItemsResponse.zipWith(noteBooks)
                .map(tuple -> {
                    UserItemsResponse userItemsResponse1 = tuple.getT1();
                    List<NoteBookResponse> noteBookResponses1 = tuple.getT2();
                    userItemsResponse1.setNoteBooks(noteBookResponses1);
                    return userItemsResponse1;
                });

        return userItemsResponse;
    }

    @GetMapping("principle")
    @Operation(hidden = true)
    public Mono<OAuth2AuthenticatedPrincipal> getPrinciple(@CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ((OAuth2AuthenticatedPrincipal) ctx.getAuthentication().getPrincipal()));
    }

    @GetMapping("authentication")
    @Operation(hidden = true)
    public Mono<String> getAuthentication(@CurrentUser OAuth2AuthenticatedPrincipal userPrincipal) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ((AbstractOAuth2TokenAuthenticationToken) ctx.getAuthentication()).getToken().getTokenValue());
    }

    private String getTime() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()).toString();
    }

    @RequestMapping("user-fallback")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(hidden = true)
    public Mono<FallBackResponse> getUserFallback() {
        return Mono.just(new FallBackResponse("User service can not respond at the moment please try again later." + getTime()));
    }

    @RequestMapping("note-fallback")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(hidden = true)
    public Mono<FallBackResponse> getNoteFallback() {
        return Mono.just(new FallBackResponse("Note service can not respond at the moment please try again later." + getTime()));
    }

    @RequestMapping("notebook-fallback")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(hidden = true)
    public Mono<FallBackResponse> getNoteBookFallback() {
        return Mono.just(new FallBackResponse("Note Book service can not respond at the moment please try again later." + getTime()));
    }

    @RequestMapping("tag-fallback")
    @PreAuthorize("hasAuthority('sanotes_user')")
    @Operation(hidden = true)
    public Mono<FallBackResponse> getTagFallback() {
        return Mono.just(new FallBackResponse("Tag service can not respond at the moment please try again later." + getTime()));
    }
}
