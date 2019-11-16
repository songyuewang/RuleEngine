package com.visiolending.main;

public class EvaluateResult {

	private boolean qualified;
	  private double rate;

	  public boolean isQualified() {
	    return qualified;
	  }

	  public double getRate() {
	    return rate;
	  }

	  public void setQualified(boolean qualified) {
	    this.qualified = qualified;
	  }

	  public void setRate(double rate) {
	    this.rate = rate;
	  }

	  @Override
	  public String toString() {
	    return "EvaluateResult{" +
	        "qualified=" + qualified +
	        ", rate=" + rate +
	        '}';
	  }
}
