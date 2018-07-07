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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.resultset.ResultSetLang;

import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotDeserializeException;
import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotSerializeException;

public abstract class Deserializer<T> extends HttpStreamer {
	private InputStream input;
	private String mediaType;

	public Deserializer(InputStream input, MediaType mediaType) {
		this(input, mediaType.toString());
	}

	public Deserializer(InputStream input, String mediaType) {
		this.input = input;
		this.mediaType = mediaType;
	}

	protected InputStream getInputStream() {
		return this.input;
	}

	protected String getMediaType() {
		return this.mediaType;
	}

	public T read() throws CannotDeserializeException {
		// RDF Media Types
		if (HttpOntologyUtils.isValidRdfMediaType(mediaType)) {
			Lang lang = HttpOntologyUtils.getRdfLanguage(mediaType);
			if (lang != null) {
				return fromRDFStream(lang);
			}
		}

		// Format to other media types
		// text/plain
		if (MediaType.TEXT_PLAIN_TYPE.toString().equals(mediaType)) {
			return fromPlainText();
		}

		// xml
		if (MediaType.TEXT_XML_TYPE.toString().equals(mediaType)
		    || MediaType.APPLICATION_XML_TYPE.toString().equals(mediaType)) {
			return fromXML();
		}

		if (MediaType.APPLICATION_JSON_TYPE.toString().equals(mediaType)) {
			return fromJSON();
		}

		// sparql-results+xml
		if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetXML
		        .getAltContentTypes())) {
			return fromSparqlXML();
		}

		// sparql-results+json
		if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetJSON
		        .getAltContentTypes())) {
			return fromSparqlJSON();
		}

		// text/csv or text/csv
		if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetCSV
		        .getAltContentTypes())) {
			return fromCSV();
		}

		if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetTSV
		        .getAltContentTypes())) {
			return fromTSV();
		}

		if (checkMediaType(mediaType, ResultSetLang.SPARQLResultSetThrift
		        .getAltContentTypes())) {
			return fromSparqlThrift();
		}

		throw new CannotDeserializeException("Cannot render media type "
		                                     + mediaType);
	}

	private static boolean checkMediaType(MediaType mediaType) {
		return checkMediaType(mediaType);
	}

	private static boolean checkMediaType(String mediaType,
	                                      List<String> isPartOf) {
		return isPartOf.stream().anyMatch(mediaType::equals);
	}

	protected abstract T
	        fromRDFStream(Lang lang) throws CannotDeserializeException;

	protected abstract T fromPlainText() throws CannotDeserializeException;

	protected abstract T fromXML() throws CannotDeserializeException;

	protected abstract T fromJSON() throws CannotDeserializeException;

	protected abstract T fromTSV() throws CannotDeserializeException;

	protected abstract T fromCSV() throws CannotDeserializeException;

	protected abstract T fromSparqlThrift() throws CannotDeserializeException;

	protected abstract T fromSparqlJSON() throws CannotDeserializeException;

	protected abstract T fromSparqlXML() throws CannotDeserializeException;

	protected List<MediaType> fillSupportedMediaTypes() {
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		String mediaType = null;

		try {
			fromRDFStream(null);
			mediaTypes.addAll(HttpOntologyUtils.getAvailableRdfMediaTypes());
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.addAll(HttpOntologyUtils.getAvailableRdfMediaTypes());
		}

		try {
			fromPlainText();
			mediaTypes.add(MediaType.TEXT_PLAIN_TYPE);
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.TEXT_PLAIN_TYPE);
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetTSV.getHeaderString();
			fromTSV();
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}
		try {
			mediaType = ResultSetLang.SPARQLResultSetCSV.getHeaderString();
			fromCSV();
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetJSON.getHeaderString();
			fromSparqlJSON();
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetXML.getHeaderString();
			fromSparqlXML();
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}

		try {
			mediaType = ResultSetLang.SPARQLResultSetThrift.getHeaderString();
			fromSparqlThrift();
			mediaTypes.add(MediaType.valueOf(mediaType));
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.valueOf(mediaType));
		}
		
		try {
			fromJSON();
			mediaTypes.add(MediaType.APPLICATION_JSON_TYPE);
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.add(MediaType.APPLICATION_JSON_TYPE);
		}

		try {
			fromXML();
			mediaTypes.addAll(Arrays.asList(MediaType.TEXT_XML_TYPE,
			                                MediaType.APPLICATION_XML_TYPE));
		} catch (CannotDeserializeException ex) {
			// Not supported
		} catch (Exception ex) {
			mediaTypes.addAll(Arrays.asList(MediaType.TEXT_XML_TYPE,
			                                MediaType.APPLICATION_XML_TYPE));
		}

		return mediaTypes;
	}
}
