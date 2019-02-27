package org.toradocu.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.util.Pair;

// FIXME turn this into:
/*
EqSpecification extends Specification {

    public EqSpecification(Guard guard) {
        Checks.nonNullParameter(guard, "guard");
        this.guard = guard;
    }
 */
public class EquivalentMethodMatch {

  private ArrayList<String> methodSignatures;
  private ArrayList<String> simpleName;
  private boolean equivalence;
  private boolean similarity;
  /** Maps each String signature to a list of String arguments */
  private Map<String, List<String>> arguments;

  private String oracle;
  /**
   * This Map and the one below map a signature with A PAIR INT (PARAM POSITION), STRING (PARAM
   * NAME)
   */
  private Map<String, List<Pair<Integer, String>>> hardcodedParams;

  private Map<String, List<Pair<Integer, String>>> staticFinalParams;
  private boolean isNegated;

  public EquivalentMethodMatch() {
    this.methodSignatures = new ArrayList<>();
    this.simpleName = new ArrayList<>();
    this.equivalence = false;
    this.similarity = false;
    this.oracle = "";
  }

  EquivalentMethodMatch(
      ArrayList<String> methodSignatures,
      boolean equivalence,
      boolean similarity,
      Map<String, List<String>> arguments,
      boolean isNegated) {
    this.methodSignatures = methodSignatures;
    extractSimpleNames();
    this.equivalence = equivalence;
    this.similarity = similarity;
    this.arguments = arguments;
    if (!methodSignatures.isEmpty()) {
      this.hardcodedParams = areArgsHardcoded(methodSignatures);
      this.staticFinalParams = areArgsStaticFinal(methodSignatures);
    } else {
      this.hardcodedParams = new HashMap<>();
      this.staticFinalParams = new HashMap<>();
    }
    this.isNegated = isNegated;
    this.oracle = "";
  }

  private void extractSimpleNames() {
    this.simpleName = new ArrayList<>();
    for (String methodSignature : this.methodSignatures) {
      if (methodSignature.contains("(")) {
        this.simpleName.add(methodSignature.substring(0, methodSignature.indexOf("(")));
      } else {
        this.simpleName.add(methodSignature);
      }
    }
  }

  public ArrayList<String> getMethodSignatures() {
    return methodSignatures;
  }

  public boolean isSimilarity() {
    return similarity;
  }

  public boolean isEquivalence() {
    return equivalence;
  }

  public Map<String, List<String>> getArguments() {
    return arguments;
  }

  public ArrayList<String> getSimpleName() {
    return simpleName;
  }

  public void setOracle(String oracle) {
    this.oracle = oracle;
  }

  public String getOracle() {
    return this.oracle;
  }

  private Map<String, List<Pair<Integer, String>>> areArgsHardcoded(
      ArrayList<String> methodSignatures) {
    Map<String, List<Pair<Integer, String>>> map = new HashMap<>();
    List<Pair<Integer, String>> constArgs = new ArrayList<>();
    for (String signature : methodSignatures) {
      List<String> patterns = new ArrayList<>();
      patterns.add("[0-9]");
      patterns.add("true|false");
      patterns.add("\"[a-zA-Z]+\"");
      List<String> arguments = this.arguments.get(signature);
      for (int i = 0; i < arguments.size(); i++) {
        for (String pattern : patterns) {
          String arg = arguments.get(i);
          java.util.regex.Matcher matchConstant =
              Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(arg);
          if (matchConstant.find()) {
            constArgs.add(new Pair<>(i, matchConstant.group(0)));
            // constants.put(signature, matchConstant.group(0));
          }
        }
      }
      map.put(signature, constArgs);
    }
    return map;
  }

  private Map<String, List<Pair<Integer, String>>> areArgsStaticFinal(
      ArrayList<String> methodSignatures) {
    Map<String, List<Pair<Integer, String>>> map = new HashMap<>();
    List<Pair<Integer, String>> sfArgs = new ArrayList<>();
    for (String signature : methodSignatures) {
      String staticFinalRegex = "[A-Z]+|\\w+(\\.[A-Z]+|#[A-Z]+)+";
      List<String> signatureArgs = this.arguments.get(signature);
      // instead of parsing patterns as for hardcoded params, search for code matchings.
      // But I wouldn't do the match HERE: here I just verify if there are static final args!
      List<String> arguments = this.arguments.get(signature);
      for (int i = 0; i < arguments.size(); i++) {
        String arg = signatureArgs.get(i);
        Matcher staticFinalMatch = Pattern.compile(staticFinalRegex).matcher(arg);
        if (staticFinalMatch.matches()) {
          sfArgs.add(new Pair<>(i, staticFinalMatch.group(0)));
          // staticFinals.put(signature, staticFinalMatch.group(0));
        }
      }
      map.put(signature, sfArgs);
    }
    return map;
  }

  public Map<String, List<Pair<Integer, String>>> getHardcodedParams() {
    return hardcodedParams;
  }

  public Map<String, List<Pair<Integer, String>>> getStaticFinalParams() {
    return staticFinalParams;
  }

  public boolean isNegated() {
    return isNegated;
  }
}
