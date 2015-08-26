package org.topbraid.shacl.rules;

import java.util.LinkedList;
import java.util.List;

import org.topbraid.shacl.model.SHACLArgument;
import org.topbraid.shacl.model.SHACLFactory;
import org.topbraid.shacl.model.SHACLShape;
import org.topbraid.shacl.model.SHACLTemplate;
import org.topbraid.shacl.model.SHACLTemplateConstraint;
import org.topbraid.shacl.model.SHACLTemplateRule;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.system.SPINLabels;
import org.topbraid.spin.util.JenaDatatypes;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A ConstraintExecutable backed by a template call.
 * 
 * @author Holger Knublauch
 */
public class TemplateRuleExecutable extends RuleExecutable {
	
	private SHACLTemplateRule rule;
	
	private SHACLTemplate template;
	
	
	public TemplateRuleExecutable(SHACLTemplateRule constraint, SHACLTemplate template) {
		super(template);
		this.rule = rule;
		this.template = template;
	}
	
	
	public SHACLTemplateRule getTemplateCall() {
		return rule;
	}


	public List<SHACLShape> getFilterShapes() {
		List<SHACLShape> results = new LinkedList<SHACLShape>();
		for(Resource scope : JenaUtil.getResourceProperties(rule, SH.filterShape)) {
			results.add(SHACLFactory.asShape(scope));
		}
		return results;
	}
	
	
	/**
	 * Checks if all non-optional arguments are present.
	 * @return true  if complete
	 */
	public boolean isComplete() {
		boolean hasDirectArgumentValue = false;
		for(SHACLArgument arg : template.getArguments()) {
			boolean hasValue = rule.hasProperty(arg.getPredicate()) || arg.getDefaultValue() != null;
			if(!arg.isOptional() && !hasValue) {
				if(!(arg.hasProperty(SH.optionalWhenInherited, JenaDatatypes.TRUE) && arg.isOptionalAtTemplate(template))) {
					return false;
				}
			}
			if(hasValue && template.hasProperty(SH.argument, arg)) {
				hasDirectArgumentValue = true;
			}
		}
		return !template.isAbstract() || !template.hasProperty(SH.argument) || hasDirectArgumentValue;
	}


	public String toString() {
		return SPINLabels.get().getLabel(template);
	}
}
