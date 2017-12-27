/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sourcey.android.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Pager<T> {
    private int currentPage;
    private int noOfRowInPage;
    private long totalRow;
    private long totalPage;
    private List<T> results;

    public int getCurrentPage() {
        return currentPage + 1;
    }
}
