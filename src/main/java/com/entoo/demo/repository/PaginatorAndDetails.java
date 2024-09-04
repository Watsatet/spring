package com.entoo.demo.repository;

import com.entoo.demo.document.User;
import com.entoo.demo.pagination.PageStructure;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatorAndDetails {
    private List<User> details;
    private PageStructure paginator;
}
