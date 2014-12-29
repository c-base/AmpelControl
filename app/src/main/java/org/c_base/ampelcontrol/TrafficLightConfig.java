package org.c_base.ampelcontrol;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TrafficLightConfig {
    private final List<TrafficLightColor> lightOrder;

    public TrafficLightConfig(List<TrafficLightColor> lightOrder) {
        this.lightOrder = Collections.unmodifiableList(new ArrayList<>(lightOrder));
    }

    public List<TrafficLightColor> getLightOrder() {
        return lightOrder;
    }
}
