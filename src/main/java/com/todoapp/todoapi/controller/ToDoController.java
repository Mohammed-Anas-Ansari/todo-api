package com.todoapp.todoapi.controller;

import com.todoapp.todoapi.dto.CustomResponse;
import com.todoapp.todoapi.dto.ToDoInfoDTO;
import com.todoapp.todoapi.dto.ToDoTaskDTO;
import com.todoapp.todoapi.enums.DeleteStatus;
import com.todoapp.todoapi.exception.DataNotFoundException;
import com.todoapp.todoapi.service.CreateToDoService;
import com.todoapp.todoapi.service.DeleteToDoService;
import com.todoapp.todoapi.service.FetchToDoService;
import com.todoapp.todoapi.service.UpdateToDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
@Slf4j
public class ToDoController {

    private final CreateToDoService createToDoService;
    private final UpdateToDoService updateToDoService;
    private final FetchToDoService fetchToDoService;
    private final DeleteToDoService deleteToDoService;

    //create to-do
    @PostMapping({"", "/"})
    public ResponseEntity<CustomResponse<ToDoInfoDTO>> createToDo(@RequestBody ToDoInfoDTO toDoInfoDTO) {
        log.info("Create To-Do:: {}", toDoInfoDTO);
        ToDoInfoDTO toDoInfo = createToDoService.createToDoInfo(toDoInfoDTO);
        CustomResponse<ToDoInfoDTO> successResponse = CustomResponse.success(toDoInfo);
        return ResponseEntity.ok(successResponse);
    }

    //add task to to-do by id
    @PostMapping("/{id}/tasks")
    public ResponseEntity<ToDoInfoDTO> addTaskToToDoById(@PathVariable long id, @RequestBody ToDoInfoDTO toDoInfoDTO) {
        log.info("Add Task To To-Do By Id:: {} :: {}", id, toDoInfoDTO);
        return ResponseEntity.ok(createToDoService.addTaskToToDoById(id, toDoInfoDTO));
    }

    //update to-do by id
    @PatchMapping("/{id}")
    public ResponseEntity<CustomResponse<ToDoInfoDTO>> updateToDoById(@PathVariable long id, @RequestBody ToDoInfoDTO toDoInfoDTO) {
        log.info("Update To-Do By Id:: {} :: {}", id, toDoInfoDTO);
        ToDoInfoDTO updatedToDo = updateToDoService.updateToDoById(id, toDoInfoDTO);
        CustomResponse<ToDoInfoDTO> successResponse = CustomResponse.success(updatedToDo);
        return ResponseEntity.ok(successResponse);
    }

    //update task by id
    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<CustomResponse<ToDoTaskDTO>> updateToDoTaskById(@PathVariable long taskId, @RequestBody ToDoTaskDTO toDoTaskDTO) {
        log.info("Update To-Do Task By Id:: {} :: {}", taskId, toDoTaskDTO);
        ToDoTaskDTO updatedToDo = updateToDoService.updateToDoTaskById(taskId, toDoTaskDTO);
        CustomResponse<ToDoTaskDTO> successResponse = CustomResponse.success(updatedToDo);
        return ResponseEntity.ok(successResponse);
    }

    //get title of all the to-do
    @GetMapping({"", "/"})
    public ResponseEntity<CustomResponse<List<ToDoInfoDTO>>> getToDoInfoOverview() {
        log.info("Get To-Do Info Overview");
        List<ToDoInfoDTO> allToDoInfo = fetchToDoService.getToDoInfoOverview();
        CustomResponse<List<ToDoInfoDTO>> successResponse = CustomResponse.success(allToDoInfo);
        return ResponseEntity.ok(successResponse);
    }

    //get title of all the to-do along with its tasks/content
    @GetMapping("/detailed")
    public ResponseEntity<CustomResponse<List<ToDoInfoDTO>>> getAllToDoInfo() {
        log.info("Get All To-Do Info With Tasks");
        List<ToDoInfoDTO> allToDoInfo = fetchToDoService.getAllToDoInfo();
        CustomResponse<List<ToDoInfoDTO>> successResponse = CustomResponse.success(allToDoInfo);
        return ResponseEntity.ok(successResponse);
    }

    //get title by id of a to-do along with its tasks/content
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<ToDoInfoDTO>> getToDoInfoById(@PathVariable long id) {
        log.info("Get To-Do Info By Id:: {}", id);
        ToDoInfoDTO toDoInfoById = fetchToDoService.getToDoInfoById(id);
        CustomResponse<ToDoInfoDTO> successResponse = CustomResponse.success(toDoInfoById);
        return ResponseEntity.ok(successResponse);
    }

    //delete task by ids
    @DeleteMapping("/tasks/{ids}")
    public ResponseEntity<CustomResponse<Map<DeleteStatus, List<Long>>>> deleteTaskByIds(@PathVariable Set<Long> ids) {
        log.info("Delete Task By Ids:: {}", ids);
        Map<DeleteStatus, List<Long>> deletedTaskByIds = deleteToDoService.deleteTaskByIds(ids);
        if (deletedTaskByIds.get(DeleteStatus.DELETED).isEmpty()) {
            throw new DataNotFoundException("Task not found with id: " + ids);
        }
        CustomResponse<Map<DeleteStatus, List<Long>>> successResponse = CustomResponse.success(deletedTaskByIds);
        return ResponseEntity.ok(successResponse);
    }

    //delete to-do by ids (move to recycle bin)
    @DeleteMapping("/{ids}")
    public ResponseEntity<CustomResponse<Map<DeleteStatus, List<Long>>>> deleteToDoByIds(@PathVariable Set<Long> ids) {
        log.info("Delete To-Do By Ids:: {}", ids);
        Map<DeleteStatus, List<Long>> deletedToDoByIds = deleteToDoService.moveToDoByIdsToRecycleBin(ids);
        if (deletedToDoByIds.get(DeleteStatus.DELETED).isEmpty()) {
            throw new DataNotFoundException("To-Do not found with id: " + ids);
        }
        CustomResponse<Map<DeleteStatus, List<Long>>> successResponse = CustomResponse.success(deletedToDoByIds);
        return ResponseEntity.ok(successResponse);
    }

    //delete to-do by ids from recycle bin
    @DeleteMapping("/recycle-bin/{ids}")
    public ResponseEntity<CustomResponse<Map<DeleteStatus, List<Long>>>> deleteToDoByIdsFromRecycleBin(@PathVariable Set<Long> ids) {
        log.info("Delete To-Do By Ids From Recycle Bin:: {}", ids);
        Map<DeleteStatus, List<Long>> deletedToDoByIds = deleteToDoService.deleteToDoByIdsFromRecycleBin(ids);
        if (deletedToDoByIds.get(DeleteStatus.DELETED).isEmpty()) {
            throw new DataNotFoundException("To-Do not found with id: " + ids);
        }
        CustomResponse<Map<DeleteStatus, List<Long>>> successResponse = CustomResponse.success(deletedToDoByIds);
        return ResponseEntity.ok(successResponse);
    }
}
