package com.visiolending.main.entities;

 

public class Rule extends RuleNode {

	  private final RuleCondition condition;
	  private final RuleAction action;

	  @Override
	  public <R> R accept(Visitor<R> visitor) {
	    return visitor.visitRule(this);
	  }

	  public RuleCondition getCondition() {
	    return condition;
	  }

	  public RuleAction getAction() {
	    return action;
	  }

	  public Rule(RuleCondition condition, RuleAction action) {
	    this.condition = condition;
	    this.action = action;
	  }
}
