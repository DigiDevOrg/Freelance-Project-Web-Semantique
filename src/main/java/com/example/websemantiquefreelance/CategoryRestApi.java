package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/category")
public class CategoryRestApi {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/all")
    @ResponseStatus
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping
    @ResponseStatus
    public ResponseEntity<?> add(@RequestParam String categoryName, @RequestParam String categoryDesc) {
        System.out.println(categoryName + " " +categoryDesc);
        return ResponseEntity.ok(categoryService.addCategory(categoryName, categoryDesc));
    }
}
