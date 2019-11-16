package com.visiolending.main.entities;

import java.util.List;

 

public class Rules extends RuleNode {

  private final List<Rule> rules;

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visitRules(this);
  }

  public List<Rule> getRules() {
    return rules;
  }

  public Rules(List<Rule> rules) {
    this.rules = rules;
  }
}

