package com.todoapp.todoapi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_info_sequence")
    @SequenceGenerator(name = "todo_info_sequence", sequenceName = "todo_info_sequence", allocationSize = 1)
    private long id;
    private String title;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;
    @CreationTimestamp
    @Column(columnDefinition = "DATETIME(3)")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(columnDefinition = "DATETIME(3)")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "toDoInfo", cascade = CascadeType.ALL)
    private List<ToDoTask> toDoTasks;

    public void addToDoTask(ToDoTask toDoTask) {
        if (toDoTasks == null) {
            toDoTasks = new ArrayList<>();
        }
        toDoTasks.add(toDoTask);
        toDoTask.setToDoInfo(this);
    }

    public void addToDoTasks(List<ToDoTask> toDoTasks) {
        this.toDoTasks = toDoTasks;
    }
}
