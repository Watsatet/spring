package com.entoo.demo.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageStructure {
    private int pageNo;
    private int itemsPerPage;
    private int totalPages;
    private int totalItems;
}
