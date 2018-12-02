import java.io.*;

import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

public class SimleQuery {

	static final String inputFileName  = "resultat2.ttl";
	
	public static void main (String args[]) {
		Model model = ModelFactory.createDefaultModel();
    	
		InputStream in = FileManager.get().open( inputFileName );
		if (in == null) {
			throw new IllegalArgumentException( "File: " + inputFileName + " not found");
		}
        
		// read the RDF/XML file
		model.read(in, null, "Turtle");      
        
		//Le nombre de séismes et de morts par pays
        String queryString = "SELECT ?Country (COUNT(?Country) AS ?totalEarthsquake) (SUM(?Deaths2) AS ?totalDeaths){ "
	    		+ "?x <http://purl.org/cerif/frapo/hasCode> ?Earthquake;"
	    		+ "<http://dbpedia.org/ontology#locationCountry> ?Country;"
	    		+ "<http://dbpedia.org/ontology#numberOfDeaths> ?Deaths."
	        	+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?Deaths) AS ?Deaths2)"
    			+ "}"
    			+ "GROUP BY ?Country "
    			+ "ORDER BY DESC(?totalEarthsquake) ";

		//Le nombre de séisme au même épicentre
/*		String queryString = "SELECT ?Latitude ?Longitude (COUNT(?Latitude) AS ?memeLieu) { "
				+ "?x <http://purl.org/cerif/frapo/hasCode> ?Earthquake;"
				+ "<http://www.w3.org/2003/01/geo#lat> ?Latitude;"
				+ "<http://www.w3.org/2003/01/geo#long> ?Longitude."
				+ "}"
				+ "GROUP BY ?Latitude ?Longitude "
				+ "ORDER BY DESC(?memeLieu) "
				+ "LIMIT 500";
*/
		Query query = QueryFactory.create(queryString) ;
		try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
			ResultSet results = qexec.execSelect() ;
			ResultSetFormatter.out(System.out, results, query) ;
		}
	}
}