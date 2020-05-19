package com.diffreviewer.repos;

import com.diffreviewer.entities.MergeRequest;
import org.springframework.data.repository.CrudRepository;

public interface MergeRequestRepo extends CrudRepository<MergeRequest, Long> {
}
