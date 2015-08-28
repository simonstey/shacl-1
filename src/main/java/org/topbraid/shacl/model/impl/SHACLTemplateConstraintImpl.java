package org.topbraid.shacl.model.impl;

import java.util.LinkedList;
import java.util.List;

import org.topbraid.shacl.constraints.ConstraintExecutable;
import org.topbraid.shacl.constraints.DerivedPropertyConstraintExecutable;
import org.topbraid.shacl.constraints.NativeConstraintExecutable;
import org.topbraid.shacl.constraints.TemplateConstraintExecutable;
import org.topbraid.shacl.model.SHACLArgument;
import org.topbraid.shacl.model.SHACLFactory;
import org.topbraid.shacl.model.SHACLTemplate;
import org.topbraid.shacl.model.SHACLTemplateConstraint;
import org.topbraid.shacl.util.SHACLUtil;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.util.JenaDatatypes;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Default implementation of SHACLTemplateConstraint.
 * 
 * @author Holger Knublauch
 */
public class SHACLTemplateConstraintImpl extends SHACLTemplateCallImpl implements SHACLTemplateConstraint {
	
	public SHACLTemplateConstraintImpl(Node node, EnhGraph graph) {
		super(node, graph);
	}

	
	@Override
	public List<ConstraintExecutable> getExecutables() {
		List<ConstraintExecutable> results = new LinkedList<ConstraintExecutable>();
		Resource type = JenaUtil.getType(this);
		if(type == null) {
			type = SHACLUtil.getDefaultTemplateType(this);
		}
		for(Resource cls : JenaUtil.getAllSuperClassesStar(type)) {
			SHACLTemplate template = SHACLFactory.asTemplate(cls);
			TemplateConstraintExecutable executable = new TemplateConstraintExecutable(this, template);
			if(template.getSPARQL() != null || executable.getValidationFunction() != null) {
				if(executable.isComplete()) {
					results.add(executable);
				}
			}
		}
		if(this.getTemplate().equals(SH.DerivedPropertyConstraint)){
			results.add(new DerivedPropertyConstraintExecutable(this,this.getTemplate()));
		}
		return results;
	}
}