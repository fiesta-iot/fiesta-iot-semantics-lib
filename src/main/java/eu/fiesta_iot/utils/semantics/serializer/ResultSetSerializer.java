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
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.ResultSetMgr;
import org.apache.jena.riot.resultset.ResultSetLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotSerializeException;
import eu.fiesta_iot.utils.semantics.serializer.formatter.JsonOuput;
import eu.fiesta_iot.utils.semantics.serializer.formatter.XmlOutput;

public class ResultSetSerializer extends Serializer<ResultSet> {
	private static final Logger log =
	        LoggerFactory.getLogger(ResultSetSerializer.class);

	private static boolean init = false;
	protected static List<MediaType> supportedMediaTypes;
	protected static List<Variant> availableVariants;

	static {
		ResultSetSerializer.init();
	}

	protected static void init() {
		if (ResultSetSerializer.init) {
			return;
		}
		ResultSetSerializer rrw = new ResultSetSerializer(null);
		ResultSetSerializer.supportedMediaTypes = rrw.fillSupportedMediaTypes();
		log.debug("SPARQL ResultSet serialization supporting "
		          + ResultSetSerializer.supportedMediaTypes.size()
		          + " media types: "
		          + ResultSetSerializer.supportedMediaTypes.toString());
		ResultSetSerializer.availableVariants =
		        HttpStreamer.getAvailableVariants(supportedMediaTypes);

		ResultSetSerializer.init = true;
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

	public ResultSetSerializer(ResultSet o) {
		super(o);
	}

//	@Override
//	protected String asRDFStream(Lang lang) throws CannotSerializeException {
//
//		// boolean isRdfLang =
//		// Arrays.stream(NON_RDF_LANGS).anyMatch(lang::equals);
//		//
//		// if (lang.equals(ResultSetLang.SPARQLResultSetText)) {
//		// asPlainText();
//		// } else if (lang.equals(ResultSetLang.SPARQLResultSetJSON)) {
//		// return asSparqlJSON();
//		// } else if (lang.equals(ResultSetLang.SPARQLResultSetCSV)) {
//		// return asCSV();
//		// } else if (lang.equals(ResultSetLang.SPARQLResultSetXML)) {
//		// return asSparqlXML();
//		// } else if (lang.equals(ResultSetLang.SPARQLResultSetTSV)) {
//		// return asTSV();
//		// }
//
//		throw new CannotSerializeException();
//	}

	@Override
	protected void
	        asRDFStream(Lang lang,
	                    OutputStream out) throws CannotSerializeException {
		throw new CannotSerializeException();
	}

//	@Override
//	protected String asPlainText() throws CannotSerializeException {
//		return ResultSetFormatter.asText(getInput());
//	}

	@Override
	protected void asPlainText(OutputStream out) throws CannotSerializeException {
		ResultSetFormatter.out(out, getInput());
	}

//	@Override
//	protected String asTSV() throws CannotSerializeException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asTSV(baos);
//		return baos.toString();
//	}

	@Override
	protected void asTSV(OutputStream out) throws CannotSerializeException {
		ResultSetFormatter.outputAsTSV(out, getInput());
	}

//	@Override
//	protected String asCSV() throws CannotSerializeException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asCSV(baos);
//		return baos.toString();
//	}

	@Override
	protected void asCSV(OutputStream out) throws CannotSerializeException {
		ResultSetFormatter.outputAsCSV(out, getInput());
	}
	
//	@Override
//	protected String asSparqlThrift() throws CannotSerializeException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asSparqlThrift(baos);
//		return baos.toString();
//	}

	@Override
	protected void asSparqlThrift(OutputStream out) throws CannotSerializeException {
		ResultSetMgr.write(out, getInput(),
		                   ResultSetLang.SPARQLResultSetThrift);
	}
	
//	@Override
//	protected String asSparqlJSON() throws CannotSerializeException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asSparqlJSON(baos);
//		return baos.toString();
//	}
	
	@Override
	protected void asSparqlJSON(OutputStream out) throws CannotSerializeException {
		ResultSetFormatter.outputAsJSON(out, getInput());
	}
	
//	@Override
//	protected String asSparqlXML() throws CannotSerializeException {
//		return ResultSetFormatter.asXMLString(getInput());
//	}

	@Override
	protected void asSparqlXML(OutputStream out) throws CannotSerializeException {
		ResultSetFormatter.outputAsXML(out, getInput());
	}
	
//	@Override
//	protected String asJSON() throws CannotSerializeException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asJSON(baos);
//		return baos.toString();
//	}

	@Override
	protected void asJSON(OutputStream out) throws CannotSerializeException {
		JsonOuput jOut = new JsonOuput();
		jOut.format(out, getInput());
	}
	
//	@Override
//	protected String asXML() throws CannotSerializeException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asXML(baos);
//		return baos.toString();
//	}
	
	@Override
	protected void asXML(OutputStream out) throws CannotSerializeException {
		XmlOutput xOut = new XmlOutput();
		xOut.format(out, getInput());
	}
}
