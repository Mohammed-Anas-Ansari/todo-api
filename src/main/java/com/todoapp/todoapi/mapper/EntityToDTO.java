package com.todoapp.todoapi.mapper;

import com.todoapp.todoapi.dto.ToDoInfoDTO;
import com.todoapp.todoapi.dto.ToDoTaskDTO;
import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.entity.ToDoTask;

import java.util.stream.Collectors;

public class EntityToDTO {

    public static ToDoInfoDTO toToDoInfoDTOWithTasks(ToDoInfo toDoInfo) {
        return ToDoInfoDTO.builder()
                .id(toDoInfo.getId())
                .title(toDoInfo.getTitle())
//                .isDeleted(toDoInfo.isDeleted())
                .createdAt(toDoInfo.getCreatedAt())
                .updatedAt(toDoInfo.getUpdatedAt())
                .toDoTasks(toDoInfo.getToDoTasks().stream()
                        .map(EntityToDTO::toToDoTaskDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public static ToDoInfoDTO toToDoInfoDTO(ToDoInfo toDoInfo) {
        return ToDoInfoDTO.builder()
                .id(toDoInfo.getId())
                .title(toDoInfo.getTitle())
//                .isDeleted(toDoInfo.isDeleted())
                .createdAt(toDoInfo.getCreatedAt())
                .updatedAt(toDoInfo.getUpdatedAt())
                .build();
    }

    public static ToDoTaskDTO toToDoTaskDTO(ToDoTask toDoTask) {
        return ToDoTaskDTO.builder()
                .id(toDoTask.getId())
                .task(toDoTask.getTask())
                .isCompleted(toDoTask.isCompleted())
                .build();
    }
}
