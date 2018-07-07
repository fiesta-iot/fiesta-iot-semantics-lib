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

import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.resultset.ResultSetApply;

public class XmlOutput {
	String stylesheetURL = null;
	boolean includeXmlinst = true;

	public XmlOutput() {
	}

	public XmlOutput(String stylesheetURL) {
		setStylesheetURL(stylesheetURL);
	}

	public XmlOutput(boolean includeXmlinst) {
		setIncludeXmlinst(includeXmlinst);
	}

	public XmlOutput(boolean includeXmlinst, String stylesheetURL) {
		setStylesheetURL(stylesheetURL);
		setIncludeXmlinst(includeXmlinst);
	}

	public void format(OutputStream out, ResultSet resultSet) {
		XmlOutputResultSet xOut = new XmlOutputResultSet(out);
		xOut.setStylesheetURL(stylesheetURL);
		xOut.setXmlInst(includeXmlinst);
		ResultSetApply a = new ResultSetApply(resultSet, xOut);
		a.apply();
	}

	public void format(OutputStream out, boolean booleanResult) {
		XmlOutputBoolean xOut = new XmlOutputBoolean(out);
		xOut.exec(booleanResult);
	}

	/** @return Returns the includeXmlinst. */
	public boolean getIncludeXmlinst() {
		return includeXmlinst;
	}

	/**
	 * @param includeXmlinst
	 *            The includeXmlinst to set.
	 */
	public void setIncludeXmlinst(boolean includeXmlinst) {
		this.includeXmlinst = includeXmlinst;
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
}