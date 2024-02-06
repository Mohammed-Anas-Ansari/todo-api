package com.todoapp.todoapi.service;

import com.todoapp.todoapi.dto.ToDoInfoDTO;
import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.entity.ToDoTask;
import com.todoapp.todoapi.exception.DataNotFoundException;
import com.todoapp.todoapi.repository.ToDoInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchToDoServiceTest {

    @Mock
    private ToDoInfoRepository toDoInfoRepository;

    @InjectMocks
    private FetchToDoService fetchToDoService;

    private final ToDoTask task1 = ToDoTask.builder()
            .id(1L)
            .task("Task 1")
            .build();
    private final ToDoTask task2 = ToDoTask.builder()
            .id(2L)
            .task("Task 2")
            .build();

    @Test
    public void testGetToDoInfoOverview() {
        // Given
        ToDoInfo toDoInfo1 = ToDoInfo.builder().id(1L).title("Test ToDo 1").build();
        ToDoInfo toDoInfo2 = ToDoInfo.builder().id(2L).title("Test ToDo 2").build();
        List<ToDoInfo> mockToDoInfos = List.of(toDoInfo1, toDoInfo2);

        // Mocking
        when(toDoInfoRepository.findAll()).thenReturn(mockToDoInfos);

        // When
        List<ToDoInfoDTO> result = fetchToDoService.getToDoInfoOverview();

        // Then
        assertEquals(mockToDoInfos.size(), result.size());
        assertEquals(mockToDoInfos.stream().map(ToDoInfo::getId).collect(Collectors.toList()),
                result.stream().map(ToDoInfoDTO::getId).collect(Collectors.toList()));
        assertEquals(mockToDoInfos.stream().map(ToDoInfo::getTitle).collect(Collectors.toList()),
                result.stream().map(ToDoInfoDTO::getTitle).collect(Collectors.toList()));
    }

    @Test
    public void testGetAllToDoInfo() {
        // Given
        ToDoInfo toDoInfo1 = ToDoInfo.builder().id(1L).title("Test ToDo 1").toDoTasks(List.of(task1)).build();
        ToDoInfo toDoInfo2 = ToDoInfo.builder().id(2L).title("Test ToDo 2").toDoTasks(List.of(task2)).build();
        List<ToDoInfo> mockToDoInfos = List.of(toDoInfo1, toDoInfo2);

        // Mocking
        when(toDoInfoRepository.findAll()).thenReturn(mockToDoInfos);

        // When
        List<ToDoInfoDTO> results = fetchToDoService.getAllToDoInfo();

        // Then
        results.forEach(result -> {
            if (result.getId() == 1L) {
                assertEquals(toDoInfo1.getId(), result.getId());
                assertEquals(toDoInfo1.getTitle(), result.getTitle());
                assertEquals(1, result.getToDoTasks().size());
                assertEquals(task1.getId(), result.getToDoTasks().get(0).getId());
                assertEquals(task1.getTask(), result.getToDoTasks().get(0).getTask());
            } else if (result.getId() == 2L) {
                assertEquals(toDoInfo2.getId(), result.getId());
                assertEquals(toDoInfo2.getTitle(), result.getTitle());
                assertEquals(1, result.getToDoTasks().size());
                assertEquals(task2.getId(), result.getToDoTasks().get(0).getId());
                assertEquals(task2.getTask(), result.getToDoTasks().get(0).getTask());
            }
        });
    }

    @Test
    public void testGetToDoInfoById_ExistingId() {
        // Given
        long id = 1L;
        ToDoInfo toDoInfo = ToDoInfo.builder().id(id).title("Test ToDo 1").toDoTasks(List.of(task1)).build();

        // Mocking
        when(toDoInfoRepository.findById(id)).thenReturn(Optional.ofNullable(toDoInfo));

        // When
        ToDoInfoDTO result = fetchToDoService.getToDoInfoById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(toDoInfo.getTitle(), result.getTitle());
        assertEquals(1, result.getToDoTasks().size());
        assertEquals(task1.getId(), result.getToDoTasks().get(0).getId());
        assertEquals(task1.getTask(), result.getToDoTasks().get(0).getTask());
    }

    @Test
    public void testGetToDoInfoById_NonExistingId() {
        // Given
        long id = 100L;

        // Mocking
        when(toDoInfoRepository.findById(id)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(DataNotFoundException.class, () -> fetchToDoService.getToDoInfoById(id));
    }

}