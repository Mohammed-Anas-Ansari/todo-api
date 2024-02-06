package com.todoapp.todoapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_task_sequence")
    @SequenceGenerator(name = "todo_task_sequence", sequenceName = "todo_task_sequence", allocationSize = 1)
    private long id;
    @ManyToOne
    private ToDoInfo toDoInfo;
    private String task;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isCompleted;

    public void setToDoInfo(ToDoInfo toDoInfo) {
        this.toDoInfo = toDoInfo;
    }
}
