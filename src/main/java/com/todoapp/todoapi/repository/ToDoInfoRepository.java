package com.todoapp.todoapi.repository;

import com.todoapp.todoapi.entity.ToDoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoInfoRepository extends JpaRepository<ToDoInfo, Long> {
}
