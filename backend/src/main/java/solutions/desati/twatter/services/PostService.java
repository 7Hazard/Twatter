package solutions.desati.twatter.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import solutions.desati.twatter.models.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    UserService userService;
    final ChartService chartService;

    public PostService(ChartService chartService) {
        this.chartService = chartService;
    }

    public JSONObject create(User author, String content, JSONArray charts) {

        var post = new JSONObject();

        if (charts != null) {
            // Create charts via chart service
            var chartsJson = chartService.create(charts);
            for (int i = 0; i < chartsJson.length(); i++) {
                var chartJson = chartsJson.getJSONObject(i);
                chartsJson.put(i, chartJson.getLong("id"));
            }
            post.put("charts", chartsJson);
        }

        post.put("author", author.getId());
        post.put("content", content);

        var posts = new JSONArray();
        posts.put(post);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        var request = new HttpEntity(posts.toString(), headers);
        var response = new RestTemplate().exchange(
                "http://localhost:34566/posts",
                HttpMethod.POST,
                request,
                String.class
        );

        return (new JSONArray(response.getBody())).getJSONObject(0);
    }

    public JSONObject fillCharts(JSONObject post) {
        LinkedList<Number> chartIds = new LinkedList<>();
        for(var cid : post.getJSONArray("charts")) chartIds.add((Number) cid);
        if(!chartIds.isEmpty())
            post.put("charts", chartService.get(chartIds));

        return post;
    }

    public JSONArray get(List<Long> authors) {
        var uri = UriComponentsBuilder.fromHttpUrl("http://localhost:34566/posts");
        for(var author : authors) {
            uri.queryParam("author", author);
        }

        // Fetch
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        var request = new HttpEntity(headers);
        var response = new RestTemplate().exchange(
                uri.build().encode().toUriString(),
                HttpMethod.GET,
                request,
                String.class
        );

        // Process
        var posts = new JSONArray(response.getBody());

        // Process charts, populate author
        var chartIds = new HashSet<Number>();
        for(var p : posts) {
            var post = (JSONObject)p;

            // Populate author
            var author = new JSONObject(userService.get(post.getLong("author")).getView());
            post.put("author", author);

            for(var id : post.getJSONArray("charts")) {
                chartIds.add((Integer)id);
            }
        }
        var chartsJson = chartService.get(chartIds.stream().toList());
        var idToChart = new HashMap<Number, JSONObject>();
        for(var c : chartsJson) {
            var chart = (JSONObject)c;
            idToChart.put(chart.getLong("id"), chart);
        }

        for(var p : posts) {
            var post = (JSONObject) p;
            var charts = post.getJSONArray("charts");
            for (int i = 0; i < charts.length(); i++) {
                var id = charts.getLong(i);
                var chart = idToChart.get(id);
                charts.put(i, chart);
            }
        }

        return posts;
    }

    public JSONArray getFeed(User user) {

        var following = user.getFollowing();
        var authorIds = following.stream().map(User::getId).collect(Collectors.toList());
        authorIds.add(user.getId());

        if(authorIds.isEmpty())
            return new JSONArray();

        return get(authorIds);
    }
}
