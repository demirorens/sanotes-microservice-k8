package sanotesnoteservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sanotesnoteservice.payload.BooleanResponse;
import sanotesnoteservice.payload.TagResponse;

@FeignClient(name = "api-gateway", url = "${api-gateway.url}")
public interface ApiGatewayClient {

    @GetMapping("/notebook/owner/{noteBookId}")
    BooleanResponse getIsUserOwner(@PathVariable("noteBookId") String noteBookId);

    @GetMapping("/tag")
    TagResponse getTag(@RequestParam("id") String tagId);
}
