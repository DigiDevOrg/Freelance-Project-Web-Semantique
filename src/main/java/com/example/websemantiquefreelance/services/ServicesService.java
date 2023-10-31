package com.example.websemantiquefreelance.services;

import com.example.websemantiquefreelance.utils.JenaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public class ServicesService {
    JenaUtils jenaUtils;

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
        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }

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
        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }

    public List<Map<String, String>> getServiceDetails(@RequestParam String serviceURI) {
        String sparqlQuery = "SELECT ?property ?value WHERE {<" + serviceURI + "> ?property ?value.}";
        List<List<String>> fields = List.of(
                List.of("property", "property"),
                List.of("value", "value")
        );
        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }

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
         return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }

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
        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }

    public String addService(String serviceName, String serviceDescription, String servicePrice, String freelancerURI) {
        try {
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

            // Execute the SPARQL INSERT query to add the new service
            jenaUtils.executeInsert(sparqlInsert);

            return "Service added successfully.";
        } catch (Exception e) {
            // Handle any exceptions that may occur during the insertion process
            return "Failed to add the service. Error: " + e.getMessage();
        }
    }


}
