package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/reviews")
public class ReviewsRestApi {
    @Autowired
    private ReviewsService reviewsService;
}
