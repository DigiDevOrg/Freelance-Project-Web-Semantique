package com.example.websemantiquefreelance;


import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/payments")
public class PaymentsRestApi {
    private OntModel ontModel;
    public PaymentsRestApi() {
        // Inside the constructor or an initialization method, initialize OntModel
        Model model = ModelFactory.createDefaultModel();
        model.read("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf"); // Replace with your file path

        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllPayments() {
        String sparqlQuery = "PREFIX ex: <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#> " +
                "SELECT ?Payments ?paymentDate ?paymentAmount ?paymentMethod " +
                "WHERE { " +
                "  ?Payments a ex:Payments; " +
                "           ex:paymentDate ?paymentDate; " +
                "           ex:paymentAmount ?paymentAmount; " +
                "           ex:paymentMethod ?paymentMethod. " +
                "}";

        QueryExecution queryExecution = QueryExecutionFactory.create(QueryFactory.create(sparqlQuery), ontModel);

        try {
            ResultSet resultSet = queryExecution.execSelect();
            List<Map<String, String>> resultList = processResultSet(resultSet);
            return ResponseEntity.ok(resultList);
        } finally {
            queryExecution.close();
        }
    }

    private List<Map<String, String>> processResultSet(ResultSet resultSet) {
        List<Map<String, String>> resultList = new ArrayList<>();

        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.nextSolution();
            Map<String, String> paymentMap = new LinkedHashMap<>();

            Resource payment = qs.getResource("Payments");
            RDFNode paymentDate = qs.get("paymentDate");
            RDFNode paymentAmount = qs.get("paymentAmount");
            RDFNode paymentMethod = qs.get("paymentMethod");

            if (payment != null) {
                paymentMap.put("Payment", payment.toString());
            }
            if (paymentDate != null) {
                paymentMap.put("Payment Date", paymentDate.toString());
            }
            if (paymentAmount != null) {
                paymentMap.put("Payment Amount", paymentAmount.toString());
            }
            if (paymentMethod != null) {
                paymentMap.put("Payment Method", paymentMethod.toString());
            }

            resultList.add(paymentMap);
        }

        return resultList;
    }

    @GetMapping("/byDate")
    public ResponseEntity<List<Map<String, String>>> getAllByDate(@RequestParam("paymentDate") String paymentDate) {
        // Construct the SPARQL query to get payments by date
        String sparqlQuery = "PREFIX ex: <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#> " +
                "SELECT ?Payments ?paymentDate ?paymentAmount ?paymentMethod " +
                "WHERE { " +
                "  ?Payments a ex:Payments; " +
                "           ex:paymentDate ?paymentDate; " +
                "           ex:paymentAmount ?paymentAmount; " +
                "           ex:paymentMethod ?paymentMethod. " +
                "  FILTER(?paymentDate = \"" + paymentDate + "\")" +
                "}";

        QueryExecution queryExecution = QueryExecutionFactory.create(QueryFactory.create(sparqlQuery), ontModel);

        try {
            ResultSet resultSet = queryExecution.execSelect();
            List<Map<String, String>> resultList = processResultSet(resultSet);
            return ResponseEntity.ok(resultList);
        } finally {
            queryExecution.close();
        }
    }

    @GetMapping("/byPaymentMethod")
    public ResponseEntity<List<Map<String, String>>> getByPaymentMethod(@RequestParam("paymentMethod") String paymentMethod) {
        // Construct the SPARQL query to get payments by payment method
        String sparqlQuery = "PREFIX ex: <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#> " +
                "SELECT ?payment ?paymentDate ?paymentAmount ?paymentMethod " +
                "WHERE { " +
                "  ?payment a ex:Payments; " +
                "           ex:paymentDate ?paymentDate; " +
                "           ex:paymentAmount ?paymentAmount; " +
                "           ex:paymentMethod \"" + paymentMethod + "\". " +
                "}";

        QueryExecution queryExecution = QueryExecutionFactory.create(QueryFactory.create(sparqlQuery), ontModel);

        try {
            ResultSet resultSet = queryExecution.execSelect();
            List<Map<String, String>> resultList = processResultSet(resultSet);
            return ResponseEntity.ok(resultList);
        } finally {
            queryExecution.close();
        }
    }

    @PostMapping("/addPayment")
    public ResponseEntity<String> addGroup(@RequestParam("paymentMethod") String paymentMethod, @RequestParam("paymentDate") String paymentDate, @RequestParam("paymentAmount") String paymentAmount) {
        Model model = ModelFactory.createDefaultModel();
        model.read("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf");
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);

        try {
            String groupIndividualURI = "http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Payments" + System.currentTimeMillis();


            Individual groupIndividual = ontModel.createIndividual(groupIndividualURI, ontModel.getOntClass("http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#payment"));

            groupIndividual.addProperty(ontModel.getDatatypeProperty("http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#paymentDate"), paymentDate);
            groupIndividual.addProperty(ontModel.getDatatypeProperty("http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#paymentAmount"), paymentAmount);

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
            paymentIndividual.setPropertyValue(ontModel.getDatatypeProperty("http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#paymentAmount"), ontModel.createTypedLiteral(newAmount));

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
