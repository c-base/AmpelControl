package org.c_base.ampelcontrol;


public class TrafficLightContainer {
    private final TrafficLightConfig config;
    private final TrafficLightState state;

    public TrafficLightContainer(TrafficLightConfig config, TrafficLightState state) {
        this.config = config;
        this.state = state;
    }

    public TrafficLightConfig getConfig() {
        return config;
    }

    public TrafficLightState getState() {
        return state;
    }
}
