package com.example.websemantiquefreelance.services;

import com.example.websemantiquefreelance.utils.JenaUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {

    String prefix = JenaUtils.getPrefix();
    public List<?> getAll() {
        String query = "SELECT ?Category ?CategoryName ?CategoryDesc WHERE {\n" +
                "  ?Category a <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#Category>.\n"
                +
                "  ?Category <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#CategoryName> ?CategoryName.\n"
                +
                "  OPTIONAL { ?Category <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-9#CategoryDesc> ?CategoryDesc.\n"
                +
                "}";

        List<List<String>> fields = new ArrayList<>();
        fields.add(new ArrayList<String>() {
            {
                add("CategoryDesc");
                add("CategoryDesc");
            }
        });

        return JenaUtils.get().executeSelect(query, fields);
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

        return JenaUtils.get().executeInsert(query);
    }



}
