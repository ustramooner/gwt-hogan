/**
 * Copyright (C) 2009-2012 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.villagechief.gwt.hogan.server;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class HoganGenerator extends Generator {
	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		// Build a new class, that implements a "paintScreen" method
		JClassType classType;

		try {
			classType = context.getTypeOracle().getType(typeName);

			// fetch the template;
			// TODO: make this name configurable some how?
			String templateResource = "/" + typeName.replace(".", "/") + ".html";
			InputStream templateStream = getClass().getResourceAsStream(templateResource);
			if (templateStream == null) {
				logger.log(TreeLogger.ERROR, "Template not found: " + templateResource);
				throw new UnableToCompleteException();
			}
			InputStreamReader streamReader = new InputStreamReader(templateStream);
			StringBuilder sb = new StringBuilder();
			char[] cbuf = new char[1024];
			int len;
			while ( (len=streamReader.read(cbuf)) >= 0) {
				sb.append(cbuf, 0, len);
			}

			ScriptEngineManager engineMgr = new ScriptEngineManager();
			ScriptEngine engine = engineMgr.getEngineByName("ECMAScript");
			if (engine == null) {
				logger.log(TreeLogger.ERROR, "ECMAScript scripting engine not defined");
				throw new UnableToCompleteException();
			}
			InputStream hoganInputStream = HoganGenerator.class.getResourceAsStream("hogan.js");
			if (hoganInputStream == null) {
				logger.log(TreeLogger.ERROR, "hogan.js is missing");
				throw new UnableToCompleteException();
			}
			Reader hoganReader = new InputStreamReader(hoganInputStream);
			Reader templatesReader = new StringReader("Hogan.compile(data, {asString: true});");

			ScriptContext scriptContext = new SimpleScriptContext();
			Bindings b = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);
			b.put("data", sb.toString());

			engine.eval(hoganReader, scriptContext);
			String template = (String) engine.eval(templatesReader, scriptContext);

			SourceWriter src = getSourceWriter(classType, context, logger);

			src.println("@Override");
			src.println("protected void onLoad() {");
			src.println("	JSONObject data = new JSONObject();");
			src.println("	Map<String, Object> params = new HashMap<String, Object>();");
			src.println("	for ( Entry<String, Object> e : getParameters().entrySet() ){");
			src.println("		JSONValue bean = null;");
			src.println("		if ( e.getValue() instanceof JSONValue )");
			src.println("			bean = (JSONValue)e.getValue();");
			src.println("		else if ( e.getValue() instanceof AutoBean<?> )");
			src.println("			bean = (JSONValue)JSONParser.parseStrict(AutoBeanCodex.encode((AutoBean<?>)e.getValue()).getPayload());");
			src.println("		data.put(e.getKey(), bean);");
			src.println("	}");
			src.println("	render(getElement(), data.getJavaScriptObject());");
			src.println("	super.onLoad();");
			src.println("}");
			src.println("");
			src.println("static native void render(Object el, JavaScriptObject params) /*-{");
			src.println("  var t = new $wnd.HoganTemplate();");
			src.println("  t.r = " + template + ";");
			src.println("  el.innerHTML = t.render(params);");
			src.println("}-*/;");

			src.commit(logger);

			System.out.println("Generating for: " + typeName);
			return typeName + "Generated";

		} catch ( Throwable e ){
			logger.log(TreeLogger.ERROR, e.getMessage());
			e.printStackTrace(System.err);
			throw new UnableToCompleteException();
		}
	}

	public SourceWriter getSourceWriter(JClassType classType, GeneratorContext context, TreeLogger logger) {
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Generated";
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
		composer.setSuperclass(classType.getName());

		// Need to add whatever imports your generated class needs.
		composer.addImport("com.google.gwt.core.client.JavaScriptObject");
		composer.addImport("com.google.gwt.json.client.JSONObject");
		composer.addImport("com.google.gwt.json.client.JSONParser");
		composer.addImport("com.google.web.bindery.autobean.shared.AutoBean");
		composer.addImport("com.google.web.bindery.autobean.shared.AutoBeanCodex");
		composer.addImport("com.google.web.bindery.autobean.shared.AutoBeanUtils");
		composer.addImport("java.util.HashMap");
		composer.addImport("java.util.Map");
		composer.addImport("java.util.Map.Entry");
		composer.addImport("com.google.gwt.json.client.JSONValue");

		PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}
}
