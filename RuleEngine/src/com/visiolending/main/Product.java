package com.visiolending.main;

public class Product {

	  private final String productName;
	  private final double baseRate;

	  public String getProductName() {
	    return productName;
	  }

	  public double getBaseRate() {
	    return baseRate;
	  }

	  public Product(String productName, double baseRate) {
	    this.productName = productName;
	    this.baseRate = baseRate;
	  }
}

