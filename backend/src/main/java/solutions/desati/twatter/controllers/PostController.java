package solutions.desati.twatter.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import solutions.desati.twatter.Env;
import solutions.desati.twatter.Helpers;
import solutions.desati.twatter.models.User;
import solutions.desati.twatter.services.AuthService;
import solutions.desati.twatter.services.ChartService;
import solutions.desati.twatter.services.PostService;

@RestController
@RequestMapping("/post")
public class PostController {
    final ChartService chartService;
    final PostService postService;
    final AuthService authService;

    public PostController(PostService postService, AuthService authService, ChartService chartService) {
        this.postService = postService;
        this.authService = authService;
        this.chartService = chartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getSingle(@PathVariable Long id, ProxyExchange<String> proxy) {
        var re = proxy.uri("http://"+ Env.postsServiceHost+"/posts/"+id).get();
        if(!re.getStatusCode().is2xxSuccessful()) return re;

        var post = postService.fillCharts(new JSONObject(re.getBody()));
        return Helpers.jsonResponse(post);
    }

    static @Data class CreatePost { private String content; @Nullable private JsonNode charts; }
    /**
     * @param body
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity create(@RequestBody CreatePost body, @RequestAttribute User user) {
        var post = postService.create(user, body.content, new JSONArray(body.charts.toString()));
        return Helpers.jsonResponse(post);
    }
}
