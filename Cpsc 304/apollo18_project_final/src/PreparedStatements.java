import java.util.*;

/*

Idea behind this class is to provide API calls for queries our database will support.
*/

public class PreparedStatements {

    // Test cases for functions
    public static void main(String[] args) {
        // Test setup
        ArrayList<String> relations = new ArrayList<String>();
        ArrayList<String> attributes = new ArrayList<String>();
        ArrayList<String> predicates = new ArrayList<String>();
        relations.add("relation1");
        relations.add("relation2");
        relations.add("relation3");
        attributes.add("attribute1");
        attributes.add("attribute2");
        attributes.add("attribute3");
        predicates.add("predicate1");
        predicates.add("predicate2");
        predicates.add("predicate3");

        // Selection Test
        System.out.println(Selection("attr", "relation", "predicate"));

        // Join Test
        System.out.println(Join("attr", relations, "predicate"));

        // Division Test
        System.out.println(Division(attributes, relations, predicates));

        // Aggregation Test
        System.out.println(Aggregation("MAX", "attr", "relation", "predicate"));
    }

    public static String Selection (String attribute, String relation, String predicate) {
        return "SELECT " + attribute + " FROM " + relation + " WHERE " + predicate;
    }

    public static String Join (String attribute, ArrayList<String> relations, String predicate) {
        String req;
        int size = relations.size();

        req = "SELECT " + attribute + " FROM ";
        for(int i = 0; i < size; i++) {
            req += relations.get(i);
            if(i < size - 1) {
                req += ", ";
            }
        }

        req += " WHERE " + predicate;

        return req;
    }

    /* Size of attributes, relations, and predicates must all be equal for this function to work*/
    public static String Division (ArrayList<String> attributes, ArrayList<String> relations, ArrayList<String> predicates) {
        int numDivisions = attributes.size(); // could also use relations.size() or predicates.size()
        String req = "";

        for(int i = 0; i < numDivisions; i++) {
            req += Selection(attributes.get(i), relations.get(i), predicates.get(i));
            if( i < numDivisions - 1) {
                req += " (";
            }
        }

        for(int i = 0; i < numDivisions - 1; i++) {
            req += ")";
        }

        return req;
    }

    public static String Aggregation(String Function, String attribute, String relation, String predicate) {
        return Selection(Function + "(" + attribute + ")", relation, predicate);
    }
}