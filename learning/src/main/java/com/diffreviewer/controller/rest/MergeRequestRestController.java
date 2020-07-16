package com.diffreviewer.controller.rest;

import com.diffreviewer.entities.MergeRequest;
import com.diffreviewer.entities.request.SaveMergeRequest;
import com.diffreviewer.exception.MergeRequestNotFoundException;
import com.diffreviewer.service.MergeRequestCRUD;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class MergeRequestRestController {

    private final MergeRequestCRUD mergeRequestCRUD;

    public MergeRequestRestController(MergeRequestCRUD mergeRequestCRUD) {
        this.mergeRequestCRUD = mergeRequestCRUD;
    }

    @GetMapping
    public List<MergeRequest> getAll() {
        return mergeRequestCRUD.getAll();
    }

    @GetMapping("/{id}")
    public MergeRequest get(@PathVariable Long id) {
        return mergeRequestCRUD.getById(id).orElseThrow(() -> new MergeRequestNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MergeRequest create(@RequestBody SaveMergeRequest request) {
        return mergeRequestCRUD.create(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody SaveMergeRequest request) {
        mergeRequestCRUD.update(id, request);
    }

    @DeleteMapping("/{id}")
    public MergeRequest delete(@PathVariable Long id) {
        return mergeRequestCRUD.deleteById(id).orElseThrow(() -> new MergeRequestNotFoundException(id));
    }
}
