package com.todoapp.todoapi.exception.controller;

import com.todoapp.todoapi.dto.CustomResponse;
import com.todoapp.todoapi.exception.BadRequestException;
import com.todoapp.todoapi.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.todoapp.todoapi.dto.CustomResponse.error;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<CustomResponse<Object>> handleBadRequestException(BadRequestException ex) {
        log.error("Exception: {} :: {}", ex.getClass().getName(), ex.getMessage());
        CustomResponse<Object> response = error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({DataNotFoundException.class})
    public ResponseEntity<CustomResponse<Object>> handleBadRequestException(DataNotFoundException ex) {
        log.error("Exception: {} :: {}", ex.getClass().getName(), ex.getMessage());
        CustomResponse<Object> response = error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
