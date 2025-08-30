package com.proyectcine.cinestark.api.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.proyectcine.cinestark.domain.utils.Views;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * Represents metadata information for pagination.
 * <p>
 * This class is annotated with the Lombok annotations @Data and @Builder to automatically generate getter, setter,
 * <p>
 * toString, equals, and hashCode methods.
 */
@Data
@Builder
@JsonView(Views.Public.class)
public class MetaResponseDTO {

    /**
     * The current page number.
     */
    private Integer pageNumber;
    /**
     * The number of items to take per page.
     */
    private Integer take;
    /**
     * The total number of items available.
     */
    private Long totalRecords;
    /**
     * The total number of pages.
     */
    private Long totalPages;
    /**
     * Indicates if there is a previous page.
     */
    private boolean hasPreviousPage;
    /**
     * Indicates if there is a next page.
     */
    private boolean hasNextPage;

    public static <T> MetaResponseDTO buildMetaResponseDTO(Page<T> products, Integer pageNumber, Integer limit){
        return MetaResponseDTO.builder()
                .pageNumber(pageNumber)
                .take(limit)
                .totalRecords(products.getTotalElements())
                .totalPages((long) products.getTotalPages())
                .hasPreviousPage(products.hasPrevious())
                .hasNextPage(products.hasNext())
                .build();
    }
}
