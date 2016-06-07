package ru.nsu.xsld;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ru.nsu.xsld.interpreters.ErrorInterpreter;
import ru.nsu.xsld.interpreters.PredicateInterpreter;
import ru.nsu.xsld.interpreters.ErrorListener;
import ru.nsu.xsld.paths.XsldParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class representing XSLD schema
 */
public class XSLD {

    //TODO remove
    public static void main(String[] args) throws Exception {
        new XSLD(new File("res/test.xml"));
    }

    private RuleChecker ruleChecker;

    /**
     * Creates XSLD schema from file
     * @param file file containing XSLD definition
     * @throws XsldException if file provided is not XSLD
     * @throws IOException
     */
    public XSLD(@NotNull File file) throws XsldException, IOException, SAXException, ParserConfigurationException {
        this(nodeFromFile(file));
    }


    /**
     * Creates XSLD schema from XML node root
     * @param element node used as root for XSLD schema
     * @throws XsldException if node provided is not XSLD
     */
    public XSLD(@NotNull Element element) throws XsldException {
        XsldParser parser = new XsldParser(element);

    }

    /**
     * Extracts XSD schema from XSLD schema and writes to stream
     * @param outputStream stream used for writing resulting XSD
     */
    public void extractXsd(@NotNull OutputStream outputStream) {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new StreamSource(new File("res/extractor.xsl")), new StreamResult(outputStream));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Verifies file against this XSLD schema
     * @param file file to match with schema
     * @param listener error listener. Can be null
     * @throws IOException
     * @return true if file matches schema
     */
    public boolean verify(@NotNull File file, @Nullable ErrorListener listener) throws IOException, SAXException, ParserConfigurationException {
        return verify(nodeFromFile(file), listener);
    }

    /**
     * Verifies XML node against this XSLD schema
     * @param element root for XML file to match with schema
     * @param listener error listener. Can be null
     * @return true if XML matches schema
     */
    public boolean verify(@NotNull Element element, @Nullable ErrorListener listener){
        throw new RuntimeException("Not implemented"); //TODO
    }

    /**
     * Add error interpreter to schema
     */
    public void addErrorInterpreter(ErrorInterpreter interpreter){
        ruleChecker.addErrorInterpreter(interpreter);
    }

    /**
     * Add predicate interpreter to schema
     */
    public void addPredicateInterpreter(PredicateInterpreter interpreter){
        ruleChecker.addPredicateInterpreter(interpreter);
    }

    private static Element nodeFromFile(File file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(file).getDocumentElement();
    }
}
