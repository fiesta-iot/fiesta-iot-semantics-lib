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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.resultset.ResultSetLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotSerializeException;

public abstract class Serializer<T> extends HttpStreamer {
	private static final Logger log = LoggerFactory.getLogger(Serializer.class);

	// Default type is text/plain
	private MediaType defaultMediaType = MediaType.TEXT_PLAIN_TYPE;

	private T input;
	private String mediaType;

	public Serializer(T input) {
		this.input = input;
	}

	protected T getInput() {
		return this.input;
	}

	protected String getMediaType() {
		return this.mediaType;
	}

	private void setMediaType(String mediaType) {
		// Remove accept-params (q=*)
		int len = mediaType.indexOf(';');
		this.mediaType = (len != -1) ? mediaType.substring(0, len) : mediaType;
	}

	public void setDefaultMediaType(MediaType mediaType) {
		this.defaultMediaType = mediaType;
	}

	public String write() throws CannotSerializeException {
		return writeAs(defaultMediaType);
	}

	public void write(OutputStream out) throws CannotSerializeException {
		writeAs(out, defaultMediaType);
	}

	protected abstract MediaType
	        _getAcceptableMediaType(List<MediaType> mediaTypes) throws CannotSerializeException;

	public String
	        writeAs(List<MediaType> mediaTypes) throws CannotSerializeException {
		return writeAs(_getAcceptableMediaType(mediaTypes));
	}

	public MediaType
	        writeAs(OutputStream out,
	                List<MediaType> mediaTypes) throws CannotSerializeException {
		MediaType mt = _getAcceptableMediaType(mediaTypes);
		writeAs(out, mt);
		return mt;
	}

	public String writeAs(MediaType type) throws CannotSerializeException {
		return writeAs(type.toString());
	}

	public void writeAs(OutputStream out,
	                    MediaType type) throws CannotSerializeException {
		writeAs(out, type.toString());
	}

	public String writeAs(String type) throws CannotSerializeException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeAs(baos, type);
		return baos.toString();
	}

	public void writeAs(OutputStream out,
	                    String type) throws CannotSerializeException {
		// Store mediaType to be used in functions in case required
		setMediaType(type);

		// RDF Media Types
		Lang lang = null;
		if (HttpOntologyUtils.isValidRdfMediaType(mediaType)
		    && (lang = HttpOntologyUtils.getRdfLanguage(mediaType)) != null) {
			// log.trace("asRDFStream");
			asRDFStream(lang, out);
		} else if (MediaType.TEXT_PLAIN_TYPE.toString().equals(mediaType)) {
			// Format to other media types
			// text/plain
			// log.debug("asPlainText");
			asPlainText(out);
		} else if (MediaType.TEXT_XML_TYPE.toString().equals(mediaType)
		           || MediaType.APPLICATION_XML_TYPE.toString()
		                   .equals(mediaType)) {
			// application/xml
			// log.trace("asXML");
			asXML(out);
		} else if (MediaType.APPLICATION_JSON_TYPE.toString()
		        .equals(mediaType)) {
			// application/json
			// log.trace("asJson");
			asJSON(out);
		} else if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetXML
		        .getAltContentTypes())) {
			// sparql-results+xml
			// log.trace("asSparqlXML");
			asSparqlXML(out);
		} else if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetJSON
		        .getAltContentTypes())) {
			// sparql-results+json
			// log.trace("asSparqlJSON");
			asSparqlJSON(out);
		} else if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetCSV
		        .getAltContentTypes())) {
			// text/csv or text/csv
			// log.trace("asCSV");
			asCSV(out);
		} else if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetTSV
		        .getAltContentTypes())) {
			// text/tsv
			// log.trace("asTSV");
			asTSV(out);
		} else if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetThrift
		        .getAltContentTypes())) {
			// text/tsv
			// log.trace("asSparqlThrift");
			asSparqlThrift(out);
		} else {
			// log.trace("CannotSerializeException");
			throw new CannotSerializeException("Cannot serialize media type "
			                                   + mediaType);
		}
	}

	private static boolean checkMediaType(MediaType mediaType) {
		return checkMediaType(mediaType);
	}

	private static boolean checkMediaType(String mediaType,
	                                      List<String> isPartOf) {
		return isPartOf.stream().anyMatch(mediaType::equals);
	}

	// protected abstract String
	// asRDFStream(Lang lang) throws CannotSerializeException;

	protected abstract void
	        asRDFStream(Lang lang,
	                    OutputStream out) throws CannotSerializeException;

	// protected abstract String asPlainText() throws CannotSerializeException;

	protected abstract void
	        asPlainText(OutputStream out) throws CannotSerializeException;;

	// protected abstract String asTSV() throws CannotSerializeException;

	protected abstract void
	        asTSV(OutputStream out) throws CannotSerializeException;

	// protected abstract String asCSV() throws CannotSerializeException;

	protected abstract void
	        asCSV(OutputStream out) throws CannotSerializeException;

	// protected abstract String asSparqlThrift() throws
	// CannotSerializeException;

	protected abstract void
	        asSparqlThrift(OutputStream out) throws CannotSerializeException;

	// protected abstract String asSparqlJSON() throws CannotSerializeException;

	protected abstract void
	        asSparqlJSON(OutputStream out) throws CannotSerializeException;

	// protected abstract String asSparqlXML() throws CannotSerializeException;

	protected abstract void
	        asSparqlXML(OutputStream out) throws CannotSerializeException;

	// protected abstract String asJSON() throws CannotSerializeException;

	protected abstract void
	        asJSON(OutputStream out) throws CannotSerializeException;

	// protected abstract String asXML() throws CannotSerializeException;

	protected abstract void
	        asXML(OutputStream out) throws CannotSerializeException;

	protected List<MediaType> fillSupportedMediaTypes() {
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		String mediaType = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// First one is the default one when using the variants approach
		try {
			asPlainText(baos);
			mediaTypes.add(MediaType.TEXT_PLAIN_TYPE);
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.TEXT_PLAIN_TYPE);
		}

		try {
			asRDFStream(null, baos);
			mediaTypes.addAll(HttpOntologyUtils.getAvailableRdfMediaTypes());
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.addAll(HttpOntologyUtils.getAvailableRdfMediaTypes());
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetTSV.getHeaderString();
			asTSV(baos);
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetCSV.getHeaderString();
			asCSV(baos);
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetJSON.getHeaderString();
			asSparqlJSON(baos);
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetXML.getHeaderString();
			asSparqlXML(baos);
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetThrift.getHeaderString();
			asSparqlThrift(baos);
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}

		try {
			asJSON(baos);
			mediaTypes.add(MediaType.APPLICATION_JSON_TYPE);
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.APPLICATION_JSON_TYPE);
		}

		try {
			asXML(baos);
			mediaTypes.addAll(Arrays.asList(MediaType.TEXT_XML_TYPE,
			                                MediaType.APPLICATION_XML_TYPE));
		} catch (CannotSerializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.addAll(Arrays.asList(MediaType.TEXT_XML_TYPE,
			                                MediaType.APPLICATION_XML_TYPE));
		}

		return mediaTypes;
	}

	public abstract List<Variant> listAvailableVariants();
}
