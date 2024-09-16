package com.entoo.demo.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Report
{
    private String vehicleNo;
    private List<AlertData> data;
}
