package com.example.hotel_managment.models;

public class Service {
    final Integer id, service_cost;
    final String service_name, service_type;

    public Service(Integer id, Integer service_cost, String service_name, String service_type) {
        this.id = id;
        this.service_cost = service_cost;
        this.service_name = service_name;
        this.service_type = service_type;
    }

    public Integer getId() {
        return id;
    }

    public Integer getService_cost() {
        return service_cost;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_type() {
        return service_type;
    }


}
