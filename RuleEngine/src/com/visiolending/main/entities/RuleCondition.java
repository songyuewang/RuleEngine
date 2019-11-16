package com.visiolending.main.entities;
 

public class RuleCondition extends RuleNode {

	  public enum Operator {GT, LT, EQ}

	  public enum ValueType {INT, STRING}

	  @Override
	  public <R> R accept(Visitor<R> visitor) {
	    return visitor.visitRuleCondition(this);
	  }

	  private final String property;
	  private final Operator operator;
	  private final String value;
	  private final ValueType valueType;

	  public RuleCondition(String property, Operator operator, String value,
	      ValueType valueType) {
	    this.property = property;
	    this.operator = operator;
	    this.value = value;
	    this.valueType = valueType;
	  }

	  public String getProperty() {
	    return property;
	  }

	  public Operator getOperator() {
	    return operator;
	  }

	  public String getValue() {
	    return value;
	  }

	  public ValueType getValueType() {
	    return valueType;
	  }
}

