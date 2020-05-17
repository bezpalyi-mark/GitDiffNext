package com.example.repos;

import com.example.entities.MergeRequest;
import com.example.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MergeRequestRepo extends CrudRepository<MergeRequest, Long> {
}
