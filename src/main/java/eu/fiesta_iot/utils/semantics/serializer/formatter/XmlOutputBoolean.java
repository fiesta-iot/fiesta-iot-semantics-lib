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
import org.apache.jena.query.ResultSet;

public class XmlOutputBoolean implements XmlTags {

	private static final String BOOL_VAR = "boolean";

	String stylesheetURL = null;
	boolean xmlInst = true;

	IndentedWriter out;

	public XmlOutputBoolean(OutputStream outStream) {
		this(new IndentedWriter(outStream));
	}

	public XmlOutputBoolean(IndentedWriter indentedOut) {
		out = indentedOut;
	}

	public void exec(boolean result) {
		if (xmlInst) {
			out.println("<?xml version=\"1.0\"?>");
		}

		if (stylesheetURL != null) {
			out.println("<?xml-stylesheet type=\"text/xsl\" href=\""
			            + stylesheetURL + "\"?>");
		}

		out.print("<" + ROOT_TAG);
		out.println("xmlns=\"" + NAMESPACE + "\">");
		out.incIndent();
		
		doVars();

		doData(result);
		
		out.decIndent();
		out.println("</" + ROOT_TAG + ">");
		out.flush();
	}

	private void doVars() {
		out.println("<" + VARS_TAG + ">");
		out.incIndent();
		out.print("<" + VAR_TAG + ">");
		out.print(BOOL_VAR);
		out.print("</" + VAR_TAG + ">");
		out.decIndent();
		out.println("</" + VARS_TAG + ">");
	}
	
	private void doData(boolean result) {
		out.println("<" + ITEMS_TAG + ">");
		out.incIndent();
		out.print("<" + ITEM_TAG + " type=\"boolean\">");
		out.print(Boolean.toString(result));
		out.println("</" + ITEM_TAG + ">");
		out.decIndent();
		out.println("</" + VARS_TAG + ">");
	}
	
	
}
