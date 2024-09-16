package com.entoo.demo.document;

import com.entoo.demo.Model.Report;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Result
{
    private List<Count> countFacet;
    private List<Report> projectionFacet;
}
