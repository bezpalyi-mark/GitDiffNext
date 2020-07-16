package com.diffreviewer.service;

import com.diffreviewer.entities.ListTask;

import java.util.List;
import java.util.Optional;

public interface ListTaskCRUD {
    List<ListTask> getAll();

    Optional<ListTask> getById(Long id);
}
