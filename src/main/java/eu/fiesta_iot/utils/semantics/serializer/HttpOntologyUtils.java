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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.resultset.ResultSetLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpOntologyUtils {
	private static final Logger log =
	        LoggerFactory.getLogger(HttpOntologyUtils.class);

	private static Lang[] NOT_RDF_LANGS = {
	        ResultSetLang.SPARQLResultSetXML, ResultSetLang.SPARQLResultSetJSON,
	        ResultSetLang.SPARQLResultSetCSV, ResultSetLang.SPARQLResultSetTSV,
	        ResultSetLang.SPARQLResultSetThrift,
	        ResultSetLang.SPARQLResultSetText };

	// text/nquads
	// application/n3
	// application/ld+json
	// application/rdf+xml
	// application/trix
	// application/rdf+json
	// text/n3
	// text/plain
	// application/rdf+thrift
	// application/x-turtle
	// null/rdf
	// application/trix+xml
	// application/x-trig
	// text/trig
	// text/rdf+n3
	// text/turtle
	// application/turtle
	// application/trig
	// text/n-quads
	// application/n-quads
	// application/n-triples
	protected static List<MediaType> getAvailableRdfMediaTypes() {
		Collection<Lang> langs = RDFLanguages.getRegisteredLanguages();
		Set<MediaType> mediaTypes = new HashSet<MediaType>();
		for (Lang lang : langs) {
			boolean isNotRdfLang =
			        Arrays.stream(NOT_RDF_LANGS).anyMatch(lang::equals);
			if (isNotRdfLang) {
				continue;
			}
			
			List<String> mediaTypesStr = lang.getAltContentTypes();
			mediaTypesStr.stream().forEach(str -> mediaTypes.add(MediaType.valueOf(str)));
		}

		mediaTypes.add(MediaType.APPLICATION_JSON_TYPE);
		mediaTypes.add(MediaType.APPLICATION_XML_TYPE);
		mediaTypes.add(MediaType.TEXT_XML_TYPE);
		
		return new ArrayList<MediaType>(mediaTypes);
	}

	public static boolean isValidRdfMediaType(MediaType m) {
		List<MediaType> rdfTypes = getAvailableRdfMediaTypes();
		return rdfTypes.stream().anyMatch(m::equals);
	}

	public static boolean isValidRdfMediaType(String m) {
		return isValidRdfMediaType(MediaType.valueOf(m));
	}

	// application/sparql-results+xml
	// application/sparql-results+json
	// text/csv
	// text/tab-separated-values
	// application/sparql-results+thrift
	// text/plain
	// application/json
	// application/xml
	// text/xml
	protected static List<MediaType> getAvailableSparqlMediaTypes() {
		Set<MediaType> mediaTypes = new HashSet<MediaType>();
		for (Lang lang : NOT_RDF_LANGS) {
			List<String> mediaTypesStr = lang.getAltContentTypes();
			mediaTypesStr.stream().forEach(str -> mediaTypes.add(MediaType.valueOf(str)));
		}

		mediaTypes.add(MediaType.APPLICATION_JSON_TYPE);
		mediaTypes.add(MediaType.APPLICATION_XML_TYPE);
		mediaTypes.add(MediaType.TEXT_XML_TYPE);

		return new ArrayList<MediaType>(mediaTypes);
	}

	public static boolean isValidSparqlMediaType(MediaType m) {
		List<MediaType> sparqlTypes = getAvailableSparqlMediaTypes();
		return sparqlTypes.stream().anyMatch(m::equals);
	}

	public static boolean isValidSparqlMediaType(String m) {
		return isValidSparqlMediaType(MediaType.valueOf(m));
	}
	
	protected static Lang getRdfLanguage(String contentType) {
		Lang lang = RDFLanguages.contentTypeToLang(contentType);
		if (lang == null) {
			log.trace("MediaType is not matching any RDF languages");
			return null;
		}

		return lang;
	}

}
