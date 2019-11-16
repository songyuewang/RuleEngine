package com.visiolending.main;

import java.util.ArrayList;
import java.util.List;
import com.visiolending.main.entities.*;
import com.visiolending.main.entities.RuleAction.AdjustType;
import com.visiolending.main.entities.RuleCondition.Operator;
import com.visiolending.main.entities.RuleCondition.ValueType;

public class EvaluateVisitor implements RuleNode.Visitor<Object>{

	private static final String PERSON_STATE_PROPERTY = "state";
	  private static final String PERSON_CREDIT_PROPERTY = "credit";
	  private static final String PRODUCT_NAME_PROPERTY = "productName";

	  private final Person person;
	  private final Product product;

	  public EvaluateVisitor(Person person, Product product) {
	    this.person = person;
	    this.product = product;
	  }

	  @Override
	  /**
	   * Should return a {@link EvaluateResult}.
	   */
	  public Object visitRules(Rules node) {
	    List<RuleAction> actions = new ArrayList<>();
	    for (Rule rule : node.getRules()) {
	      actions.add(((RuleAction) rule.accept(this)));
	    }
	    EvaluateResult result = new EvaluateResult();
	    result.setQualified(true);
	    result.setRate(product.getBaseRate());
	    for (RuleAction action : actions) {
	      if (action == null) {
	        System.out.println("Skip unaccepted rule.");
	        continue;
	      }

	      System.out.println("merge rule action: " + action);

	      if (!action.isQualified()) {
	        result.setQualified(false);
	        return result;
	      }

	      if (action.getAdjustType() == AdjustType.PLUS) {
	        result.setRate(result.getRate() + action.getAdjustRate());
	      } else if (action.getAdjustType() == AdjustType.MINUS) {
	        result.setRate(result.getRate() - action.getAdjustRate());
	      }
	    }
	    return result;
	  }

	  @Override
	  /**
	   * Should return a {@link RuleAction}.
	   */
	  public Object visitRuleAction(RuleAction node) {
	    return node;
	  }

	  @Override
	  /**
	   * Should return a boolean value.
	   */
	  public Object visitRuleCondition(RuleCondition node) {
	    String stringValue = null;
	    int numericValue = -1;
	    if (node.getProperty().equals(PERSON_STATE_PROPERTY)) {
	      stringValue = person.getState();
	    } else if (node.getProperty().equals(PERSON_CREDIT_PROPERTY)) {
	      numericValue = person.getCredit();
	    } else if (node.getProperty().equals(PRODUCT_NAME_PROPERTY)) {
	      stringValue = product.getProductName();
	    } else {
	      throw new UnsupportedOperationException("unknown property: " + node.getProperty());
	    }
	    if (stringValue != null) {
	      if (node.getOperator() == Operator.EQ && node.getValueType() == ValueType.STRING) {
	        return stringValue.equalsIgnoreCase(node.getValue());
	      } else {
	        throw new UnsupportedOperationException(
	            "unknown operator for string type: " + node.getOperator());
	      }
	    } else {
	      // Numeric value.
	      if (node.getValueType() != ValueType.INT) {
	        throw new UnsupportedOperationException("unknown value type, a number is expected");
	      }
	      if (node.getOperator() == Operator.EQ) {
	        return numericValue == Integer.valueOf(node.getValue());
	      } else if (node.getOperator() == Operator.GT) {
	        return numericValue > Integer.valueOf(node.getValue());
	      } else if (node.getOperator() == Operator.LT) {
	        return numericValue < Integer.valueOf(node.getValue());
	      }
	    }
	    return null;
	  }

	  @Override
	  /**
	   * Should return a {@link RuleAction} if the rule can be applied, otherwise {@code null}.
	   */
	  public Object visitRule(Rule node) {
	    boolean applied = (boolean) node.getCondition().accept(this);
	    if (!applied) {
	      return null;
	    }
	    return node.getAction().accept(this);
	  }
}
