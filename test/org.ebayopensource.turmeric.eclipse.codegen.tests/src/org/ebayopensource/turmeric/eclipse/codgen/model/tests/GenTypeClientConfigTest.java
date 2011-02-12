/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codgen.model.tests;


import java.util.Map;

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeClientConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class GenTypeClientConfigTest {

	GenTypeClientConfig model = null;
	@Before
	public void setUp() throws Exception {
		model = new GenTypeClientConfig();
	}

	@After
	public void tearDown() throws Exception {
		model = null;
	}
	
	@Test
	public void testGenTypClientConfigMetaDataDirectoryNull() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeClientConfig.PARAM_MDEST));
	}
	
	@Test
	public void testGenTypClientConfigMetaDataDirectory() {
		model.setMetadataDirectory("metaDataDirectory");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeClientConfig.PARAM_MDEST));
	}

}