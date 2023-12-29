package com.practice.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private int page = 0;
    private int size = 10;
    private String search;
}
