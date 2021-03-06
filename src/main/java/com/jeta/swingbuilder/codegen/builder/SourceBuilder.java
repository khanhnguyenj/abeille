/*
 * Copyright (C) 2005 Jeff Tassin
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.jeta.swingbuilder.codegen.builder;

import com.jeta.forms.store.memento.FormMemento;

/*******************************************************************************
 * This object builds the Java source that corresponds to the given FormMemento.
 * 
 * @author Jeff Tassin
 */
public interface SourceBuilder {
	/**
	 * component id for the JETARegistry
	 */
	public static final String COMPONENT_ID = "jeta.forms.SourceBuilder";
	
	public static final String BUILD_CONSTANT_SIZE = "SourceBuilder.isBuildConstantSize";
	public static final String BUILD_INCLUDE_BINDING = "SourceBuilder.isIncludeBinding";
	
	public void closeBrace();

	public void dedent();

	public void indent();

	public void openBrace();

	public void println();

	public void println(String txt);

	public void print(String txt);

	public void print(char c);
	
	public void buildSourceCustom(ClassDeclarationManager cdm);
	
	public String buildSource(FormMemento fm);
}
