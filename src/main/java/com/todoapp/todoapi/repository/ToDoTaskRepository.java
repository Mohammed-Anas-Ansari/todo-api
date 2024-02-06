package com.todoapp.todoapi.repository;

import com.todoapp.todoapi.entity.ToDoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoTaskRepository extends JpaRepository<ToDoTask, Long> {
}
