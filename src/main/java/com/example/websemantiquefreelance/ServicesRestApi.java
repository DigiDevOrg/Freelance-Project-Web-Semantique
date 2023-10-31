package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.ServicesService;
import com.example.websemantiquefreelance.utils.JenaUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

}
