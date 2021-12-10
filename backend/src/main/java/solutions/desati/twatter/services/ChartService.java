package solutions.desati.twatter.services;

import org.json.JSONArray;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import solutions.desati.twatter.Env;

import java.util.List;

@Service
public class ChartService {

    public JSONArray get(List<Number> chartIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        var uri = UriComponentsBuilder.fromHttpUrl("http://"+ Env.chartsServiceHost +"/charts");
        for(var chartid: chartIds)
            uri.queryParam("id", chartid);

        var request = new HttpEntity(headers);
        var response = new RestTemplate().exchange(
                uri.build().encode().toUriString(),
                HttpMethod.GET,
                request,
                String.class
        );
        return new JSONArray(response.getBody());
    }

    public JSONArray create(JSONArray charts) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        var request = new HttpEntity(charts.toString(), headers);
        var response = new RestTemplate().exchange(
                "http://"+ Env.chartsServiceHost+"/charts",
                HttpMethod.POST,
                request,
                String.class
        );
        return new JSONArray(response.getBody());
    }
}
