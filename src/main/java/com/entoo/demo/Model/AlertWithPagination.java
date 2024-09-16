package com.entoo.demo.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AlertWithPagination
{
    private String _id;
    private Report main;
    private int totalItems;
}
