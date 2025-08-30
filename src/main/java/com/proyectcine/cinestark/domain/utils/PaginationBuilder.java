package com.proyectcine.cinestark.domain.utils;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
public class PaginationBuilder {
    public static Pageable createSort (String field, String order, Integer page, Integer limit){
        Sort.Direction direction = Sort.Direction.ASC;

        if (order.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Sort s = Sort.by(direction, field);
        return PageRequest.of((page).intValue()-1, limit.intValue()).withSort(s);
    }
}
