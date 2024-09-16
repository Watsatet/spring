package com.entoo.demo.document;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("penalty_details")
@Getter
@Setter
public class Penalty
{
    private String _id;
    private String penaltyId;
    private String riderId;
    private String leadCode;
    private String riderName;
    private String clientId;
    private String clientCode;
    private String clientName;
    private String riderCustomerId;
    private double totalPenaltyAmount;
    private double totalPendingAmount;
    private String riderDepartment;
    private double totalCollectedAmount;
    private String status;
    private Date closureDate;
    private Date createdAt;
    private Date modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private List<String> penaltyCategories;
    private String city;
    private String state;
    private String hubId;
    private String _class;
}
