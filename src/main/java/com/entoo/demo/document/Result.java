package com.entoo.demo.document;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Result
{
    private Count countFacet;
    private List<User> projectionFacet;
}
