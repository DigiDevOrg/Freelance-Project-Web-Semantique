package com.example.websemantiquefreelance.services;

import com.example.websemantiquefreelance.utils.JenaUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public List<?> getAll() {
        String query = "SELECT ?individual ?bio ?email ?username\n" +
                "WHERE {\n" +
                "  ?individual a ?type.\n" +
                "  FILTER (?type = <http://www.semanticweb.org/inès/ontologies/2023/9/untitled-ontology-2#Member> || ?type = <http://www.semanticweb.org/inès/ontologies/2023/9/untitled-ontology-2#Administrator>).\n"
                +
                "  OPTIONAL { ?individual <http://www.semanticweb.org/inès/ontologies/2023/9/untitled-ontology-2#bio> ?bio }\n"
                +
                "  OPTIONAL { ?individual <http://www.semanticweb.org/inès/ontologies/2023/9/untitled-ontology-2#email> ?email }\n"
                +
                "  OPTIONAL { ?individual <http://www.semanticweb.org/inès/ontologies/2023/9/untitled-ontology-2#username> ?username }\n"
                +
                "}";

        List<List<String>> fields = new ArrayList<>();
        // fill array on creation
        fields.add(new ArrayList<String>() {
            {
                add("bio");
                add("bio");
            }
        });
        fields.add(new ArrayList<String>() {
            {
                add("email");
                add("email");
            }
        });
        fields.add(new ArrayList<String>() {
            {
                add("username");
                add("username");
            }
        });

        return JenaUtils.get().executeSelect(query, fields);
    }

}
