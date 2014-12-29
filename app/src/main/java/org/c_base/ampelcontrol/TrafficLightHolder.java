package org.c_base.ampelcontrol;


import android.content.Context;


public class TrafficLightHolder {
    private final Context context;
    private final TrafficLight trafficLight;

    TrafficLightHolder(Context context, TrafficLight trafficLight) {
        this.context = context;
        this.trafficLight = trafficLight;
    }

    public String getHost() {
        return trafficLight.getHost();
    }

    @Override
    public String toString() {
        return context.getString(trafficLight.getNameResId());
    }
}
