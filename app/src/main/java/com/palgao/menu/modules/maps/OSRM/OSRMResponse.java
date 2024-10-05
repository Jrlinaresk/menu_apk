package com.palgao.menu.modules.maps.OSRM;

import java.util.List;

public class OSRMResponse {
    public List<Route> routes;

    public static class Route {
        public List<Leg> legs;
    }

    public static class Leg {
        public List<Step> steps;
    }

    public static class Step {
        public String geometry;
    }
}
