package com.todoapp.todoapi.service;

import com.todoapp.todoapi.dto.ToDoInfoDTO;
import com.todoapp.todoapi.dto.ToDoTaskDTO;
import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.entity.ToDoTask;
import com.todoapp.todoapi.repository.ToDoInfoRepository;
import com.todoapp.todoapi.repository.ToDoTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateToDoServiceTest {

    @Mock
    private ToDoInfoRepository toDoInfoRepository;

    @Mock
    private ToDoTaskRepository toDoTaskRepository;

    @InjectMocks
    private UpdateToDoService updateToDoService;

    @Test
    public void testUpdateToDoById() {
        // Given
        long id = 1L;
        ToDoInfoDTO toDoInfoDTO = ToDoInfoDTO.builder().title("Updated Title").build();
        ToDoInfo existingToDoInfo = ToDoInfo.builder().id(id).title("Old Title").isDeleted(false).build();

        ToDoInfo updatedToDoInfo = ToDoInfo.builder()
                .id(id)
                .title(toDoInfoDTO.getTitle())
                .isDeleted(existingToDoInfo.isDeleted())
                .createdAt(existingToDoInfo.getCreatedAt())
                .updatedAt(LocalDateTime.now()).build();

        // Mocking
        when(toDoInfoRepository.findById(id)).thenReturn(Optional.of(existingToDoInfo));
        when(toDoInfoRepository.save(any(ToDoInfo.class))).thenReturn(updatedToDoInfo);

        // When
        ToDoInfoDTO result = updateToDoService.updateToDoById(id, toDoInfoDTO);

        // Then
        assertNotNull(result);
        assertEquals(toDoInfoDTO.getTitle(), result.getTitle());
        assertEquals(existingToDoInfo.getId(), result.getId());
        assertEquals(existingToDoInfo.isDeleted(), result.isDeleted());
    }

    @Test
    public void testUpdateToDoTaskById() {
        // Given
        long taskId = 1L;
        ToDoTaskDTO toDoTaskDTO = ToDoTaskDTO.builder().task("Updated Task").isCompleted(true).build();
        ToDoTask existingToDoTask = ToDoTask.builder().id(taskId).task("Old Task").isCompleted(false).build();

        ToDoTask updatedToDoTask = ToDoTask.builder()
                .id(taskId)
                .task(toDoTaskDTO.getTask())
                .isCompleted(toDoTaskDTO.isCompleted()).build();

        // Mocking
        when(toDoTaskRepository.findById(taskId)).thenReturn(Optional.of(existingToDoTask));
        when(toDoTaskRepository.save(any(ToDoTask.class))).thenReturn(updatedToDoTask);

        // When
        ToDoTaskDTO result = updateToDoService.updateToDoTaskById(taskId, toDoTaskDTO);

        // Then
        assertNotNull(result);
        assertEquals(toDoTaskDTO.getTask(), result.getTask());
        assertEquals(existingToDoTask.getId(), result.getId());
        assertEquals(toDoTaskDTO.isCompleted(), result.isCompleted());
    }
}