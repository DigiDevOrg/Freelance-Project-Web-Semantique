package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.OrderService;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/order")
public class OrderRestApi {
    @Autowired
    OrderService orderService;
    @GetMapping("/allOrder")
    public List<Map<String, String>> getAll() {
        return orderService.getAll();
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
