/*
 * Copyright 2006 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.enunciate.contract.rest;

import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.ReferenceType;
import org.codehaus.enunciate.contract.validation.ValidationException;
import org.codehaus.enunciate.contract.jaxb.types.XmlType;
import org.codehaus.enunciate.contract.jaxb.types.XmlTypeException;
import org.codehaus.enunciate.contract.jaxb.types.XmlTypeFactory;
import org.codehaus.enunciate.rest.annotations.*;
import net.sf.jelly.apt.decorations.declaration.DecoratedMethodDeclaration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A REST method.
 *
 * @author Ryan Heaton
 */
public class RESTMethod extends DecoratedMethodDeclaration {

  private final RESTNoun noun;
  private final RESTParameter properNoun;
  private final RESTParameter nounValue;
  private final Collection<RESTParameter> adjectives;
  private final Collection<RESTError> RESTErrors;
  private final String jsonpParameter;

  public RESTMethod(MethodDeclaration delegate) {
    super(delegate);

    RESTParameter properNoun = null;
    RESTParameter nounValue = null;
    this.adjectives = new ArrayList<RESTParameter>();
    int parameterPosition = 0;
    for (ParameterDeclaration parameterDeclaration : getParameters()) {
      RESTParameter restParameter = new RESTParameter(parameterDeclaration, parameterPosition++);
      if (restParameter.isProperNoun()) {
        if (properNoun != null) {
          throw new ValidationException(properNoun.getPosition(), "REST method has more than one proper noun.  The other found at " + restParameter.getPosition());
        }
        
        properNoun = restParameter;
      }
      else if (restParameter.isNounValue()) {
        if (nounValue != null) {
          throw new ValidationException(nounValue.getPosition(), "REST method has more than one noun value.  The other found at " + restParameter.getPosition());
        }

        nounValue = restParameter;
      }
      else {
        adjectives.add(restParameter);
      }
    }

    this.nounValue = nounValue;
    this.properNoun = properNoun;

    this.RESTErrors = new ArrayList<RESTError>();
    for (ReferenceType referenceType : getThrownTypes()) {
      ClassDeclaration throwableDeclaration = ((ClassType) referenceType).getDeclaration();
      this.RESTErrors.add(new RESTError(throwableDeclaration));
    }

    String noun = getSimpleName();
    Noun nounInfo = getAnnotation(Noun.class);
    if ((nounInfo != null) && (!"".equals(nounInfo.value()))) {
      noun = nounInfo.value();
    }

    String nounContext = "";
    NounContext nounContextInfo = delegate.getDeclaringType().getAnnotation(NounContext.class);
    if (nounContextInfo != null) {
      nounContext = nounContextInfo.value();
    }
    if ((nounInfo != null) && (!"##default".equals(nounInfo.context()))) {
      nounContext = nounInfo.context();
    }
    
    this.noun = new RESTNoun(noun, nounContext);

    String jsonpParameter = null;
    JSONP jsonpInfo = getAnnotation(JSONP.class);
    if (jsonpInfo == null) {
      jsonpInfo = delegate.getDeclaringType().getAnnotation(JSONP.class);
      if (jsonpInfo == null) {
        jsonpInfo = delegate.getDeclaringType().getPackage().getAnnotation(JSONP.class);
      }
    }

    if (jsonpInfo != null) {
      jsonpParameter = jsonpInfo.paramName();
    }

    this.jsonpParameter = jsonpParameter;
  }

  /**
   * The noun for this method.
   *
   * @return The noun for this method.
   */
  public RESTNoun getNoun() {
    return noun;
  }

  /**
   * The verb for this method.
   *
   * @return The verb for this method.
   */
  public VerbType[] getVerbs() {
    VerbType[] verbs = { VerbType.read };

    Verb verbInfo = getAnnotation(Verb.class);
    if (verbInfo != null) {
      verbs = verbInfo.value();
    }

    return verbs;
  }

  /**
   * The proper noun for this method.
   *
   * @return The proper noun for this method.
   */
  public RESTParameter getProperNoun() {
    return this.properNoun;
  }

  /**
   * The noun value for this method.
   *
   * @return The noun value for this method.
   */
  public RESTParameter getNounValue() {
    return this.nounValue;
  }

  /**
   * The adjectives for this REST method.
   *
   * @return The adjectives for this REST method.
   */
  public Collection<RESTParameter> getAdjectives() {
    return adjectives;
  }

  /**
   * The errors possibly thrown by this REST method.
   *
   * @return The errors possibly thrown by this REST method.
   */
  public Collection<RESTError> getRESTErrors() {
    return RESTErrors;
  }

  /**
   * The XML type of the return value.
   *
   * @return The XML type of the return value.
   */
  public XmlType getXMLReturnType() {
    try {
      return XmlTypeFactory.getXmlType(getReturnType());
    }
    catch (XmlTypeException e) {
      throw new ValidationException(getPosition(), e.getMessage());
    }
  }

  /**
   * The JSONP parameter name.
   *
   * @return The JSONP parameter name, or null if none.
   */
  public String getJSONPParameter() {
    return jsonpParameter;
  }
}
