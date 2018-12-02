import java.io.*;
import java.lang.Math.*;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

public class MutipleQuery {

    static final String fileEarthquake  = "resultat2.ttl";
    static final String fileTsunami  = "data2.ttl";
	
    public static void main (String args[]) {
    	FileManager.get().addLocatorClassLoader(main.class.getClassLoader());
    	
    	Model earthquake = FileManager.get().loadModel("res.ttl");
    	Model tsunami = FileManager.get().loadModel("data2.ttl");
    	
    	Model model = earthquake.union(tsunami);
	
       InputStream inEarthquake = FileManager.get().open( fileEarthquake );
        if (inEarthquake == null) {
            throw new IllegalArgumentException( "File: " + fileEarthquake + " not found");
        }
        InputStream inTsunami = FileManager.get().open( fileEarthquake );
        if (inTsunami == null) {
            throw new IllegalArgumentException( "File: " + fileEarthquake + " not found");
        }
  

     // read the RDF/XML file
        model.read(inEarthquake, null, "Turtle");
        model.read(inTsunami, null, "Turtle");
        
        String queryString = "SELECT ?year ?month ?day ?country ?death (COUNT (DISTINCT ?earthq) AS ?earthqcount) (COUNT (DISTINCT ?tsu) AS ?tsucount) { "
 				+ "?earthq <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology#Earthquake>;"
 				+ "<http://dbpedia.org/ontology#locationCountry> ?country;"
	    		+ "<http://dbpedia.org/ontology#year> ?year1;"
	    		+ "<http://dbpedia.org/ontology#month> ?month1;"
	    		+ "<http://dbpedia.org/ontology#day> ?day1;"
	    		+ "<http://dbpedia.org/ontology#numberOfDeaths> ?death1."
	    		+ "?tsu <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/page/Tsunami>;"
	    		+ "<http://dbpedia.org/ontology/country> ?country;"
	    		+ "<http://dbpedia.org/ontology/year> ?year1;"
	    		+ "<http://dbpedia.org/ontology/month> ?month1;"
	    		+ "<http://dbpedia.org/ontology/day> ?day1;"
	    		+ "<http://purl.org/vocab/bio/0.1/death> ?death1."
	    		+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?death1) AS ?death)"
	    		+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?year1) AS ?year)"
	    		+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?month1) AS ?month)"
	    		+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?day1) AS ?day)"
	    		+ "FILTER (?year > 1899)"
    			+ "} "
    			+ "GROUP BY ?year ?month ?day ?country ?death "
    			+ "ORDER BY (?year) "
    			+ "LIMIT 20";
/* 		String queryString = "SELECT ?year ?month ?day ?country ?death (COUNT (DISTINCT ?earthq) AS ?earthqcount) ?time (COUNT (DISTINCT ?tsu) AS ?tsucount) ?hour ?minute ?second  { "
				+ "?earthq <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology#Earthquake>;"
				+ "<http://dbpedia.org/ontology#locationCountry> ?country;"
				+ "<http://dbpedia.org/ontology#year> ?year1;"
				+ "<http://dbpedia.org/ontology#month> ?month1;"
				+ "<http://dbpedia.org/ontology#day> ?day1;"
				+ "<http://dbpedia.org/ontology#time> ?time;"
				+ "<http://dbpedia.org/ontology#numberOfDeaths> ?death1."
				+ "?tsu <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/page/Tsunami>;"
				+ "<http://dbpedia.org/ontology/country> ?country;"
				+ "<http://dbpedia.org/ontology/year> ?year1;"
				+ "<http://dbpedia.org/ontology/month> ?month1;"
				+ "<http://dbpedia.org/ontology/day> ?day1;"
				+ "<http://dbpedia.org/page/24-hour_clock> ?hour;"
				+ "<http://dbpedia.org/page/Minute> ?minute;"
				+ "<http://dbpedia.org/page/Second> ?second;"
				+ "<http://purl.org/vocab/bio/0.1/death> ?death1."
				+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?death1) AS ?death)"
				+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?year1) AS ?year)"
				+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?month1) AS ?month)"
				+ "BIND(<http://www.w3.org/2001/XMLSchema#integer>(?day1) AS ?day)"
				+ "FILTER (?year > 1899)"
				+ "} "
				+ "GROUP BY ?year ?month ?day ?country ?time ?hour ?minute ?second ?death "
				+ "ORDER BY DESC(?year) "
				+ "";
*/               
    	Query query = QueryFactory.create(queryString) ;
    	try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
    		ResultSet results = qexec.execSelect() ;
    		ResultSetFormatter.out(System.out, results, query) ;
	    }
    }
}