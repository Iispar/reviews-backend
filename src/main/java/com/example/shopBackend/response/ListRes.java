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

    public List<?> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<?> responseList) {
        this.responseList = responseList;
    }

    public boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }
}
