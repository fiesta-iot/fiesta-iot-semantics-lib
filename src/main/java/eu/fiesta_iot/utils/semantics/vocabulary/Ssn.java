/*******************************************************************************
 * Copyright (c) 2018 Jorge Lanza, 
 *                    David Gomez, 
 *                    Luis Sanchez,
 *                    Juan Ramon Santana
 *
 * For the full copyright and license information, please view the LICENSE
 * file that is distributed with this source code.
 *******************************************************************************/
package eu.fiesta_iot.utils.semantics.vocabulary;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Ssn {

	private static final OntModel model =
	        ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	
	// The namespace of the vocabulary as a string
	public static final String NS_SHORT = "ssn";
	public static final String NS = "http://purl.oclc.org/NET/ssnx/ssn#";

	// The namespace of the vocabulary as a Resoruce
	public static final Resource NAMESPACE = model.createResource(NS);

	public static final OntClass Deployment =
	        model.createClass(NS + "Deployment");
	public static final OntClass Platform = model.createClass(NS + "Platform");
	public static final OntClass Device = model.createClass(NS + "Device");
	public static final OntClass SensingDevice =
	        model.createClass(NS + "SensingDevice");

	public static final Property onPlatform =
	        model.createProperty(NS + "onPlatform");
	public static final Property attachedSystem =
	        model.createProperty(NS + "attachedSystem");
	public static final Property hasDeployment =
	        model.createProperty(NS + "hasDeployment");
	public static final Property hasSubSystem =
	        model.createProperty(NS + "hasSubSystem");

	public static final OntClass Observation =
	        model.createClass(NS + "Observation");
	public static final OntClass ObservationValue =
	        model.createClass(NS + "ObservationValue");
	public static final OntClass SensorOutput =
	        model.createClass(NS + "SensorOutput");

	public static final Property observedBy =
	        model.createProperty(NS + "observedBy");
	public static final Property madeObservation =
	        model.createProperty(NS + "madeObservation");
	public static final Property observedProperty =
	        model.createProperty(NS + "observedProperty");
	public static final Property observationResult =
	        model.createProperty(NS + "observationResult");
	public static final Property observationSamplingTime =
	        model.createProperty(NS + "observationSamplingTime");
	public static final Property hasValue =
	        model.createProperty(NS + "hasValue");

}
