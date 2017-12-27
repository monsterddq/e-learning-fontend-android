package com.sourcey.android.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CategoryDto {
    private String categoryCode;
    private String categoryIntro;
    private List<String> subcategoriesName;
    private List<String> subcategoriesCode;
    private int subcategoriesCount;
    private int creationDate;
    private int lastUpdateDate;

    public CategoryDto(String categoryCode, String categoryIntro) {
        this.categoryCode = categoryCode;
        this.categoryIntro = categoryIntro;
    }
}
