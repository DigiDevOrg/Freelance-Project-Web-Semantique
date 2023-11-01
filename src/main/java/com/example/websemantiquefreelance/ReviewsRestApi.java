package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.ReviewsService;
import com.example.websemantiquefreelance.utils.JenaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/review")
public class ReviewsRestApi {

    private ReviewsService reviewsService;
    JenaUtils jenaUtils;
    @GetMapping("/all")
        public List<Map<String, String>> getAllReviews() {
            String sparqlQuery = "SELECT ?review ?reviewDate ?reviewRating ?reviewText " +
                    "WHERE {" +
                    "  ?review a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Reviews>." +
                    "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewDate> ?reviewDate." +
                    "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewRating> ?reviewRating." +
                    "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewText> ?reviewText." +
                    "}";
            List<List<String>> fields = List.of(
                    List.of("review", "reviewURI"),
                    List.of("reviewDate", "reviewDate"),
                    List.of("reviewRating", "reviewRating"),
                    List.of("reviewText", "reviewText")
            );
            return jenaUtils.get().executeSelect(sparqlQuery, fields);
        }

    @GetMapping("/byRating")
    public List<Map<String, String>> getByRating(@RequestParam("rating") Integer rating) {
        String sparqlQuery = "SELECT ?review ?reviewDate ?reviewRating ?reviewText " +
                "WHERE {" +
                "  ?review a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Reviews>." +
                "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewDate> ?reviewDate." +
                "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewRating> <" + rating + ">." +
                "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewText> ?reviewText." +
                "}";
        List<List<String>> fields = List.of(
                List.of("review", "reviewURI"),
                List.of("reviewDate", "reviewDate"),
                List.of("reviewRating", "reviewRating"),
                List.of("reviewText", "reviewText")
        );
        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }
    @GetMapping("/byText")
    public List<Map<String, String>> getByText(@RequestParam("text") String text) {
        String sparqlQuery = "SELECT ?review ?reviewDate ?reviewRating ?reviewText " +
                "WHERE {" +
                "  ?review a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Reviews>." +
                "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewDate> ?reviewDate." +
                "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewRating> ?reviewRating." +
                "  ?review <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewText> <" + text + ">." +
                "}";
        List<List<String>> fields = List.of(
                List.of("review", "reviewURI"),
                List.of("reviewDate", "reviewDate"),
                List.of("reviewRating", "reviewRating"),
                List.of("reviewText", "reviewText")
        );
        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }
        @GetMapping("/details")
        public List<Map<String, String>> getReviewDetails(@RequestParam String reviewURI) {
            String sparqlQuery = "SELECT ?property ?value WHERE {<" + reviewURI + "> ?property ?value.}";
            List<List<String>> fields = List.of(
                    List.of("property", "property"),
                    List.of("value", "value")
            );
            return jenaUtils.executeSelect(sparqlQuery, fields);
        }

        @GetMapping("/search")
        public List<Map<String, String>> searchReviews(@RequestParam String keyword) {
            String sparqlQuery = "SELECT ?review ?reviewDate ?reviewRating ?reviewText " +
                    "WHERE {" +
                    "  ?review a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Reviews>." +
                    "  ?review <#reviewDate> ?reviewDate." +
                    "  ?review <#reviewRating> ?reviewRating." +
                    "  ?review <#reviewText> ?reviewText." +
                    "  FILTER (regex(?reviewDate, '" + keyword + "', 'i') || regex(?reviewRating, '" + keyword + "', 'i'))." +
                    "}";
            List<List<String>> fields = List.of(
                    List.of("review", "reviewURI"),
                    List.of("reviewDate", "reviewDate"),
                    List.of("reviewRating", "reviewRating"),
                    List.of("reviewText", "reviewText")
            );
            return jenaUtils.executeSelect(sparqlQuery, fields);
        }




    @PostMapping("/addReview")
    public ResponseEntity<String> addReview(
            @RequestParam String reviewDate,
            @RequestParam String reviewRating,
            @RequestParam String reviewText,
            @RequestParam String freelancerURI // The URI of the Freelancer to associate the service with
    ) {
        // Generate a new service URI or use a predefined one if applicable
        String newReviewURI = "http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#newReviewURI"; // Replace with the actual URI

        // Construct the SPARQL INSERT query to add a new service
        String sparqlInsert = "INSERT DATA { " +
                "<" + newReviewURI + "> a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Reviews> ; " +
                "    <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewDate>  '" + reviewDate + "' ; " +
                "    <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#reviewRating> '" + reviewRating + "' ; " +
                "    <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Reviews> '" + reviewText + "' ; " +
                "}";

        try {
            // Execute the SPARQL INSERT query to add the new service
            jenaUtils.executeInsert(sparqlInsert);

            return new ResponseEntity<>("Review added successfully.", HttpStatus.OK);
        } catch (Exception e) {
            // Handle any exceptions that may occur during the insertion process
            return new ResponseEntity<>("Failed to add the Review. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
