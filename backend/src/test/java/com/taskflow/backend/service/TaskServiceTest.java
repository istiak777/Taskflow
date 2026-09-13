package com.taskflow.backend.service;

import com.taskflow.backend.entity.Task;
import com.taskflow.backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task("Write tests", "Cover the service layer", false);
        sampleTask.setId(1L);
    }

    @Test
    void getAllTasks_returnsAllTasksFromRepository() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(List.of(sampleTask));

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Write tests");
    }

    @Test
    void getTaskById_whenFound_returnsTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Task result = taskService.getTaskById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getTaskById_whenNotFound_throwsException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void createTask_savesAndReturnsTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.createTask(new Task("Write tests", "Cover the service layer", false));

        assertThat(result.getId()).isEqualTo(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deleteTask_whenNotFound_throwsException() {
        when(taskRepository.existsById(42L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(42L))
                .isInstanceOf(NoSuchElementException.class);

        verify(taskRepository, never()).deleteById(any());
    }
}
