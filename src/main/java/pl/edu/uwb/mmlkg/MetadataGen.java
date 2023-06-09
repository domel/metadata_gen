package pl.edu.uwb.mmlkg;

import com.electronwill.nightconfig.core.file.FileConfig;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class MetadataGen {
    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: metadata-gen <esx_path> <config_file_path>");
        }

        String esx_path = args[0];
        String configFilePath = args[1];

        // Parse TOML file
        FileConfig config = FileConfig.of(configFilePath);
        config.load();

        System.err.println("Generating metadata...");

        // Create a new XML document
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("mm:metadata").addNamespace("dc", "http://purl.org/dc/terms/").addNamespace("mm", "https://ii.uwb.edu.pl/mizarmetadata/");

        // Add all entries from the config file
        TreeMap<String, Object> sortedConfig = new TreeMap<>(config.valueMap());
        for (Map.Entry<String, Object> entry : sortedConfig.entrySet()) {
            if (entry.getValue() instanceof List<?> values) {
                // add multiple values
                for (Object value : values) {
                    root.addElement("dc:" + entry.getKey()).addText(value.toString());
                }
            } else {
                root.addElement("dc:" + entry.getKey()).addText(entry.getValue().toString());
            }
        }

        // last modified data

        File directory = new File(esx_path);
        File[] files = directory.listFiles();

        Arrays.sort(Objects.requireNonNull(files), (a, b) -> Long.compare(b.lastModified(), a.lastModified()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String latestDate = dateFormat.format(new Date(files[0].lastModified()));
        root.addElement("dc:date").addText(latestDate);

        // Elements to count
        String[] elements = {"Arguments", "Simple-Term", "Infix-Term", "Item", "Local-Reference", "Relation-Formula", "Label", "Proposition", "Straightforward-Justification", "Variable", "Standard-Type", "Regular-Statement", "Numeral-Term", "Link", "Variables", "Explicitly-Qualified-Segment", "Theorem-Reference", "Variable-Segments", "Reserved-Dscr-Type", "Substitutions", "Right-Circumflex-Symbol", "Circumfix-Term", "Scope", "Conjunctive-Formula", "Qualified-Segments", "Universal-Quantifier-Formula", "Adjective-Cluster", "Block", "Conclusion", "Definition-Reference", "Free-Variable-Segment", "Substitution", "Single-Assumption", "Generalization", "Iterative-Step", "Clustered-Type", "Thesis", "Assumption", "Implicitly-Qualified-Segment", "Multi-Attributive-Formula", "Conditions", "Selector-Term", "Restriction", "Equalities-List", "Type-Changing-Statement", "Choice-Statement", "Equality", "Iterative-Equality", "Iterative-Steps-List", "Theorem-Item", "Equating", "Existential-Quantifier-Formula", "Constant-Definition", "Case-Block", "Diffuse-Statement", "Conditional-Formula", "Suppose-Head", "Private-Predicate-Formula", "Private-FunctorTerm", "Negated-Formula", "Default-Generalization", "Loci-Declaration", "Qualifying-Formula", "Loci", "Struct-Type", "Placeholder-Term", "Correctness-Condition", "Locus", "Definition-Item", "Disjunctive-Formula", "Equality-To-Itself", "Exemplification", "Fraenkel-Term", "Per-Cases", "Collective-Assumption", "Coherence", "Implicit-Exemplification", "Biconditional-Formula", "Type-List", "Cluster", "Redefine", "Contradiction", "Definiens", "Scheme-Justification", "Type-Specification", "Infix-Functor-Pattern", "Functorial-Registration", "Functor-Definition", "Private-Predicate-Definition", "It-Term", "Reservation-Segment", "Existence", "Global-Choice-Term", "Reservation", "CaseHead", "Forgetful-Functor-Term", "Private-Functor-Definition", "Section-Pragma", "Qualification-Term", "Uniqueness", "Attribute-Pattern", "Example", "Conditional-Registration", "Aggregate-Term", "Attribute-Definition", "Existential-Assumption", "Existential-Registration", "Exemplifying-Variable", "Correctness", "Correctness-Conditions", "RightSide-Of-Relation-Formula", "Multi-Relation-Formula", "Simple-Fraenkel-Term", "Functor-Segment", "Predicate-Pattern", "Mode-Pattern", "Mode-Definition", "Text-Proper", "Predicate-Definition", "Pragma", "Pattern-Shaped-Expression", "Schematic-Variables", "Scheme", "Scheme-Block-Item", "Scheme-Head", "Expandable-Mode", "Compatibility", "Partial-Definiens", "Selector", "Selector-Functor-Pattern", "Provisional-Formulas", "Properties", "StandardMode", "Property", "Predicate-Segment", "Field-Segment", "Selectors", "Canceled", "Internal-Selector-Term", "Notion-Name", "Otherwise", "Partial-Definiens-List", "Consistency", "Reduction", "Reducibility", "Circumfix-Functor-Pattern", "Flexary-Disjunctive-Formula", "Func-Synonym", "Flexary-Conjunctive-Formula", "Aggregate-Functor-Pattern", "Ancestors", "Field-Segments", "Forgetful-FunctorPattern", "Selectors-List", "Strict-Pattern", "Structure-Definition", "Structure-Pattern", "Structure-Patterns-Rendering", "Identify", "Loci-Equalities", "Loci-Equality", "Attr-Synonym", "Attr-Antonym", "Pred-Antonym", "Pred-Synonym", "Property-Registration", "Mode-Synonym", "Redex", "Reduct"};

        // Map to store element counts
        HashMap<String, Integer> elementCounts = new HashMap<>();
        for (String element : elements) {
            elementCounts.put(element, 0);
        }

        // Directory to scan
        File dir = new File(esx_path);

        // Initialize XML parsing objects
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }

        // Scan directory for XML files
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.getName().endsWith(".esx")) {
                continue;
            }
            try {
                org.w3c.dom.Document doc = builder.parse(file);
                for (String element : elements) {
                    NodeList nodes = doc.getElementsByTagName(element);
                    int count = elementCounts.get(element);
                    elementCounts.put(element, count + nodes.getLength());
                }
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        }

        // Print element counts
        for (String element : elements) {
            root.addElement("mm:" + element.replace("-", "") + "Card").addText(elementCounts.get(element).toString());
        }

        // Print the XML document
        //System.out.println(document.asXML());
        System.out.println(prettyFormat(document.asXML(), 2));

    }
}