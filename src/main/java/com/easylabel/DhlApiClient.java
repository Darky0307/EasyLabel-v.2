package com.easylabel;

import com.easylabel.model.ShipmentRequest;
import com.easylabel.model.LabelResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DhlApiClient {
    private final String apiUrl;
    private final String token;
    private final CloseableHttpClient http = HttpClients.createDefault();
    private final ObjectMapper json = new ObjectMapper();

    public DhlApiClient(String apiUrl, String token) {
        this.apiUrl = apiUrl;
        this.token = token;
    }

    /**
     * Erstellt eine Sendung bei DHL und liefert Trackingnummer + Label-URL
     */
    public LabelResponse createShipment(ShipmentRequest req) throws Exception {
        HttpPost post = new HttpPost(apiUrl + "/shipments");
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(json.writeValueAsString(req), "UTF-8"));
        try (CloseableHttpResponse resp = http.execute(post)) {
            return json.readValue(resp.getEntity().getContent(), LabelResponse.class);
        }
    }

    /**
     * Storniert eine bestehende Sendung bei DHL
     */
    public boolean cancelShipment(String trackingNumber) throws Exception {
        HttpDelete del = new HttpDelete(apiUrl + "/shipments/" + trackingNumber);
        del.setHeader("Authorization", "Bearer " + token);
        try (CloseableHttpResponse resp = http.execute(del)) {
            return resp.getStatusLine().getStatusCode() == 204;
        }
    }
}