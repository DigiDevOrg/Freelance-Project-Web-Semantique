package com.example.websemantiquefreelance;

import com.example.websemantiquefreelance.services.CategoryService;
import jdk.jfr.Category;
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
@RequestMapping("/category")
public class CategoryRestApi {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/all")
    public List<Map<String, String>> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/details")
    public List<Map<String, String>> getCategoryDetails(@RequestParam String CategoryURI) {
        return categoryService.DetailsCategory(CategoryURI);
    }
    @PostMapping
    @ResponseStatus
    public ResponseEntity<?> add(@RequestParam String categoryName, @RequestParam String categoryDesc) {
        System.out.println(categoryName + " " +categoryDesc);
        return ResponseEntity.ok(categoryService.addCategory(categoryName, categoryDesc));
    }

    @DeleteMapping("/deleteCategory")
    public ResponseEntity<String> deleteGroup(@RequestParam("category") String category) {
        Model model = ModelFactory.createDefaultModel();
        model.read("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf");
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);
        Individual categoryIndividual = ontModel.getIndividual(category);

        if (categoryIndividual != null) {

            categoryIndividual.remove();
            try (OutputStream outputStream = new FileOutputStream("src/main/java/com/example/websemantiquefreelance/Freelancing.rdf")) {
                ontModel.write(outputStream, "RDF/XML-ABBREV");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the category.");
            }

            return ResponseEntity.status(HttpStatus.OK).body("category deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("category not found.");
        }
    }

    @GetMapping("/CategoryByName")
    public List<Map<String, String>> getCtegoryByName (@RequestParam String CategoryName) {
        return categoryService.CategoryByName(CategoryName);
    }
    @GetMapping("/search")
    public List<Map<String, String>> searchCategory(@RequestParam String keyword) {
        return categoryService.SearchCategory(keyword);
    }

}
