package com.todoapp.todoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.todoapp.todoapi.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.todoapp.todoapi.enums.ResponseStatus.ERROR;
import static com.todoapp.todoapi.enums.ResponseStatus.SUCCESS;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse<T> {

    private ResponseStatus status;
    private String message;
    private T data;

    public static <T> CustomResponse<T> success(T data) {
        return new CustomResponse<>(SUCCESS, SUCCESS.toString(), data);
    }

    public static <T> CustomResponse<T> error(T data) {
        return new CustomResponse<>(ERROR, ERROR.toString(), data);
    }

    public static <T> CustomResponse<T> error(String message) {
        return new CustomResponse<>(ERROR, message, null);
    }
}
