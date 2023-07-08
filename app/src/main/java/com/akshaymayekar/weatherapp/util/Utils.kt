package com.akshaymayekar.weatherapp.util

import com.akshaymayekar.weatherapp.view.adapter.City

class Utils {

    companion object {

        fun getCityCoordinates(): HashMap<String, City> {
            var map = HashMap<String, City>()

            map["Pune"] = City(18.5204, 73.8567);
            map["Mumbai"] = City(19.0760, 72.8777);
            map["Navi Mumbai"] = City(19.0330, 73.0297);
            map["Surat"] = City(21.1702, 72.8311);
            map["Nagpur"] = City(21.1458, 79.0882);
            map["Nashik"] = City(19.9975, 73.7898);
            map["Kolhapur"] = City(16.7050, 74.2433);


            return map
        }

        fun getCityList(): MutableList<String> {
            var list =  mutableListOf<String>()
            list.add("Pune");
            list.add("Mumbai");
            list.add("Navi Mumbai");
            list.add("Surat");
            list.add("Nagpur");
            list.add("Nashik");
            list.add("Kolhapur");

            return list
        }
    }
}