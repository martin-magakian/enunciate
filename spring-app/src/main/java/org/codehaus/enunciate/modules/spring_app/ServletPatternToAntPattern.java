/*
 * Copyright 2006-2008 Web Cohesion
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

package org.codehaus.enunciate.modules.spring_app;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.util.List;

/**
 * Gets the qualified package name for a package or type.
 *
 * @author Ryan Heaton
 */
public class ServletPatternToAntPattern implements TemplateMethodModelEx {

  /**
   * Gets the client-side package for the type, type declaration, package, or their string values.
   *
   * @param list The arguments.
   * @return The string value of the client-side package.
   */
  public Object exec(List list) throws TemplateModelException {
    if (list.size() < 1) {
      throw new TemplateModelException("The servletPatternToAntPattern method must have a pattern as a parameter.");
    }

    TemplateScalarModel model = (TemplateScalarModel) list.get(0);
    String pattern = model.getAsString();
    if (pattern.endsWith("/*")) {
      pattern = pattern + "*";
    }
    return pattern;
  }


}