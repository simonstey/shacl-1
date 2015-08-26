package org.topbraid.shacl.model.impl;

import org.topbraid.shacl.model.SHACLConstraintViolation;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Default implementation of SHACLConstraintViolation.
 * 
 * @author Holger Knublauch
 */
public class SHACLConstraintViolationImpl extends SHACLResourceImpl implements SHACLConstraintViolation {
	
	public SHACLConstraintViolationImpl(Node node, EnhGraph graph) {
		super(node, graph);
	}

	
	@Override
	public Resource getFocusNode() {
		return JenaUtil.getResourceProperty(this, SH.focusNode);
	}

	
	@Override
	public String getMessage() {
		return JenaUtil.getStringProperty(this, SH.message);
	}

	
	@Override
	public RDFNode getObject() {
		return JenaUtil.getProperty(this, SH.object);
	}

	
	@Override
	public Property getPredicate() {
		Resource value = JenaUtil.getPropertyResourceValue(this, SH.predicate);
		if(value != null) {
			return JenaUtil.asProperty(value);
		}
		else {
			return null;
		}
	}

	
	@Override
	public Resource getSourceConstraint() {
		return JenaUtil.getResourceProperty(this, SH.sourceConstraint);
	}

	
	@Override
	public Resource getSourceShape() {
		return JenaUtil.getResourceProperty(this, SH.sourceShape);
	}

	
	@Override
	public Resource getSubject() {
		return JenaUtil.getResourceProperty(this, SH.subject);
	}
}