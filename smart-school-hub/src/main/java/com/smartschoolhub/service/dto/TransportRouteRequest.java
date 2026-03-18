package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransportRouteRequest {
    @NotBlank
    private String routeName;

    private String routeDescription;

    @NotNull
    private BigDecimal routeCost;

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteDescription() {
        return routeDescription;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    public BigDecimal getRouteCost() {
        return routeCost;
    }

    public void setRouteCost(BigDecimal routeCost) {
        this.routeCost = routeCost;
    }
}
