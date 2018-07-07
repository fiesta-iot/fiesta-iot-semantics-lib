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
import java.io.PrintWriter;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.sparql.resultset.JSONOutput;
import org.apache.jena.sparql.resultset.XMLOutput;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotSerializeException;
import eu.fiesta_iot.utils.semantics.serializer.formatter.JsonOuput;
import eu.fiesta_iot.utils.semantics.serializer.formatter.XmlOutput;

public class BooleanSerializer extends Serializer<Boolean> {
	private static final Logger log =
	        LoggerFactory.getLogger(BooleanSerializer.class);

	private static boolean init = false;
	protected static List<MediaType> supportedMediaTypes;
	protected static List<Variant> availableVariants;

	static {
		BooleanSerializer.init();
	}

	protected static void init() {
		if (BooleanSerializer.init) {
			return;
		}

		BooleanSerializer brw = new BooleanSerializer(false);
		BooleanSerializer.supportedMediaTypes = brw.fillSupportedMediaTypes();
		log.debug("Boolean serialization supporting "
		          + BooleanSerializer.supportedMediaTypes.size()
		          + " media types: "
		          + BooleanSerializer.supportedMediaTypes.toString());
		BooleanSerializer.availableVariants =
				HttpStreamer.getAvailableVariants(supportedMediaTypes);

		BooleanSerializer.init = true;
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
	protected MediaType _getAcceptableMediaType(List<MediaType> mediaTypes) throws CannotSerializeException {
		return ResultSetSerializer.getAcceptableMediaType(mediaTypes);
	}
	
	public BooleanSerializer(Boolean input) {
		super(input);
	}

//	@Override
//	protected String asRDFStream(Lang lang) throws CannotSerializeException {
//		// Create model for boolean value
//		Model bm = ModelFactory.createDefaultModel();
//		bm.addLiteral(bm.createResource(), RDF.value, (boolean) getInput());
//		// Assuming that serialization is done using the same function
//		// This will avoid the need for getMediaType()
//		// return new ModelSerializer(bm).asRDFStream(lang);
//
//		return new ModelSerializer(bm).writeAs(getMediaType());
//	}

	@Override
	protected void asRDFStream(Lang lang, OutputStream out) throws CannotSerializeException {
		// Create model for boolean value
		Model bm = ModelFactory.createDefaultModel();
		bm.addLiteral(bm.createResource(), RDF.value, (boolean) getInput());
		// Assuming that serialization is done using the same function
		// This will avoid the need for getMediaType()
		// return new ModelSerializer(bm).asRDFStream(lang);

		new ModelSerializer(bm).writeAs(out, getMediaType());
	}

//	@Override
//	protected String asPlainText() {
//		return getInput() ? "True" : "False";
//	}
	
	@Override
	protected void asPlainText(OutputStream out) {
		PrintWriter pw = new PrintWriter(out); 
		pw.write(getInput() ? "True" : "False");
	}

	
	
//	@Override
//	protected String asXML() {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asXML(baos);
//		return baos.toString();
//	}

	@Override
	protected void asXML(OutputStream out) {
		XmlOutput bOut = new XmlOutput();
		bOut.format(out, getInput());
	}

	
//	@Override
//	protected String asJSON() {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asJSON(baos);
//		return baos.toString();
//	}
	
	@Override
	protected void asJSON(OutputStream out) {
		JsonOuput jOut = new JsonOuput();
		jOut.format(out, getInput());
	}

//	@Override
//	protected String asCSV() {
//		return Boolean.toString(getInput());
//	}

	@Override
	protected void asCSV(OutputStream out) {
		PrintWriter pw = new PrintWriter(out); 
		pw.write(Boolean.toString(getInput()));
	}
	
//	@Override
//	protected String asTSV() {
//		return asCSV();
//	}

	@Override
	protected void asTSV(OutputStream out) {
		asCSV(out);
	}
	
//	@Override
//	protected String asSparqlThrift() throws CannotSerializeException {
//		throw new CannotSerializeException();
//	}
	
	@Override
	protected void asSparqlThrift(OutputStream out) throws CannotSerializeException {
		throw new CannotSerializeException();
	}
	
//	@Override
//	protected String asSparqlXML() {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asSparqlXML(baos);
//		return new String(baos.toByteArray());
//	}

	@Override
	protected void asSparqlXML(OutputStream out) {
		XMLOutput xOut = new XMLOutput(null);
		xOut.format(out, getInput());
	}
	
//	@Override
//	protected String asSparqlJSON() {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		asSparqlJSON(baos);
//		return new String(baos.toByteArray());
//	}
	
	@Override
	protected void asSparqlJSON(OutputStream out) {
		JSONOutput xOut = new JSONOutput();
		xOut.format(out, getInput());
	}
}
