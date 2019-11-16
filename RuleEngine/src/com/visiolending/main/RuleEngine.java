package com.visiolending.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.visiolending.main.entities.*;
import com.visiolending.main.entities.RuleAction.AdjustType;
import com.visiolending.main.entities.RuleCondition.Operator;
import com.visiolending.main.entities.RuleCondition.ValueType;
 

public class RuleEngine {

	  private static final String RULE = "rule";

	  private static final String CONDITION = "condition";
	  private static final String PROPERTY = "property";
	  private static final String OPERATOR = "operator";
	  private static final String VALUE_TYPE = "valueType";
	  private static final String VALUE = "value";

	  private static final String ACTION = "action";
	  private static final String QUALIFIED = "qualified";
	  private static final String ADJUST_TYPE = "adjustType";
	  private static final String ADJUST_RATE = "adjustRate";

	  public static final double BASE_RATE = 5.0;

	  private static Rules parseRules(InputStream file) {
	    List<Rule> rules = new ArrayList<>();
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    dbFactory.setIgnoringElementContentWhitespace(true);
	    DocumentBuilder dBuilder;
	    try (InputStream stream = file) {
	      dBuilder = dbFactory.newDocumentBuilder();
	      Document doc = dBuilder.parse(stream);
	      doc.getDocumentElement().normalize();
	      Element root = doc.getDocumentElement();
	      NodeList ruleElements = root.getElementsByTagName(RULE);
	      for (int i = 0; i < ruleElements.getLength(); i++) {
	        rules.add(parseRule((Element) ruleElements.item(i)));
	      }
	    } catch (ParserConfigurationException e) {
	      e.printStackTrace();
	    } catch (SAXException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return new Rules(rules);
	  }

	  private static Rule parseRule(Element ruleElement) {
	    return new Rule(
	        parseRuleCondition((Element) ruleElement.getElementsByTagName(CONDITION).item(0)),
	        parseRuleAction((Element) ruleElement.getElementsByTagName(ACTION).item(0)));
	  }

	  private static RuleCondition parseRuleCondition(Element ruleConditionElement) {
	    String property = ruleConditionElement.getAttribute(PROPERTY);
	    Operator operator = Operator.valueOf(ruleConditionElement.getAttribute(OPERATOR).toUpperCase());
	    ValueType valueType = ValueType
	        .valueOf(ruleConditionElement.getAttribute(VALUE_TYPE).toUpperCase());
	    String value = ruleConditionElement.getAttribute(VALUE);
	    return new RuleCondition(property, operator, value, valueType);
	  }

	  private static RuleAction parseRuleAction(Element ruleActionElement) {
	    boolean qualified = "true".equalsIgnoreCase(ruleActionElement.getAttribute(QUALIFIED));
	    if (!qualified) {
	      return new RuleAction(false, AdjustType.PLUS, 0.0);
	    }
	    AdjustType adjustType = AdjustType.valueOf(ruleActionElement.getAttribute(ADJUST_TYPE).toUpperCase());
	    double adjustRate = Double.valueOf(ruleActionElement.getAttribute(ADJUST_RATE));
	    return new RuleAction(true, adjustType, adjustRate);
	  }

	  private static EvaluateResult evaluate(Person person, Product product, Rules rules) {
	    return (EvaluateResult) rules.accept(new EvaluateVisitor(person, product));
	  }

	  public static void main(String[] args) {
	    Person person = new Person(777, "FL");
	    Product product = new Product("7-1 ARM", BASE_RATE);
	    Rules rules = parseRules(
	        RuleEngine.class.getClassLoader().getResourceAsStream("rules.xml"));
	    if (rules.getRules().isEmpty()) {
	      System.out.println("no rules");
	      return;
	    }
	    EvaluateResult evaluateResult = evaluate(person, product, rules);
	    System.out.println("result = " + evaluateResult);
	  }

}

