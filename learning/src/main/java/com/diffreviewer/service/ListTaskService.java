package com.diffreviewer.service;

import com.diffreviewer.entities.ListTask;
import com.diffreviewer.repos.ListTaskRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ListTaskService implements ListTaskCRUD {

    private final ListTaskRepo listTaskRepo;

    public ListTaskService(ListTaskRepo listTaskRepo) {
        this.listTaskRepo = listTaskRepo;
    }

    @Override
    public List<ListTask> getAll() {
        List<ListTask> list = new ArrayList<>();
        listTaskRepo.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Optional<ListTask> getById(Long id) {
        return listTaskRepo.findById(id);
    }
}
