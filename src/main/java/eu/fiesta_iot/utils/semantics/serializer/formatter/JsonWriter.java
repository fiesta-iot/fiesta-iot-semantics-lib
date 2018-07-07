/*******************************************************************************
 * Copyright (c) 2018 Jorge Lanza, 
 *                    David Gomez, 
 *                    Luis Sanchez,
 *                    Juan Ramon Santana
 *
 * For the full copyright and license information, please view the LICENSE
 * file that is distributed with this source code.
 *******************************************************************************/
package eu.fiesta_iot.utils.semantics.serializer.formatter;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFErrorHandler;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.RDFDefaultErrorHandler;
import org.apache.jena.shared.UnknownPropertyException;
import org.apache.jena.util.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonWriter implements RDFWriter {

	protected static Logger logger = LoggerFactory.getLogger(JsonWriter.class);

	RDFErrorHandler errorHandler = new RDFDefaultErrorHandler();

	IndentedWriter out;
	
	public JsonWriter() {
	}

	@Override
	public Object setProperty(String propName, Object propValue) {
		throw new UnknownPropertyException(propName);
	}

	@Override
	public void write(Model model, OutputStream out, String base) {
		try {
			Writer w;
			try {
				w = new OutputStreamWriter(out, "ascii");
			} catch (UnsupportedEncodingException e) {
				logger.warn("ASCII is not supported: in NTripleWriter.write",
				            e);
				w = FileUtils.asUTF8(out);
			}
			write(model, w, base);
			w.flush();

		} catch (Exception ioe) {
			errorHandler.error(ioe);
		}
	}

	@Override
	public void write(Model model, Writer writer, String base) {
		try {
			PrintWriter pw;
			if (writer instanceof PrintWriter) {
				pw = (PrintWriter) writer;
			} else {
				pw = new PrintWriter(writer);
			}
			
			pw.println("{");
			pw.println("\t\"vars\": [");
			pw.println("\t\t\"subject\", \"predicate\", \"object\"");
			pw.println("\t],");
			
			pw.println("\t\"items\": [");
			StmtIterator iter = model.listStatements();
			Statement stmt = null;
			while (iter.hasNext()) {
				stmt = iter.nextStatement();
				pw.println("\t\t{");
				writeSubject(stmt, pw);
				pw.println(",");
				writePredicate(stmt, pw);
				pw.println(",");
				writeObject(stmt, pw);
				pw.println();
				pw.print("\t\t}");
				if (iter.hasNext()) {
					pw.print(",");
				}
				pw.println();
			}
			pw.println("\t]");
			pw.println("}");
			pw.flush();
		} catch (Exception e) {
			errorHandler.error(e);
		}
	}
	
	@Override
	public RDFErrorHandler setErrorHandler(RDFErrorHandler errHandler) {
		RDFErrorHandler old = this.errorHandler;
		this.errorHandler = errHandler;
		return old;
	}

	protected static void writeSubject(Statement stmt, PrintWriter pw) {
		write("subject", stmt.getSubject(), pw);
	}
	
	protected static void writePredicate(Statement stmt, PrintWriter pw) {
		write("predicate", stmt.getPredicate(), pw);
	}
	
	protected static void writeObject(Statement stmt, PrintWriter pw) {
		write("object", stmt.getObject(), pw);
	}
	
	protected static void write(String stmt, RDFNode r, PrintWriter pw) {
		pw.print("\t\t\t\"" + stmt + "\": ");
		writeRdfNode(r, pw);
	}
	
	
	protected static void writeRdfNode(RDFNode r, PrintWriter writer) {
		writer.print(quote(r.toString()));
	}

	private static String quote(String string) {
		return JSONObject.quote(string);
	}
}
