package org.topbraid.shacl.constraints;

import java.util.LinkedList;
import java.util.List;

import org.topbraid.shacl.model.SHACLFactory;
import org.topbraid.shacl.model.SHACLShape;
import org.topbraid.shacl.model.SHACLTemplate;
import org.topbraid.shacl.model.SHACLTemplateCall;
import org.topbraid.shacl.model.SHACLTemplateConstraint;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.system.SPINLabels;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A ConstrainExecutable representing a directly executable constraint,
 * e.g. backed by a sh:sparql query.
 * 
 * @author Holger Knublauch
 */
public class DerivedPropertyConstraintExecutable extends ConstraintExecutable {
	
	private SHACLTemplateConstraint constraint;
	
	private SHACLTemplate template;
	
	public DerivedPropertyConstraintExecutable(SHACLTemplateConstraint constraint, SHACLTemplate template) {
		super(template);
		this.constraint = constraint;
		this.template = template;
	}

	@Override
	public List<SHACLShape> getFilterShapes() {
		List<SHACLShape> results = new LinkedList<SHACLShape>();
		for(Resource scope : JenaUtil.getResourceProperties(getResource(), SH.filterShape)) {
			results.add(SHACLFactory.asShape(scope));
		}
		return results;
	}


	@Override
	public SHACLTemplateCall getTemplateCall() {
		return null;
	}


	public String toString() {
		return SPINLabels.get().getLabel(template);
	}
}
