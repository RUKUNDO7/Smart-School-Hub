package com.smartschoolhub.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transport_routes")
public class TransportRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_name", nullable = false, unique = true)
    private String routeName;

    @Column(name = "route_description")
    private String routeDescription;

    @Column(name = "route_cost", nullable = false)
    private BigDecimal routeCost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
