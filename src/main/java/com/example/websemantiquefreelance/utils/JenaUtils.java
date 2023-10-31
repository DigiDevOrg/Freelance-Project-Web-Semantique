package com.example.websemantiquefreelance.utils;



import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class JenaUtils {
    private static JenaUtils _instance = null;
    public Model model;
    public OntModel ontModel;
    private QueryExecution queryExec;
    private String rdfFile = "src/main/java/org/example/Freelancing.rdf";

    public Dataset dataset;

    public static String getPrefix() {
        return "untitled-ontology-9";
    }

    public JenaUtils() {

        dataset = DatasetFactory.createTxnMem();
        model = ModelFactory.createDefaultModel();
        model.read(rdfFile);
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);

    }

    public static JenaUtils get() {
        if (_instance == null) {
            _instance = new JenaUtils();
        }
        return _instance;
    }

    public List<Map<String, String>> executeSelect(String selectQuery, List<List<String>> fields) {
        queryExec = QueryExecutionFactory.create(selectQuery, ontModel);
        ResultSet results = queryExec.execSelect();
        List<Map<String, String>> resultList = new ArrayList<>();
        while (results.hasNext()) {
            Map<String, String> resultMap = new HashMap<>();
            QuerySolution solution = results.nextSolution();
            for (List<String> field : fields) {
                // check for null
                if (solution.get(field.get(0)) != null) {
                    resultMap.put(field.get(1), solution.get(field.get(0)).toString());
                }
            }
            resultList.add(resultMap);
        }
        queryExec.close();
        return resultList;
    }

    public String executeInsert(String insertQuery) {
        try {
            dataset.begin(ReadWrite.WRITE);

            // Get the default model from the dataset for insert
            Model model = dataset.getDefaultModel();

            // Execute the insert query
            UpdateRequest updateRequest = UpdateFactory.create(insertQuery);
            UpdateAction.execute(updateRequest, model);

            // Commit the transaction
            dataset.commit();

            // Return success message
            return "INSERT query executed successfully.";
        } catch (Exception e) {
            // Rollback the transaction in case of an exception
            dataset.abort();
            return "Failed to execute the INSERT query: " + e.getMessage();
        } finally {
            // End the transaction
            dataset.end();
        }
    }
}
