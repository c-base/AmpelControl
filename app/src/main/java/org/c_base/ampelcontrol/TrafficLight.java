package org.c_base.ampelcontrol;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class TrafficLight {
    private static final TrafficLight AMPEL_1 = new TrafficLight(R.string.ampel_1_name, "151.217.64.87");
//    private static final TrafficLight AMPEL_2 = new TrafficLight(R.string.ampel_2_name, "151.217.43.28");
//    private static final TrafficLight AMPEL_3 = new TrafficLight(R.string.ampel_3_name, "151.217.95.186");

    public static final List<TrafficLight> LIGHTS = Collections.unmodifiableList(
            Arrays.asList(AMPEL_1));

    private final int nameResId;
    private final String host;

    private TrafficLight(int nameResId, String host) {
        this.nameResId = nameResId;
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public int getNameResId() {
        return nameResId;
    }
}
