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

public class XmlOutputResultSet implements ResultSetProcessor, XmlTags {

	String stylesheetURL = null;
	boolean xmlInst = true;

	IndentedWriter out;

	private boolean multiLineVarNames = false;
	private boolean multiLineValues = false;

	XmlOutputResultSet(OutputStream outStream) {
		this(new IndentedWriter(outStream));
	}

	XmlOutputResultSet(IndentedWriter indentedOut) {
		out = indentedOut;
	}

	private boolean firstSolution = true;
	private boolean firstBindingInSolution = true;

	@Override
	public void start(ResultSet rs) {
		if (xmlInst) {
			out.println("<?xml version=\"1.0\"?>");
		}

		if (stylesheetURL != null) {
			out.println("<?xml-stylesheet type=\"text/xsl\" href=\""
			            + stylesheetURL + "\"?>");
		}

		out.print("<" + ROOT_TAG + " xmlns=\"" + NAMESPACE + "\">");

		out.incIndent();
		doVars(rs);
		openTag(ITEMS_TAG, true);
		out.incIndent();
	}

	private void doVars(ResultSet rs) {
		openTag(VARS_TAG, true);
		out.incIndent();
		for (Iterator iter = rs.getResultVars().iterator(); iter.hasNext();) {
			String varname = (String) iter.next();

			openTag(VAR_TAG, false);
			if (multiLineVarNames) {
				out.println();
				out.incIndent();
			}
			out.print(xml_escape(varname));
			if (multiLineVarNames) {
				out.println();
				out.decIndent();
			}
			closeTag(VAR_TAG, true);
		}

		out.decIndent();
		closeTag(VARS_TAG, true);
	}

	@Override
	public void finish(ResultSet res) {
		// Close last binding.
		out.decIndent();
		closeTag(ITEMS_TAG, true);
		out.decIndent();
		closeTag(ROOT_TAG, true);
		out.flush();
	}

	@Override
	public void start(QuerySolution arg0) {
		openTag(ITEM_TAG, true);
		out.incIndent();
	}

	@Override
	public void binding(String name, RDFNode value) {
		if (value == null) {
			return;
		}

		openTag(name, false);
		if (multiLineVarNames) {
			out.println();
			out.incIndent();
		}
		out.print(xml_escape(value.toString()));
		if (multiLineVarNames) {
			out.println();
			out.decIndent();
		}

		closeTag(name, true);
	}

	@Override
	public void finish(QuerySolution arg0) {
		out.decIndent();
		closeTag(ITEM_TAG, true);
	}

	private void openTag(String tag, boolean newline) {
		out.print("<" + tag + ">");
		if (newline) {
			out.println();
		}
	}

	private void closeTag(String tag, boolean newline) {
		out.print("</" + tag + ">");
		if (newline) {
			out.println();
		}
	}

	protected static String xml_escape(String string) {
		String s = string;
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("\r", "&#x0A;"); // Safe - excessively safe
		s = s.replaceAll("\n", "&#x0D;");
		return s;
	}

	/** @return Returns the stylesheetURL. */
	public String getStylesheetURL() {
		return stylesheetURL;
	}

	/**
	 * @param stylesheetURL
	 *            The stylesheetURL to set.
	 */
	public void setStylesheetURL(String stylesheetURL) {
		this.stylesheetURL = stylesheetURL;
	}

	/** @return Returns the xmlInst. */
	public boolean getXmlInst() {
		return xmlInst;
	}

	/**
	 * @param xmlInst
	 *            The xmlInst to set.
	 */
	public void setXmlInst(boolean xmlInst) {
		this.xmlInst = xmlInst;
	}

}
