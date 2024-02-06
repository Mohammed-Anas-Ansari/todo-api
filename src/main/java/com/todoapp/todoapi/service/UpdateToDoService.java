package com.todoapp.todoapi.service;

import com.todoapp.todoapi.dto.ToDoInfoDTO;
import com.todoapp.todoapi.dto.ToDoTaskDTO;
import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.entity.ToDoTask;
import com.todoapp.todoapi.exception.BadRequestException;
import com.todoapp.todoapi.exception.DataNotFoundException;
import com.todoapp.todoapi.mapper.EntityToDTO;
import com.todoapp.todoapi.repository.ToDoInfoRepository;
import com.todoapp.todoapi.repository.ToDoTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateToDoService {

    private final ToDoInfoRepository toDoInfoRepository;
    private final ToDoTaskRepository toDoTaskRepository;

    public ToDoInfoDTO updateToDoById(long id, ToDoInfoDTO toDoInfoDTO) {
        ToDoInfo toDoInfo = toDoInfoRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("To-Do not found with id: " + id));
        String title = toDoInfoDTO.getTitle() == null ? "" : toDoInfoDTO.getTitle().trim();
        ToDoInfo updatedToDoInfo = toDoInfoRepository.save(updateTitle(toDoInfo, title));
        return EntityToDTO.toToDoInfoDTO(updatedToDoInfo);
    }

    public ToDoTaskDTO updateToDoTaskById(long taskId, ToDoTaskDTO toDoTaskDTO) {
        if (toDoTaskDTO.getTask() == null || toDoTaskDTO.getTask().isBlank())
            throw new BadRequestException("Task cannot be empty");
        ToDoTask toDoTask = toDoTaskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException("To-Do Task not found with id: " + taskId));
        ToDoTask updatedToDoTask = toDoTaskRepository.save(updateTask(toDoTask, toDoTaskDTO));
        return EntityToDTO.toToDoTaskDTO(updatedToDoTask);
    }

    private ToDoTask updateTask(ToDoTask toDoTask, ToDoTaskDTO toDoTaskDTO) {
        return ToDoTask.builder()
                .id(toDoTask.getId())
                .task(toDoTaskDTO.getTask())
                .isCompleted(toDoTaskDTO.isCompleted())
                .toDoInfo(toDoTask.getToDoInfo())
                .build();
    }

    private ToDoInfo updateTitle(ToDoInfo toDoInfo, String title) {
        return ToDoInfo.builder()
                .id(toDoInfo.getId())
                .title(title)
                .isDeleted(toDoInfo.isDeleted())
                .createdAt(toDoInfo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
