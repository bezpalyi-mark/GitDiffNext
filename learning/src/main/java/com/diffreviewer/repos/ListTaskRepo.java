package com.diffreviewer.repos;

import com.diffreviewer.entities.ListTask;
import org.springframework.data.repository.CrudRepository;

public interface ListTaskRepo extends CrudRepository<ListTask, Long>{

}