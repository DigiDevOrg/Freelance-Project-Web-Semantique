package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.ServicesService;
import com.example.websemantiquefreelance.utils.JenaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/services")
public class ServicesRestApi {

   @Autowired
    private ServicesService servicesService;

    @GetMapping("/all")
    public List<Map<String, String>> getAllServices() {
        return servicesService.getAllServices();
    }

    @GetMapping("/category")
    public List<Map<String, String>> getServicesByCategory(@RequestParam String categoryURI) {
        return servicesService.getServicesByCategory(categoryURI);
    }

    @GetMapping("/details")
    public List<Map<String, String>> getServiceDetails(@RequestParam String serviceURI) {
        return servicesService.getServiceDetails(serviceURI);
    }

    @GetMapping("/search")
    public List<Map<String, String>> searchServices(@RequestParam String keyword) {
        return servicesService.searchServices(keyword);
    }

    @GetMapping("/servicesByFreelancer")
    public List<Map<String, String>> getServicesByFreelancer(@RequestParam String freelancerURI) {
        return servicesService.getServicesByFreelancer(freelancerURI);
    }

    @PostMapping("/addService")
    public ResponseEntity<String> addService(
            @RequestParam String serviceName,
            @RequestParam String serviceDescription,
            @RequestParam String servicePrice,
            @RequestParam String freelancerURI
    ) {
        String result = servicesService.addService(serviceName, serviceDescription, servicePrice, freelancerURI);

        if (result.startsWith("Service added successfully.")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
