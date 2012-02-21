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

import java.util.Date;

public class TestDataImpl implements TestData{
	private String field1;
	private Date dateField;
	private Integer number;
	
	public String getField1() {
		return field1;
	}
	public void setField1(String v) {
		this.field1 = v;
	}

	public Date getDateField() {
		return dateField;
	}
	public void setDateField(Date v) {
		this.dateField = v;
	}

	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer v) {
		this.number = v;
	}
}
