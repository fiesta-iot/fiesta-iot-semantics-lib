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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

import org.apache.jena.atlas.json.JSON;
import org.apache.jena.atlas.json.JsonArray;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.atlas.json.JsonValue;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fiesta_iot.utils.semantics.serializer.exceptions.CannotDeserializeException;

public class ModelDeserializer extends Deserializer<Model> {
	private static final Logger log =
	        LoggerFactory.getLogger(ModelDeserializer.class);

	private static boolean init = false;
	protected static List<MediaType> supportedMediaTypes;
	protected static List<Variant> availableVariants;
	
	static {
		ModelDeserializer.init();
	}

	protected static void init() {
		if (ModelDeserializer.init) {
			return;
		}
		
		ModelDeserializer mrr = new ModelDeserializer(new InputStream() {
			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return 0;
			}
		}, "");
		ModelDeserializer.supportedMediaTypes = mrr.fillSupportedMediaTypes();

		log.debug("Model deserialization supporting "
		          + ModelDeserializer.supportedMediaTypes.size()
		          + " media types: "
		          + ModelDeserializer.supportedMediaTypes.toString());
		
		ModelDeserializer.init = true;
	}
	
	public static List<MediaType> getSupportedMediaTypes() {
		return supportedMediaTypes;
	}
	
	protected List<MediaType> listSupportedMediaTypes() {
		return getSupportedMediaTypes();
	}
	

	public ModelDeserializer(InputStream input, MediaType mediaType) {
		super(input, mediaType);
	}

	public ModelDeserializer(InputStream input, String mediaType) {
		super(input, mediaType);
	}

	@Override
	protected Model fromRDFStream(Lang lang) throws CannotDeserializeException {
		Model m = ModelFactory.createDefaultModel();
		// Let it throw the RiotException as this way we know the
		// provided document is not correctly formatted
		// try {
		m.read(getInputStream(), null, lang.getHeaderString());
		return m;
		// } catch (RiotException ex) {
		// throw new RenderReaderException();
		// }
	}

	@Override
	protected Model fromPlainText() throws CannotDeserializeException {
		return fromRDFStream(RDFLanguages.NTRIPLES);
	}

	@Override
	protected Model fromXML() throws CannotDeserializeException {
		throw new CannotDeserializeException();
	}

	@Override
	protected Model fromJSON() throws CannotDeserializeException {
		// TODO Auto-generated method stub
		JsonObject o = JSON.parse(getInputStream());

		// Retrieve vars to check if it is a RDF object
		JsonArray vars = o.get("vars").getAsArray();

		JsonArray items = o.get("items").getAsArray();
		Iterator<JsonValue> iter = items.iterator();

		Model m = ModelFactory.createDefaultModel();
		while (iter.hasNext()) {
			JsonObject item = iter.next().getAsObject();
			;
			String subStr = item.get("subject").getAsString().toString();
			String preStr = item.get("predicate").getAsString().toString();
			String objStr = item.get("object").getAsString().toString();

			Resource sub = ResourceFactory.createResource(subStr);
			Property pre = ResourceFactory.createProperty(preStr);
			// There is no way to know if a literal or resource
			// Only way right now is to
			RDFNode obj = ResourceFactory.createResource(objStr);

			Statement stmt = ResourceFactory.createStatement(sub, pre, obj);
			m.add(stmt);
		}

		return m;
	}

	@Override
	protected Model fromTSV() throws CannotDeserializeException {
		throw new CannotDeserializeException();
	}

	@Override
	protected Model fromCSV() throws CannotDeserializeException {
		throw new CannotDeserializeException();
	}
	
	@Override
	protected Model fromSparqlJSON() throws CannotDeserializeException {
		throw new CannotDeserializeException();
	}

	@Override
	protected Model fromSparqlXML() throws CannotDeserializeException {
		throw new CannotDeserializeException();
	}
	
	@Override
	protected Model fromSparqlThrift() throws CannotDeserializeException {
		throw new CannotDeserializeException();
	}

}
