package com.visiolending.main.entities;


public abstract class RuleNode {

	public abstract <R> R accept(Visitor<R> visitor);

	  public interface Visitor<T> {

	    T visitRules(Rules node);

	    T visitRuleAction(RuleAction node);

	    T visitRuleCondition(RuleCondition node);

	    T visitRule(Rule node);
	  }
}
