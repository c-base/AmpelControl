package org.c_base.ampelcontrol;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TrafficLightController {

    private static final String KEY_RED = "R";
    private static final String KEY_YELLOW = "Y";
    private static final String KEY_GREEN = "G";
    private static final String KEY_ORDER = "order";

    public static TrafficLightController newInstance(String host) {
        return new TrafficLightController(host);
    }


    private String host;

    private TrafficLightController(String host) {
        this.host = host;
    }

    public boolean setAmpelState(TrafficLightState state) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Builder()
                .url("http://" + host + "/cgi-bin/ampel?" +
                        KEY_GREEN + "=" + (state.isGreen() ? "1" : "0") + "&" +
                        KEY_YELLOW + "=" + (state.isYellow() ? "1" : "0") + "&" +
                        KEY_RED + "=" + (state.isRed() ? "1" : "0"))
                .build();

        Response response = client.newCall(request).execute();
        return response.code() == 200;
    }

    public TrafficLightContainer getAmpelState() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Builder()
                .url("http://" + host + "/cgi-bin/ampelstatus")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            throw new IOException("Server returned error code: " + response.code());
        }

        String body = response.body().string();

        try {
            JSONObject json = new JSONObject(body);
            boolean red = json.has(KEY_RED) && json.getInt(KEY_RED) == 1;
            boolean yellow = json.has(KEY_YELLOW) && json.getInt(KEY_YELLOW) == 1;
            boolean green = json.has(KEY_GREEN) && json.getInt(KEY_GREEN) == 1;

            TrafficLightState state = new TrafficLightState.Builder()
                    .red(red)
                    .yellow(yellow)
                    .green(green)
                    .build();

            JSONArray order = json.getJSONArray(KEY_ORDER);
            List<TrafficLightColor> lightOrder = new ArrayList<>();
            for (int i = 0, end = order.length(); i < end; i++) {
                String key = order.getString(i);
                lightOrder.add(getColorForKey(key));
            }

            TrafficLightConfig config = new TrafficLightConfig(lightOrder);

            return new TrafficLightContainer(config, state);
        } catch (JSONException e) {
            throw new IOException("Invalid JSON", e);
        }
    }

    private TrafficLightColor getColorForKey(String key) {
        if (KEY_RED.equals(key)) {
            return TrafficLightColor.RED;
        } else if (KEY_YELLOW.equals(key)) {
            return TrafficLightColor.YELLOW;
        } else if (KEY_GREEN.equals(key)) {
            return TrafficLightColor.GREEN;
        }

        throw new IllegalArgumentException("Unknown traffic light color key: " + key);
    }
}
