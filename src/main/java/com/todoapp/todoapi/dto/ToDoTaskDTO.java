package com.todoapp.todoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@ToString
@Getter
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToDoTaskDTO {
    private long id;
    @Length(max = 255, message = "Task length should be less than 255")
    private String task;
    private boolean isCompleted;
}
