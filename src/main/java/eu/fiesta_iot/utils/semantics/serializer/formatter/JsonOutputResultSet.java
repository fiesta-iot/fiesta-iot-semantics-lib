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
import java.util.Iterator;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.resultset.ResultSetProcessor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonOutputResultSet implements ResultSetProcessor, JsonTags {

	private static final Logger log =
	        LoggerFactory.getLogger(JsonOutputResultSet.class);

//	private boolean multiLineValues = false;
	private boolean multiLineVarNames = true;

	private boolean firstSolution = true;
	private boolean firstBindingInSolution = true;

	IndentedWriter out;

	JsonOutputResultSet(OutputStream outStream) {
		this(new IndentedWriter(outStream));
	}

	JsonOutputResultSet(IndentedWriter indentedOut) {
		out = indentedOut;
	}

	private void doVars(ResultSet rs) {
		out.print(quoteName(VARS_STR) + ": [ ");
		if (multiLineVarNames) {
			out.println();
		}
		out.incIndent();
		for (Iterator iter = rs.getResultVars().iterator(); iter.hasNext();) {
			String varname = (String) iter.next();
			out.print("\"" + varname + "\"");
			if (iter.hasNext()) {
				out.print(", ");
			}
			if (multiLineVarNames) {
				out.println();
			}
		}
		out.decIndent();
		out.println(" ],");
	}

	
	@Override
	public void start(ResultSet rs) {
		out.println("{");
		out.incIndent();
		doVars(rs);
		out.println(quoteName(ITEMS_STR) + ": [");
		out.incIndent();
		firstSolution = true;
	}

	@Override
	public void finish(ResultSet res) {
		// Close last binding.
		out.println();
		out.decIndent(); // items
		out.println("]");
		out.decIndent();
		out.println("}"); // top level {}
		out.flush();
	}

	@Override
	public void start(QuerySolution arg0) {
		if (!firstSolution) {
			out.println(",");
		}

		firstSolution = false;
		out.println("{");
		out.incIndent();
		firstBindingInSolution = true;
	}

	@Override
	public void binding(String name, RDFNode value) {
		if (value == null)
			return;

		if (!firstBindingInSolution) {
			out.println(",");
		}
		firstBindingInSolution = false;

		// Do not use quoteName - varName may not be JSON-safe as a bare name.
		out.print(quote(name) + ": " + quote(value.toString()));
	}

	@Override
	public void finish(QuerySolution arg0) {
		out.println();
		out.decIndent();
		out.print("}");
	}

	private static String quote(String string) {
		return JSONObject.quote(string);
	}

	// Quote a name (known to be JSON-safe)
	// Never the RHS of a member entry (for example "false")
	// Some (the Java JSON code for one) JSON parsers accept an unquoted
	// string as a name of a name/value pair.
	private static String quoteName(String string) {
		// Safest to quote anyway.
		return quote(string);
	}
}
