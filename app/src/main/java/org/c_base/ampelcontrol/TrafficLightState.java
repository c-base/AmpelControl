package org.c_base.ampelcontrol;


public class TrafficLightState {
    private final boolean red;
    private final boolean yellow;
    private final boolean green;

    private TrafficLightState(Builder builder) {
        this.red = builder.red;
        this.yellow = builder.yellow;
        this.green = builder.green;
    }

    public boolean isRed() {
        return red;
    }

    public boolean isYellow() {
        return yellow;
    }

    public boolean isGreen() {
        return green;
    }

    public static class Builder {
        private boolean red;
        private boolean yellow;
        private boolean green;

        public TrafficLightState build() {
            return new TrafficLightState(this);
        }

        public Builder red(boolean red) {
            this.red = red;
            return this;
        }

        public Builder yellow(boolean yellow) {
            this.yellow = yellow;
            return this;
        }

        public Builder green(boolean green) {
            this.green = green;
            return this;
        }
    }
}
