package com.example.websemantiquefreelance.services;

import com.example.websemantiquefreelance.utils.JenaUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {
    String prefix= JenaUtils.getPrefix();
    public List<?> getAll() {
        String query = "SELECT ?individual ?orderStatus ?orderDate \n" +
                "WHERE {\n" +
                " ?individual a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Order>.\n" +
                " OPTIONAL { ?individual <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#orderStatus> ?orderStatus }\n"
                +
                " OPTIONAL { ?individual <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#orderDate> ?orderDate }\n"
             +
                "}";

        List<List<String>> fields = new ArrayList<>();
        // fill array on creation
        fields.add(new ArrayList<String>() {
            {
                add("orderStatus");
                add("orderStatus");
            }
        });
        fields.add(new ArrayList<String>() {
            {
                add("orderDate");
                add("orderDate");
            }
        });
        fields.add(new ArrayList<String>() {
            {
                add("pageUrl");
                add("pageUrl");
            }
        });
        fields.add(new ArrayList<String>() {
            {
                add("likesCount");
                add("likesCount");
            }
        });
        fields.add(new ArrayList<String>() {
            {
                add("name");
                add("name");
            }
        });

        return JenaUtils.get().executeSelect(query, fields);
    }
    @DeleteMapping("/deleteOrder")
    public ResponseEntity<String> deleteOrder(@RequestParam("orderId") String orderId) {
        Model model = ModelFactory.createDefaultModel();
        model.read("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf");

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);

        Individual orderIndividual = ontModel.getIndividual(orderId);

        if (orderIndividual != null) {
            orderIndividual.remove();

            try (OutputStream outputStream = new FileOutputStream("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf")) {
                ontModel.write(outputStream, "RDF/XML-ABBREV");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the order.");
            }

            return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        }
    }


}
