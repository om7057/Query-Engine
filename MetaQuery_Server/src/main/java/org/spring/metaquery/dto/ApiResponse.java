package org.spring.metaquery.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * ApiResponse is a generic class that represents the structure of API responses.
 * It includes fields for success status, message, and data.
 *
 * @param <T> the type of data included in the response
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final Boolean success;
    private final String message;
    private final T data;

    public ApiResponse(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(Boolean success, String message) {
        this(success, message, null);
    }
}
