package com.titanic.hackingreactive.examplequery;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

@Getter
@NoArgsConstructor
public class ExampleItem {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String distributorRegion;
    private Date releaseDate;
    private int availableUnits;
    private Point location;
    private boolean active;

    public ExampleItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
