/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.util.List;

/**
 *
 * @author Admin
 */
public class GridView {
    private Object data;
    private int pageIndex;
    private int pageSize;
    private List<GridFiltering> filtering;
    private long count;
    private List<Object> response;
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<GridFiltering> getFiltering() {
        return filtering;
    }

    public void setFiltering(List<GridFiltering> filtering) {
        this.filtering = filtering;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Object> getResponse() {
        return response;
    }

    public void setResponse(List<Object> response) {
        this.response = response;
    }

    
    
}
