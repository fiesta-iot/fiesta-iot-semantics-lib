/*******************************************************************************
 * Copyright (c) 2018 Jorge Lanza, 
 *                    David Gomez, 
 *                    Luis Sanchez,
 *                    Juan Ramon Santana
 *
 * For the full copyright and license information, please view the LICENSE
 * file that is distributed with this source code.
 *******************************************************************************/
package eu.fiesta_iot.utils.semantics.serializer;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotSerializeException;
import eu.fiesta_iot.utils.semantics.serializer.formatter.JsonWriter;

public class ModelSerializer extends Serializer<Model> {
	private static final Logger log =
	        LoggerFactory.getLogger(ModelSerializer.class);

	private static boolean init = false;
	protected static List<MediaType> supportedMediaTypes;
	protected static List<Variant> availableVariants;

	static {
		ModelSerializer.init();
	}

	protected static void init() {
		if (ModelSerializer.init) {
			return;
		}

		ModelSerializer mrw = new ModelSerializer(null);
		ModelSerializer.supportedMediaTypes = mrw.fillSupportedMediaTypes();
		log.debug("Model serialization supporting "
		          + ModelSerializer.supportedMediaTypes.size()
		          + " media types: "
		          + ModelSerializer.supportedMediaTypes.toString());
		ModelSerializer.availableVariants =
		        HttpStreamer.getAvailableVariants(supportedMediaTypes);

		ModelSerializer.init = true;
	}

	public static List<MediaType> getSupportedMediaTypes() {
		return supportedMediaTypes;
	}

	protected List<MediaType> listSupportedMediaTypes() {
		return getSupportedMediaTypes();
	}

	public static List<Variant> getAvailableVariants() {
		return availableVariants;
	}

	public List<Variant> listAvailableVariants() {
		return getAvailableVariants();
	}

	public static MediaType
	        getAcceptableMediaType(List<MediaType> mediaTypes) throws CannotSerializeException {
		return HttpStreamer.getAcceptableMediaType(mediaTypes,
		                                           supportedMediaTypes);
	}

	@Override
	protected MediaType
	        _getAcceptableMediaType(List<MediaType> mediaTypes) throws CannotSerializeException {
		return ResultSetSerializer.getAcceptableMediaType(mediaTypes);
	}

	public ModelSerializer(Model input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	protected String asRDFStream(Lang lang) throws CannotSerializeException {
//		StringWriter out = new StringWriter();
//		getInput().write(out, lang.getName());
//		return out.toString();
//	}

	@Override
	protected void
	        asRDFStream(Lang lang,
	                    OutputStream out) throws CannotSerializeException {
		getInput().write(out, lang.getName());
	}

//	@Override
//	// TODO: Returns log for RiotException [line: 1, col: 1 ] Failed to find a
//	// prefix name or keyword: (0;0x0000)
//	// Should consider solving this issue
//	protected String asPlainText() throws CannotSerializeException {
//		StringWriter out = new StringWriter();
//		getInput().write(out, RDFLanguages.strLangNTriples);
//		return out.toString();
//	}

	@Override
	// TODO: Returns log for RiotException [line: 1, col: 1 ] Failed to find a
	// prefix name or keyword: (0;0x0000)
	// Should consider solving this issue
	protected void
	        asPlainText(OutputStream out) throws CannotSerializeException {
		getInput().write(out, RDFLanguages.strLangNTriples);
	}

//	@Override
//	protected String asTSV() throws CannotSerializeException {
//		return asSparqlJSON();
//	}

	@Override
	protected void asTSV(OutputStream out) throws CannotSerializeException {
		throw new CannotSerializeException();
	}

//	@Override
//	protected String asCSV() throws CannotSerializeException {
//		// We can try to implement as it is really similar to JSON format
//		throw new CannotSerializeException();
//	}

	@Override
	protected void asCSV(OutputStream out) throws CannotSerializeException {
		throw new CannotSerializeException();
	}

//	@Override
//	protected String asSparqlThrift() throws CannotSerializeException {
//		throw new CannotSerializeException();
//	}

	@Override
	protected void
	        asSparqlThrift(OutputStream out) throws CannotSerializeException {
		throw new CannotSerializeException();
	}

//	@Override
//	protected String asSparqlJSON() throws CannotSerializeException {
//		throw new CannotSerializeException();
//		/*	
//				ResultSet resultSet = ResultSetFactory.makeResults(getInput());
//				// Assuming that serialization is done using the same function
//				// This will avoid the need for getMediaType()
//				// return new ResultSetSerializer(resultSet).asSparqlJSON();
//				return new ResultSetSerializer(resultSet).writeAs(getMediaType());
//		*/
//	}

	@Override
	protected void
	        asSparqlJSON(OutputStream out) throws CannotSerializeException {
		throw new CannotSerializeException();
	}

//	@Override
//	protected String asSparqlXML() throws CannotSerializeException {
//		throw new CannotSerializeException();
//	}

	@Override
	protected void
	        asSparqlXML(OutputStream out) throws CannotSerializeException {
		throw new CannotSerializeException();
	}

//	@Override
//	protected String asJSON() throws CannotSerializeException {
//		StringWriter out = new StringWriter();
//		JsonWriter jOut = new JsonWriter();
//		jOut.write(getInput(), out, null);
//		return out.toString();
//	}

	@Override
	protected void asJSON(OutputStream out) throws CannotSerializeException {
		JsonWriter jOut = new JsonWriter();
		jOut.write(getInput(), out, null);
	}

	protected String asXML() throws CannotSerializeException {
		StmtIterator stmts = getInput().listStatements();

		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		sb.append("\n");
		sb.append("<data>\n");
		sb.append("\t<vars>\n");
		sb.append("\t\t<var>subject</var>\n");
		sb.append("\t\t<var>predicate</var>\n");
		sb.append("\t\t<var>object</var>\n");
		sb.append("\t</vars>\n");
		sb.append("\t<items>\n");
		while (stmts.hasNext()) {
			sb.append("\t\t<item>\n");

			Statement stmt = stmts.next();
			Resource s = stmt.getSubject();
			Property p = stmt.getPredicate();
			RDFNode o = stmt.getObject();
			sb.append("\t\t\t<subject>" + s.toString() + "</subject>\n");
			sb.append("\t\t\t<predicate>" + p.toString() + "</predicate>\n");
			sb.append("\t\t\t<object>" + o.toString() + "</object>\n");

			sb.append("\t\t</item>\n");
		}

		sb.append("\t</items>\n");
		sb.append("</data>\n");

		return sb.toString();
	}

	@Override
	protected void asXML(OutputStream out) throws CannotSerializeException {
		String xml = asXML();
		PrintWriter writer = new PrintWriter(out);
		writer.println(xml);
		writer.flush();
		writer.close();
	}

}
