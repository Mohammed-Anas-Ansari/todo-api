package com.todoapp.todoapi.service;

import com.todoapp.todoapi.dto.ToDoInfoDTO;
import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.exception.DataNotFoundException;
import com.todoapp.todoapi.mapper.EntityToDTO;
import com.todoapp.todoapi.repository.ToDoInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FetchToDoService {

    private final ToDoInfoRepository toDoInfoRepository;

    public List<ToDoInfoDTO> getToDoInfoOverview() {
        List<ToDoInfo> toDoInfos = toDoInfoRepository.findAll();
        return toDoInfos.stream()
                .filter(toDoInfo -> !toDoInfo.isDeleted())
                .map(EntityToDTO::toToDoInfoDTO)
                .collect(Collectors.toList());
    }

    public List<ToDoInfoDTO> getAllToDoInfo() {
        List<ToDoInfo> toDoInfos = toDoInfoRepository.findAll();
        return toDoInfos.stream()
                .filter(toDoInfo -> !toDoInfo.isDeleted())
                .map(EntityToDTO::toToDoInfoDTOWithTasks)
                .collect(Collectors.toList());
    }

    public ToDoInfoDTO getToDoInfoById(long id) {
        return toDoInfoRepository.findById(id)
                .filter(toDoInfo -> !toDoInfo.isDeleted())
                .map(EntityToDTO::toToDoInfoDTOWithTasks)
                .orElseThrow(() -> new DataNotFoundException("To-Do not found with id: " + id));
    }
}
