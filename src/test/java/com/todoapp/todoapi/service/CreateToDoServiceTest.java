package com.todoapp.todoapi.service;

import com.todoapp.todoapi.dto.ToDoInfoDTO;
import com.todoapp.todoapi.dto.ToDoTaskDTO;
import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.entity.ToDoTask;
import com.todoapp.todoapi.exception.BadRequestException;
import com.todoapp.todoapi.repository.ToDoInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateToDoServiceTest {
    @Mock
    private ToDoInfoRepository toDoInfoRepository;

    @InjectMocks
    private CreateToDoService createToDoService;

    @Test
    void testCreateToDoInfo() {
        // Given
        ToDoInfoDTO inputDTO = ToDoInfoDTO.builder().title("Test Title").build();
        ToDoInfo savedToDoInfo = ToDoInfo.builder().id(1L).title("Test Title").build();

        // Mocking
        when(toDoInfoRepository.save(any(ToDoInfo.class))).thenReturn(savedToDoInfo);

        // When
        ToDoInfoDTO resultDTO = createToDoService.createToDoInfo(inputDTO);

        // Then
        assertEquals(savedToDoInfo.getId(), resultDTO.getId());
        assertEquals(savedToDoInfo.getTitle(), resultDTO.getTitle());
    }

    @Test
    void testAddTaskToToDoByIdWithEmptyTaskList() {
        // Given
        ToDoInfoDTO inputDTO = ToDoInfoDTO.builder().toDoTasks(Collections.emptyList()).build();

        // Then
        assertThrows(BadRequestException.class, () -> createToDoService.addTaskToToDoById(1L, inputDTO));
    }

    @Test
    public void testAddTaskToToDoById_Positive() {
        // Given
        long id = 1L;
        ToDoTaskDTO taskDTO = ToDoTaskDTO.builder().task("Test Task 1").build();
        ToDoInfoDTO toDoInfoDTO = ToDoInfoDTO.builder().title("Test ToDo").toDoTasks(List.of(taskDTO)).build();

        ToDoTask savedTask = ToDoTask.builder().id(1L).task("Test Task 1").build();
        ToDoInfo savedToDoInfo = ToDoInfo.builder().id(id).title("Test ToDo").toDoTasks(List.of(savedTask)).build();

        // Mocking
        when(toDoInfoRepository.findById(id)).thenReturn(Optional.of(savedToDoInfo));
        when(toDoInfoRepository.save(any(ToDoInfo.class))).thenReturn(savedToDoInfo);

        // When
        ToDoInfoDTO result = createToDoService.addTaskToToDoById(id, toDoInfoDTO);

        // Then
        assertEquals(savedToDoInfo.getId(), result.getId());
        assertEquals(savedToDoInfo.getTitle(), result.getTitle());
        assertEquals(1, result.getToDoTasks().size());
        assertEquals(savedTask.getTask(), result.getToDoTasks().get(0).getTask());
    }
}