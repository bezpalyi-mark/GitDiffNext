package com.diffreviewer.repos;

import com.diffreviewer.entities.ListTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListTaskRepo extends CrudRepository<ListTask, Long>{

}