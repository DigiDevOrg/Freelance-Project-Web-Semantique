package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.PaymentsService;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/payments")
public class PaymentsRestApi {
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllGroups(@RequestParam(value = "paymentMethod", required = false) String paymentMethodFilter) {
        Model model = ModelFactory.createDefaultModel();
        model.read("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf");
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);
        String sparqlQuery = "PREFIX ex: <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                "SELECT DISTINCT ?payment ?paymentDate ?paymentAmount ?paymentMethod " +
                "WHERE { " +
                "  ?payment a ?paymentAmount; " +
                "          ex:name ?paymentMethod; " +
                "          ex:paymentMethod ?paymentMethod. " +
                "  OPTIONAL { ?payment a ?subClass. } " +
                (paymentMethodFilter != null ? "FILTER (str(?paymentMethod) = '" + paymentMethodFilter + "')." : "") +
                "}";

        QueryExecution queryExecution = QueryExecutionFactory.create(QueryFactory.create(sparqlQuery), ontModel);
        ResultSet resultSet = queryExecution.execSelect();

        List<Map<String, String>> resultList = new ArrayList<>();
        while (resultSet.hasNext()) {
            QuerySolution solution = resultSet.nextSolution();
            String paymentMethod = solution.get("paymentMethod") != null ? solution.get("paymentMethod").toString() : null;
            String paymentDate = solution.get("paymentDate") != null ? solution.get("paymentDate").toString() : null;
            String paymentAmount = solution.get("paymentAmount") != null ? solution.get("paymentAmount").toString() : null;

            Map<String, String> paymentMap = new HashMap<>();
            if (paymentMethod != null) paymentMap.put("paymentMethod", paymentMethod);
            if (paymentDate != null) paymentMap.put("paymentDate", paymentDate);
            if (paymentAmount != null) paymentMap.put("paymentAmount", paymentAmount);
            resultList.add(paymentMap);
        }

        return ResponseEntity.ok(resultList);
    }
    @PostMapping("/addPayment")
    public ResponseEntity<String> addGroup(@RequestParam("paymentMethod") String paymentMethod, @RequestParam("paymentDate") String paymentDate, @RequestParam("paymentAmount") String paymentAmount) {
        Model model = ModelFactory.createDefaultModel();
        model.read("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf");
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);

        try {
            String groupIndividualURI = "http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#Payment" + System.currentTimeMillis();

        /*    // Determine the class of the group based on the groupType
            String groupClassURI = "";
            if ("public".equalsIgnoreCase(groupType)) {
                groupClassURI = "http://www.semanticweb.org/inès/ontologies/2023/9/untitled-ontology-2#publicGroup";
            } else if ("private".equalsIgnoreCase(groupType)) {
                groupClassURI = "http://www.semanticweb.org/inès/ontologies/2023/9/untitled-ontology-2#privateGroup";
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid group type.");
            }
*/

            Individual groupIndividual = ontModel.createIndividual(groupIndividualURI, ontModel.getOntClass("http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#payment"));

            groupIndividual.addProperty(ontModel.getDatatypeProperty("http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#paymentDate"), paymentDate);
            groupIndividual.addProperty(ontModel.getDatatypeProperty("http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#paymentAmount"), paymentAmount);

            try (OutputStream outputStream = new FileOutputStream("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf")) {
                ontModel.write(outputStream, "RDF/XML-ABBREV");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Payment added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add the payment.");
        }

    }
    @DeleteMapping("/deletePayment")
    public ResponseEntity<String> deleteGroup(@RequestParam("paymentURI") String paymentURI) {
        Model model = ModelFactory.createDefaultModel();
        model.read("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf");
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);
        Individual paymentIndividual = ontModel.getIndividual(paymentURI);

        if (paymentIndividual != null) {

            paymentIndividual.remove();
            try (OutputStream outputStream = new FileOutputStream("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf")) {
                ontModel.write(outputStream, "RDF/XML-ABBREV");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the payment.");
            }

            return ResponseEntity.status(HttpStatus.OK).body("payment deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("payment not found.");
        }
    }
    @PutMapping("/updatePayment")
    public ResponseEntity<String> updateGroup(@RequestParam("PaymentURI") String groupURI, @RequestParam("newAmount") String newAmount) {
        // Load RDF data from a file
        Model model = ModelFactory.createDefaultModel();
        model.read("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf");

        // Create an OntModel that performs inference
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);

        // Find the group individual based on the provided URI
        Individual paymentIndividual = ontModel.getIndividual(groupURI);

        if (paymentIndividual != null) {
            // Update the description of the group
            paymentIndividual.setPropertyValue(ontModel.getDatatypeProperty("http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#paymentAmount"), ontModel.createTypedLiteral(newAmount));

            // Save the updated RDF data to your file or database
            try (OutputStream outputStream = new FileOutputStream("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf")) {
                ontModel.write(outputStream, "RDF/XML-ABBREV");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update the payment.");
            }

            return ResponseEntity.status(HttpStatus.OK).body("payment updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("payment not found.");
        }
    }

}
