package com.todoapp.todoapi.service;

import com.todoapp.todoapi.dto.ToDoInfoDTO;
import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.entity.ToDoTask;
import com.todoapp.todoapi.exception.BadRequestException;
import com.todoapp.todoapi.exception.DataNotFoundException;
import com.todoapp.todoapi.mapper.EntityToDTO;
import com.todoapp.todoapi.repository.ToDoInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateToDoService {

    private final ToDoInfoRepository toDoInfoRepository;

    public ToDoInfoDTO createToDoInfo(ToDoInfoDTO toDoInfoDTO) {
        String title = toDoInfoDTO.getTitle() == null ? "" : toDoInfoDTO.getTitle().trim();
        ToDoInfo toDoInfo = toDoInfoRepository.save(ToDoInfo.builder().title(title).build());
        return EntityToDTO.toToDoInfoDTO(toDoInfo);
    }

    public ToDoInfoDTO addTaskToToDoById(long id, ToDoInfoDTO toDoInfoDTO) {
        if (toDoInfoDTO.getToDoTasks() == null || toDoInfoDTO.getToDoTasks().isEmpty())
            throw new BadRequestException("Task list cannot be empty");

        ToDoInfo toDoInfo = toDoInfoRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("To-Do not found with id: " + id));

        List<ToDoTask> toDoTasks = new ArrayList<>();
        toDoInfoDTO.getToDoTasks().stream()
                .map(taskDTO -> ToDoTask.builder().task(taskDTO.getTask()).build())
                .forEach(task -> {
                    task.setToDoInfo(toDoInfo);
                    if (task.getTask() == null || task.getTask().isBlank())
                        throw new BadRequestException("Task cannot be empty");
                    toDoTasks.add(task);
                });
        toDoInfo.addToDoTasks(toDoTasks);
        toDoInfoRepository.save(toDoInfo);
        return EntityToDTO.toToDoInfoDTOWithTasks(toDoInfo);
    }
}
