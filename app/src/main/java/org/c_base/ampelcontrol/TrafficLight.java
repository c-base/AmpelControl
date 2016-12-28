package org.c_base.ampelcontrol;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class TrafficLight {
    private static final TrafficLight AMPEL_1 = new TrafficLight(R.string.ampel_1_name, "alberich.c3kid.space");
    private static final TrafficLight AMPEL_2 = new TrafficLight(R.string.ampel_2_name, "brunhilde.c3kid.space");
    private static final TrafficLight AMPEL_3 = new TrafficLight(R.string.ampel_3_name, "sigfried.c3kid.space");

    public static final List<TrafficLight> LIGHTS = Collections.unmodifiableList(
            Arrays.asList(AMPEL_1, AMPEL_2, AMPEL_3));

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
