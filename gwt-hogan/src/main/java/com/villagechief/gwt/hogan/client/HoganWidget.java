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
package com.villagechief.gwt.hogan.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public abstract class HoganWidget extends ComplexPanel implements HasParameters {
	protected Map<String, Object> parameters = new HashMap<String, Object>();

	public HoganWidget() {
		setElement(DOM.createDiv());
	}

	public void addParameter(String key, Object value) {
		if (value == null) {
			parameters.put(key, JSONNull.getInstance());
		} else if (value instanceof JSONValue || value instanceof AutoBean) {
			parameters.put(key, value);
		} else {
			AutoBean<?> bean = AutoBeanUtils.getAutoBean(value);
			if (bean == null)
				throw new IllegalArgumentException(key + " must be loaded into AutoBean");
			parameters.put(key, bean);
		}
	}

	public Object getParameter(String key) {
		return parameters.get(key);
	}

	protected Map<String, Object> getParameters() {
		return parameters;
	}

	@Override
	public void add(Widget child) {
		super.add(child, getElement());
	}

	public void add(Widget child, GQuery container) {
		child.removeFromParent();

		// Logical attach.
		getChildren().add(child);

		// Physical attach.
		container.append(child.getElement());

		// Adopt.
		adopt(child);
	}

}