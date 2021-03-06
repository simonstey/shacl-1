package org.topbraid.shacl.model.impl;

import java.util.LinkedList;
import java.util.List;

import org.topbraid.shacl.constraints.ConstraintExecutable;
import org.topbraid.shacl.constraints.TemplateConstraintExecutable;
import org.topbraid.shacl.model.SHACLFactory;
import org.topbraid.shacl.model.SHACLTemplate;
import org.topbraid.shacl.model.SHACLTemplateConstraint;
import org.topbraid.shacl.model.SHACLTemplateRule;
import org.topbraid.shacl.rules.RuleExecutable;
import org.topbraid.shacl.rules.TemplateRuleExecutable;
import org.topbraid.shacl.util.SHACLUtil;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Default implementation of SHACLTemplateConstraint.
 * 
 * @author Holger Knublauch
 */
public class SHACLTemplateRuleImpl extends SHACLTemplateCallImpl implements SHACLTemplateRule {
	
	public SHACLTemplateRuleImpl(Node node, EnhGraph graph) {
		super(node, graph);
	}

	
	@Override
	public List<RuleExecutable> getExecutables() {
		List<RuleExecutable> results = new LinkedList<RuleExecutable>();
		Resource type = JenaUtil.getType(this);
		if(type == null) {
			type = SHACLUtil.getDefaultTemplateType(this);
		}
		for(Resource cls : JenaUtil.getAllSuperClassesStar(type)) {
			SHACLTemplate template = SHACLFactory.asTemplate(cls);
			if(template.getSPARQL() != null) {
				TemplateRuleExecutable executable = new TemplateRuleExecutable(this, template);
				if(executable.isComplete()) {
					results.add(executable);
				}
			}
		}
		return results;
	}
}