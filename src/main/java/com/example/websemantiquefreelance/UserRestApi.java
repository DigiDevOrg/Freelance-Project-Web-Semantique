package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserRestApi {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @ResponseStatus
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(userService.getAll());
    }

}