package com.example.shopBackend.response;

import lombok.Data;

import java.util.List;

@Data
public class ListRes {
    private List<?> responseList;
    private boolean nextPage;

    public ListRes(List<?> responseList, boolean nextPage) {
        this.responseList = responseList;
        this.nextPage = nextPage;
    }
}
