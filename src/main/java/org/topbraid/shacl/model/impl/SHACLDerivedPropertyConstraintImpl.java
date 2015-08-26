package org.topbraid.shacl.model.impl;

import org.topbraid.shacl.model.SHACLPropertyConstraint;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Default implementation of SHACLPropertyConstraint.
 * 
 * @author Holger Knublauch
 */
public class SHACLDerivedPropertyConstraintImpl extends SHACLPropertyConstraintImpl implements SHACLPropertyConstraint {
	
	public SHACLDerivedPropertyConstraintImpl(Node node, EnhGraph graph) {
		super(node, graph);
	}

	public String toString() {
		return "Property " + getVarName();
	}
}