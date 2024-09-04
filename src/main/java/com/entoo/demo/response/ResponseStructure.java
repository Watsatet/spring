package com.entoo.demo.response;

import com.entoo.demo.repository.PaginatorAndDetails;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStructure
{
    private int httpStatus;
    private String message;
    private PaginatorAndDetails body;
}
