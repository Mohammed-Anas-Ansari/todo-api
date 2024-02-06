package com.todoapp.todoapi.service;

import com.todoapp.todoapi.entity.ToDoInfo;
import com.todoapp.todoapi.entity.ToDoTask;
import com.todoapp.todoapi.enums.DeleteStatus;
import com.todoapp.todoapi.repository.ToDoInfoRepository;
import com.todoapp.todoapi.repository.ToDoTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteToDoServiceTest {

    @Mock
    private ToDoInfoRepository toDoInfoRepository;

    @Mock
    private ToDoTaskRepository toDoTaskRepository;

    @InjectMocks
    private DeleteToDoService deleteToDoService;

    private final Set<Long> inputIds = Set.of(1L, 2L);

    @Test
    public void testDeleteTaskByIds() {
        // Given
        ToDoTask task1 = ToDoTask.builder().id(1L).build();
        ToDoTask task2 = ToDoTask.builder().id(2L).build();

        // Mocking
        when(toDoTaskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(toDoTaskRepository.findById(2L)).thenReturn(Optional.of(task2));

        // When
        Map<DeleteStatus, List<Long>> result = deleteToDoService.deleteTaskByIds(inputIds);

        // Then
        assertNotNull(result);
        assertEquals(2, result.get(DeleteStatus.DELETED).size());
        assertTrue(result.get(DeleteStatus.DELETED).containsAll(List.of(1L, 2L)));
        assertTrue(result.get(DeleteStatus.NOT_DELETED).isEmpty());
    }

    @Test
    public void testDeleteToDoByIdsFromRecycleBin() {
        // Given
        ToDoInfo info1 = ToDoInfo.builder().id(1L).isDeleted(true).build();
        ToDoInfo info2 = ToDoInfo.builder().id(2L).isDeleted(false).build();

        // Mocking
        when(toDoInfoRepository.findById(1L)).thenReturn(Optional.of(info1));
        when(toDoInfoRepository.findById(2L)).thenReturn(Optional.of(info2));

        // When
        Map<DeleteStatus, List<Long>> result = deleteToDoService.deleteToDoByIdsFromRecycleBin(inputIds);

        // Then
        assertNotNull(result);
        assertEquals(1, result.get(DeleteStatus.DELETED).size());
        assertTrue(result.get(DeleteStatus.DELETED).contains(1L));
        assertEquals(1, result.get(DeleteStatus.NOT_DELETED).size());
        assertTrue(result.get(DeleteStatus.NOT_DELETED).contains(2L));
    }

    @Test
    public void testMoveToDoByIdsToRecycleBin() {
        // Given
        ToDoInfo info1 = ToDoInfo.builder().id(1L).isDeleted(false).build();
        ToDoInfo info2 = ToDoInfo.builder().id(2L).isDeleted(true).build();

        // Mocking
        when(toDoInfoRepository.findById(1L)).thenReturn(Optional.of(info1));
        when(toDoInfoRepository.findById(2L)).thenReturn(Optional.of(info2));

        // When
        Map<DeleteStatus, List<Long>> result = deleteToDoService.moveToDoByIdsToRecycleBin(inputIds);

        // Then
        assertNotNull(result);
        assertEquals(1, result.get(DeleteStatus.DELETED).size());
        assertTrue(result.get(DeleteStatus.DELETED).contains(1L));
        assertEquals(1, result.get(DeleteStatus.NOT_DELETED).size());
        assertTrue(result.get(DeleteStatus.NOT_DELETED).contains(2L));
    }

}