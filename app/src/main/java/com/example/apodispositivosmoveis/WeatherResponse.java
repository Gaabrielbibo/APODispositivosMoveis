package com.example.apodispositivosmoveis;

import java.util.List;

public class WeatherResponse {
    public Coord coord;
    public List<Weather> weather;
    public Main main;
    public String name;

    public class Coord {
        public double lon;
        public double lat;
    }

    public class Weather {
        public String description;
    }

    public class Main {
        public double temp;
        public double humidity;
    }
}
