package ru.nsu.xsld;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.w3c.dom.Node;
import ru.nsu.xsld.interpreters.ErrorInterpreter;
import ru.nsu.xsld.interpreters.PredicateInterpreter;
import ru.nsu.xsld.interpreters.XsldErrorListener;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing XSLD schema
 */
public class XSLD {

    private RuleChecker ruleChecker;

    /**
     * Creates XSLD schema from file
     * @param file file containing XSLD definition
     * @throws XsldException if file provided is not XSLD
     * @throws IOException
     */
    public XSLD(@NotNull File file)  throws XsldException, IOException{
        throw new RuntimeException("Not implemented"); //TODO
    }


    /**
     * Creates XSLD schema from XML node root
     * @param node node used as root for XSLD schema
     * @throws XsldException if node provided is not XSLD
     */
    public XSLD(@NotNull Node node) throws XsldException {
        throw new RuntimeException("Not implemented"); //TODO
    }

    /**
     * Extracts XSD schema from XSLD schema and writes to stream
     * @param outputStream stream used for writing resulting XSD
     */
    public void extractXsd(@NotNull OutputStream outputStream) {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new StreamSource(new File("extractor.xsl")), new StreamResult(outputStream));
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
    public boolean verify(@NotNull File file, @Nullable XsldErrorListener listener) throws IOException{
        throw new RuntimeException("Not implemented"); //TODO
    }

    /**
     * Verifies XML node against this XSLD schema
     * @param node root for XML file to match with schema
     * @param listener error listener. Can be null
     * @return true if XML matches schema
     */
    public boolean verify(@NotNull Node node, @Nullable XsldErrorListener listener){
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
}
