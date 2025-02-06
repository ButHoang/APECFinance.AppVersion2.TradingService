package com.apec_finance.trading.comon;

import lombok.Data;

import java.util.List;

@Data
public class PaginationRS<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}