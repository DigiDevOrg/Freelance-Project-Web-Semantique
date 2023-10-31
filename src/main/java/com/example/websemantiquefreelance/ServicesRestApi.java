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
    private ServicesService servicesService;
        private final JenaUtils jenaUtils;



    public ServicesRestApi(ServicesService servicesService, JenaUtils jenaUtils) {
        this.servicesService = servicesService;
        this.jenaUtils = jenaUtils;
    }

    @GetMapping("/all")
        public List<Map<String, String>> getAllServices() {
            String sparqlQuery = "SELECT ?service ?serviceName ?serviceDescription ?servicePrice " +
                    "WHERE {" +
                    "  ?service a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Services>." +
                    "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#serviceName> ?serviceName." +
                    "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#serviceDescription> ?serviceDescription." +
                    "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#servicePrice> ?servicePrice." +
                    "}";
            List<List<String>> fields = List.of(
                    List.of("service", "serviceURI"),
                    List.of("serviceName", "serviceName"),
                    List.of("serviceDescription", "serviceDescription"),
                    List.of("servicePrice", "servicePrice")
            );
            return jenaUtils.executeSelect(sparqlQuery, fields);
        }

    @GetMapping("/category")
    public List<Map<String, String>> getServicesByCategory(@RequestParam String categoryURI) {
        String sparqlQuery = "SELECT ?service ?serviceName ?serviceDescription ?servicePrice " +
                "WHERE {" +
                "  ?service a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Services>." +
                "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#serviceName> ?serviceName." +
                "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#serviceDescription> ?serviceDescription." +
                "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#servicePrice> ?servicePrice." +
                "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#hasCategory> <" + categoryURI + ">." +
                "}";
        List<List<String>> fields = List.of(
                List.of("service", "serviceURI"),
                List.of("serviceName", "serviceName"),
                List.of("serviceDescription", "serviceDescription"),
                List.of("servicePrice", "servicePrice")
        );
        return jenaUtils.executeSelect(sparqlQuery, fields);
    }

        @GetMapping("/details")
        public List<Map<String, String>> getServiceDetails(@RequestParam String serviceURI) {
            String sparqlQuery = "SELECT ?property ?value WHERE {<" + serviceURI + "> ?property ?value.}";
            List<List<String>> fields = List.of(
                    List.of("property", "property"),
                    List.of("value", "value")
            );
            return jenaUtils.executeSelect(sparqlQuery, fields);
        }

        @GetMapping("/search")
        public List<Map<String, String>> searchServices(@RequestParam String keyword) {
            String sparqlQuery = "SELECT ?service ?serviceName ?serviceDescription ?servicePrice " +
                    "WHERE {" +
                    "  ?service a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Services>." +
                    "  ?service <#serviceName> ?serviceName." +
                    "  ?service <#serviceDescription> ?serviceDescription." +
                    "  ?service <#servicePrice> ?servicePrice." +
                    "  FILTER (regex(?serviceName, '" + keyword + "', 'i') || regex(?serviceDescription, '" + keyword + "', 'i'))." +
                    "}";
            List<List<String>> fields = List.of(
                    List.of("service", "serviceURI"),
                    List.of("serviceName", "serviceName"),
                    List.of("serviceDescription", "serviceDescription"),
                    List.of("servicePrice", "servicePrice")
            );
            return jenaUtils.executeSelect(sparqlQuery, fields);
        }


    @GetMapping("/servicesByFreelancer")
    public List<Map<String, String>> getServicesByFreelancer(@RequestParam String freelancerURI) {
        String sparqlQuery = "SELECT ?service ?serviceName ?serviceDescription ?servicePrice " +
                "WHERE {" +
                "  <" + freelancerURI + "> <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#hasService> ?service." +
                "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#serviceName> ?serviceName." +
                "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#serviceDescription> ?serviceDescription." +
                "  ?service <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#servicePrice> ?servicePrice." +
                "}";
        List<List<String>> fields = List.of(
                List.of("service", "serviceURI"),
                List.of("serviceName", "serviceName"),
                List.of("serviceDescription", "serviceDescription"),
                List.of("servicePrice", "servicePrice")
        );
        return jenaUtils.executeSelect(sparqlQuery, fields);
    }

    @PostMapping("/addService")
    public ResponseEntity<String> addService(
            @RequestParam String serviceName,
            @RequestParam String serviceDescription,
            @RequestParam String servicePrice,
            @RequestParam String freelancerURI // The URI of the Freelancer to associate the service with
    ) {
        // Generate a new service URI or use a predefined one if applicable
        String newServiceURI = "http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#newServiceURI"; // Replace with the actual URI

        // Construct the SPARQL INSERT query to add a new service
        String sparqlInsert = "INSERT DATA { " +
                "<" + newServiceURI + "> a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Services> ; " +
                "    <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#serviceName> '" + serviceName + "' ; " +
                "    <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#serviceDescription> '" + serviceDescription + "' ; " +
                "    <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#servicePrice> '" + servicePrice + "' ; " +
                "    <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#belongsToFreelancer> <" + freelancerURI + "> ." +
                "}";

        try {
            // Execute the SPARQL INSERT query to add the new service
            jenaUtils.executeInsert(sparqlInsert);

            return new ResponseEntity<>("Service added successfully.", HttpStatus.OK);
        } catch (Exception e) {
            // Handle any exceptions that may occur during the insertion process
            return new ResponseEntity<>("Failed to add the service. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
