package com.entoo.demo.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("vehicle_alerts")
@Getter
@Setter
public class Alert {

    private String id;

    private String gpsAssetMasterId;

    private String assetMasterId;

    private String alertId;

    private String deviceMappingId;

    private String state;

    private int city;

    private String latLongCoordinates;

    private String alertName;

    private String vehicleRegistrationNumber;

    private Date created_at;

    private Date modified_at;

    private String userId;

}
