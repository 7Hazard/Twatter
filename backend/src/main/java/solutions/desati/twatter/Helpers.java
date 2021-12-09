package solutions.desati.twatter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class Helpers {

    public static ResponseEntity jsonResponse(JSONObject json) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("content-type", "application/json; charset=utf-8");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(json.toString());
    }

    public static ResponseEntity jsonResponse(JSONArray json) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("content-type", "application/json; charset=utf-8");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(json.toString());
    }
}
