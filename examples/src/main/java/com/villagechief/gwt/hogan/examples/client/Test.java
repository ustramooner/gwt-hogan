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
package com.villagechief.gwt.hogan.examples.client;

import static com.google.gwt.query.client.GQuery.*;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.villagechief.gwt.hogan.client.HoganWidget;

public class Test extends HoganWidget {
	Button b = new Button();

	public Test() {
		b.setText("GWT Button");

		// can add java beans as long as it's been passed to the AutoBean
		// Factory...
		TestDataFactory beanFactory = GWT.create(TestDataFactory.class);
		TestData test1 = beanFactory.data().as(); // just track the object, we
													// can look it up again
													// later...
		test1.setField1("test");
		test1.setNumber(1);
		test1.setDateField(new Date());
		addParameter("data", test1);

		// can also use an an impelemented object
		TestData test2 = new TestDataImpl();
		test2.setField1("<b>test2</b>");
		test2.setNumber(1);
		test2.setDateField(new Date());
		addParameter("data2", beanFactory.create(TestData.class, test2));

		// or pass in json object directly
		JSONArray arr = new JSONArray();
		arr.set(0, new JSONString("item 1"));
		arr.set(1, new JSONString("item 2"));
		arr.set(2, new JSONString("item 3"));
		addParameter("list", arr);

		// gwt events work...
		b.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.alert("button click (gwt event)");
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		// since the javascript is only rendered on load, we can only interact
		// with the template at this point

		// add a gwt widget into a div using a gquery selector
		add(b, $("#placeholder1"));

		// and gquery also works
		$("#button1").click(new Function() {
			public boolean f(Event e) {
				Window.alert("button click (gquery event)");
				return true;
			}
		});

		// do a lame gquery animation
		$("#button1").animate("top:'+=500'", 500, new Function() {
			public void f(Element e) {
				$(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
				$(e).animate("top:'-=500'");
			}
		});
	}
}
