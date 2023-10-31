package com.example.websemantiquefreelance.services;

import com.example.websemantiquefreelance.utils.JenaUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {


    JenaUtils jenaUtils;
    String prefix = jenaUtils.getPrefix();

    public List<Map<String, String>> getAll() {
        String sparqlQuery = "SELECT ?Category ?CategoryName ?CategoryDesc " +
                "WHERE {" +
                "  ?Category a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Category>." +
                "  ?Category <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#CategoryName> ?CategoryName." +
                "  ?Category <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#CategoryDesc> ?CategoryDesc." +
                "}";
        List<List<String>> fields = List.of(
                List.of("Category", "CategoryURI"),
                List.of("CategoryName", "CategoryName"),
                List.of("CategoryDesc", "CategoryDesc")
        );
        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }

    public String addCategory(String categoryName, String categoryDesc ) {
        String query = "PREFIX untitled-ontology-2: <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n\n" +
                "INSERT DATA { \n" +
                prefix + ":newCategory a " + prefix + ":newCategory ; \n" +
                prefix + ":categoryName \"" + categoryName + "\"^^xsd:string ;\n" +
                prefix + ":CategoryDesc \"" + categoryDesc + "\" ;\n" +
                "}";

        return jenaUtils.get().executeInsert(query);
    }

    public List<Map<String, String>> DetailsCategory (@RequestParam String CategoryURI) {
        String sparqlQuery = "SELECT ?property ?value WHERE {<" + CategoryURI + "> ?property ?value.}";
        List<List<String>> fields = List.of(
                List.of("property", "property"),
                List.of("value", "value")
        );
        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }


    public List<Map<String, String>> CategoryByName(@RequestParam String CategoryName) {

        String sparqlQuery = "SELECT ?categoryURI ?CategoryName ?CategoryDesc WHERE {"
                + "?categoryURI <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#CategoryName> ?CategoryName ."
                + "?categoryURI <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#CategoryDesc> ?CategoryDesc ."
                + "FILTER(?CategoryName = \"" + CategoryName + "\")"
                + "}";

        List<List<String>> fields = List.of(
                List.of("categoryURI", "categoryURI"),
                List.of("CategoryName", "CategoryName"),
                List.of("CategoryDesc", "CategoryDesc")
        );

        return jenaUtils.get().executeSelect(sparqlQuery, fields);
    }

    public List<Map<String, String>> SearchCategory(String keyword) {
        String sparqlQuery = "SELECT ?categoryURI ?CategoryName ?CategoryDesc " +
                "WHERE {" +
                "  ?Category a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Category>." +
                "  ?Category <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#CategoryName> ?CategoryName." +
                "  ?Category <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#CategoryDesc> ?CategoryDesc." +
                "  FILTER (regex(?CategoryName, '" + keyword + "', 'i') || regex(?CategoryDesc, '" + keyword + "', 'i'))." +
                "}";
        List<List<String>> fields = List.of(
                List.of("categoryURI", "categoryURI"),
                List.of("CategoryName", "CategoryName"),
                List.of("CategoryDesc", "CategoryDesc")
        );

        System.out.println("SPARQL QUERY: " + sparqlQuery);
        System.out.println(keyword);
        List<Map<String, String>> results = jenaUtils.get().executeSelect(sparqlQuery, fields);


        return results;
    }




}
