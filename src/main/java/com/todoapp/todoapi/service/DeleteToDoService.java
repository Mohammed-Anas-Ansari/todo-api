package com.todoapp.todoapi.service;

import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.enums.DeleteStatus;
import com.todoapp.todoapi.repository.ToDoInfoRepository;
import com.todoapp.todoapi.repository.ToDoTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeleteToDoService {

    private final ToDoInfoRepository toDoInfoRepository;
    private final ToDoTaskRepository toDoTaskRepository;

    public Map<DeleteStatus, List<Long>> deleteTaskByIds(Set<Long> ids) {
        Map<DeleteStatus, List<Long>> map = new EnumMap<>(DeleteStatus.class);
        map.put(DeleteStatus.DELETED, new ArrayList<>());
        map.put(DeleteStatus.NOT_DELETED, new ArrayList<>());
        ids.stream()
                .filter(Objects::nonNull)
                .forEach(id ->
                        toDoTaskRepository.findById(id).ifPresentOrElse(task -> {
                                    toDoTaskRepository.delete(task);
                                    map.get(DeleteStatus.DELETED).add(id);
                                }, () -> map.get(DeleteStatus.NOT_DELETED).add(id)
                        )
                );
        return map;
    }

    public Map<DeleteStatus, List<Long>> deleteToDoByIdsFromRecycleBin(Set<Long> ids) {
        Map<DeleteStatus, List<Long>> map = new EnumMap<>(DeleteStatus.class);
        map.put(DeleteStatus.DELETED, new ArrayList<>());
        map.put(DeleteStatus.NOT_DELETED, new ArrayList<>());
        ids.stream()
                .filter(Objects::nonNull)
                .forEach(id ->
                        toDoInfoRepository.findById(id).ifPresentOrElse(task -> {
                                    if (task.isDeleted()) {
                                        toDoInfoRepository.delete(task);
                                        map.get(DeleteStatus.DELETED).add(id);
                                    } else
                                        map.get(DeleteStatus.NOT_DELETED).add(id);
                                }, () ->
                                        map.get(DeleteStatus.NOT_DELETED).add(id)
                        )
                );
        return map;
    }

    public Map<DeleteStatus, List<Long>> moveToDoByIdsToRecycleBin(Set<Long> ids) {
        Map<DeleteStatus, List<Long>> map = new EnumMap<>(DeleteStatus.class);
        map.put(DeleteStatus.DELETED, new ArrayList<>());
        map.put(DeleteStatus.NOT_DELETED, new ArrayList<>());
        ids.stream()
                .filter(Objects::nonNull)
                .forEach(id ->
                        toDoInfoRepository.findById(id).ifPresentOrElse(task -> {
                                    if (!task.isDeleted()) {
                                        toDoInfoRepository.save(updateIsDeleted(task, true));
                                        map.get(DeleteStatus.DELETED).add(id);
                                    } else
                                        map.get(DeleteStatus.NOT_DELETED).add(id);
                                }, () ->
                                        map.get(DeleteStatus.NOT_DELETED).add(id)
                        )
                );
        return map;
    }

    private ToDoInfo updateIsDeleted(ToDoInfo toDoInfo, boolean isDeleted) {
        return ToDoInfo.builder()
                .id(toDoInfo.getId())
                .title(toDoInfo.getTitle())
                .isDeleted(isDeleted)
                .createdAt(toDoInfo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .toDoTasks(toDoInfo.getToDoTasks())
                .build();
    }
}
