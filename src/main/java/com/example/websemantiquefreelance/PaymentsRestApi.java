package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/payments")
public class PaymentsRestApi {
    @Autowired
    private PaymentsService paymentsService;
}
