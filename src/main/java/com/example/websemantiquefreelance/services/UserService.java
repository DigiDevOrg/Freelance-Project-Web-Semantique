package com.example.websemantiquefreelance.services;

import com.example.websemantiquefreelance.utils.JenaUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public List<?> getAll() {
        String query = "SELECT ?individual ?firstname ?lastname ?email ?username\n" +
                "WHERE {\n" +
                "  ?individual a ?type.\n" +
                "  FILTER (?type = <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#Client> || ?type = <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#Freelancer> || ?type = <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#Admin>).\n"
                +
                "  OPTIONAL { ?individual <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#firstname> ?firstname }\n"
                +
                "  OPTIONAL { ?individual <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#lastname> ?lastname }\n"
                +
                "  OPTIONAL { ?individual <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#email> ?email }\n"
                +
                "  OPTIONAL { ?individual <http://www.semanticweb.org/mahdi/ontologies/2023/9/untitled-ontology-2#username> ?username }\n"
                +
                "}";

        List<List<String>> fields = new ArrayList<>();
        // fill array on creation
        fields.add(new ArrayList<String>() {
            {
                add("firstname");
                add("firstname");
            }
        });
        fields.add(new ArrayList<String>() {
            {
                add("lastname");
                add("lastname");
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
