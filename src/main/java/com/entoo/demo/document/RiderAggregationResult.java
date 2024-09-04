package com.entoo.demo.document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiderAggregationResult {
    private String deductionId;          // Maps to "$penaltyId"
    private Object deductionCategory;    // Maps to "$penaltyCategories" (use Object if it's an array or complex type)
    private String riderName;            // Maps to "$riderName"
    private String clientName;           // Maps to "$clientName"
    private String hubName;              // Maps to "$result1.name"
    private String riderId;              // Maps to "$lead_code"
    private String city;                 // Maps to "$result3.city_name"
    private String riderCustomerId;      // Maps to "$riderCustomerId"
    private String createdDate;          // Maps to formatted date string from "$createdAt"
    private String modifiedDate;         // Maps to formatted date string from "$modifiedAt"
    private String mobile;               // Maps to "$result2.mobile"
    private String status;               // Maps to "$result4.value"
    private double totalCollectedAmount; // Maps to "$totalCollectedAmount" (assuming it's a numeric type)
}


