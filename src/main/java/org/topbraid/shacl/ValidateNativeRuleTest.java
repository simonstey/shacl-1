package org.topbraid.shacl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import junit.framework.TestCase;

import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.constraints.ModelConstraintValidator;
import org.topbraid.shacl.util.ModelPrinter;
import org.topbraid.shacl.util.SHACLUtil;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.MultiUnion;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.RDF;

public class ValidateNativeRuleTest extends TestCase {

	/**
	 * Loads the SHACL Square example file and validates all constraints. This
	 * test case can also be used as a starting point for your own custom
	 * applications.
	 * 
	 * @throws FileNotFoundException
	 */
	public void testSHACLNativeRules() throws InterruptedException {

		// Load the main data model
		Model dataModel = JenaUtil.createMemoryModel();

		dataModel.read(getClass().getResourceAsStream("/shaclrule.shacl.ttl"),
				"urn:dummy", FileUtils.langTurtle);

		// Load the shapes Model (here, includes the dataModel because that has
		// templates in it)
		Model shaclModel = JenaUtil.createDefaultModel();
		InputStream is = getClass().getResourceAsStream("/etc/shacl.shacl.ttl");
		shaclModel.read(is, SH.BASE_URI, FileUtils.langTurtle);
		MultiUnion unionGraph = new MultiUnion(new Graph[] {
				shaclModel.getGraph(), dataModel.getGraph() });

		Model shapesModel = ModelFactory.createModelForGraph(unionGraph);

		// Make sure all sh:Functions are registered
		SHACLFunctions.registerFunctions(shapesModel);

		// Create Dataset that contains both the main query model and the shapes
		// model
		// (here, using a temporary URI for the shapes graph)
		URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:"
				+ UUID.randomUUID().toString());

		// initializes the default model of the dataset (which will be queried)
		Dataset dataset = ARQFactory.get().getDataset(dataModel);
		dataset.addNamedModel(shapesGraphURI.toString(), shapesModel);

		// Run the validator and print results
		Model results = ModelConstraintValidator.get().validateModel(dataset,
				shapesGraphURI, null, false, null);
		System.out.println(ModelPrinter.get().print(SHACLUtil.removeDuplicateResultMessages(results)));

		// Expecting 6 constraint violations
		assertEquals(6, results.listResourcesWithProperty(RDF.type, SH.Error)
				.toList().size()
				+ results.listResourcesWithProperty(RDF.type, SH.Warning)
						.toList().size());
	}

//	public void testSHACLWithoutRules() throws InterruptedException {
//
//		// Load the main data model
//		Model dataModel = JenaUtil.createMemoryModel();
//
//		dataModel.read(getClass().getResourceAsStream("/shaclrule.shacl.ttl"),
//				"urn:dummy", FileUtils.langTurtle);
//
//		// Load the shapes Model (here, includes the dataModel because that has
//		// templates in it)
//		Model shaclModel = JenaUtil.createDefaultModel();
//		InputStream is = getClass().getResourceAsStream("/etc/shacl.shacl.ttl");
//		shaclModel.read(is, SH.BASE_URI, FileUtils.langTurtle);
//		MultiUnion unionGraph = new MultiUnion(new Graph[] {
//				shaclModel.getGraph(), dataModel.getGraph() });
//
//		Model shapesModel = SHACLUtil
//				.withDefaultValueTypeInferences(ModelFactory
//						.createModelForGraph(unionGraph));
//
//		// Make sure all sh:Functions are registered
//		SHACLFunctions.registerFunctions(shapesModel);
//
//		// Create Dataset that contains both the main query model and the shapes
//		// model
//		// (here, using a temporary URI for the shapes graph)
//		URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:"
//				+ UUID.randomUUID().toString());
//
//		// initializes the default model of the dataset (which will be queried)
//		Dataset dataset = ARQFactory.get().getDataset(dataModel);
//		dataset.addNamedModel(shapesGraphURI.toString(), shapesModel);
//
//		// Run the validator and print results
//		Model results = ModelConstraintValidator.get().validateModel(dataset,
//				shapesGraphURI, null, false, null, false);
//		//System.out.println(ModelPrinter.get().print(results));
//
//		// Expecting 4 constraint violations
//		assertEquals(4, results.listResourcesWithProperty(RDF.type, SH.Error)
//				.toList().size()
//				+ results.listResourcesWithProperty(RDF.type, SH.Warning)
//						.toList().size());
//	}
}
