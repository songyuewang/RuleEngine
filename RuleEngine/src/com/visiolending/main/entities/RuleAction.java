package com.visiolending.main.entities;

 

public class RuleAction extends RuleNode {

	  public enum AdjustType {PLUS, MINUS}

	  private final boolean qualified;
	  private final AdjustType adjustType;
	  private final double adjustRate;

	  public RuleAction(boolean qualified, AdjustType adjustType, double adjustRate) {
	    this.qualified = qualified;
	    this.adjustType = adjustType;
	    this.adjustRate = adjustRate;
	  }

		@Override
		public <R> R accept(Visitor<R> visitor) {
	    return visitor.visitRuleAction(this);
	  }

	  public boolean isQualified() {
	    return qualified;
	  }

	  public AdjustType getAdjustType() {
	    return adjustType;
	  }

	  public double getAdjustRate() {
	    return adjustRate;
	  }

	  @Override
	  public String toString() {
	    return "RuleAction{" +
	        "qualified=" + qualified +
	        ", adjustType=" + adjustType +
	        ", adjustRate=" + adjustRate +
	        '}';
	  }
}

