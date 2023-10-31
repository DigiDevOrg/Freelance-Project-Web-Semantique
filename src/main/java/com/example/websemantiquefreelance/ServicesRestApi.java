package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.ServicesService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/services")
public class ServicesRestApi {
    private ServicesService servicesService;
}
