/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc;


import java.io.File;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.IAssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.ConsumeNewServiceWizard;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages.ConsumeNewServiceWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author shrao test for context menu option 'Add/Remove Required Service'
 *         option on Impl project tests: - Add a service and validate - Remove
 *         the added service & validate - Add multiple services & validate -
 *         Remove multiple services & validate
 * 
 */
public class CtxMenuAddRemoveServiceTest extends AbstractTestCase {
	
	static String WSDL_FILE = ServiceSetupCleanupValidate
	.getWsdlFilePath("CalcService.wsdl");
	final String namespacePart = "blogs";

	@Test
	public void testCtxMenuAddRemoveService1() throws Exception {
		
		Boolean b = ConsumerFromIntfTest.createServiceFromWsdl(new File(WSDL_FILE).toURI().toURL(),namespacePart);
		ConsumeNewServiceWizard addRemoveSvcWizard = new ConsumeNewServiceWizard();

		ServiceFromWsdlParamModel model = new ServiceFromWsdlParamModel();
		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();

		model.setServiceName("BlogsCalcServiceV1");
		model.setWorkspaceRootDirectory(PARENT_DIR);

		final String projectName = model.getServiceName();
		final IProject project = WorkspaceUtil.getProject(projectName);

		ConsumeNewServiceWizardPage addRemoveSvcWizardPage = new ConsumeNewServiceWizardPage(
				project);
		AssetInfo service1 = new AssetInfo("Svc1", IAssetInfo.TYPE_PROJECT);
		addRemoveSvcWizardPage.getAddedServices().add(service1); 
		try {
			boolean addSvc = addRemoveSvcWizard.performFinish();
			Assert.assertTrue(
					"Add Services returned false. Check 'Add/Remove Services' context menu on SOA Consumer projects",
					addSvc == true);
			// validate Base<Service>Consumer.java is regenerated with the
			// operations in the added service

		} catch (Exception ex) {
			System.out.println("Exception in testCtxMenuAddRemoveService1: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testCtxMenuAddRemoveService1: "
					+ ex.getLocalizedMessage());
		}
	}

	@Test
	public void testCtxMenuAddRemoveService2() throws Exception {
		ConsumeNewServiceWizard addRemoveSvcWizard = new ConsumeNewServiceWizard();

		ServiceFromWsdlParamModel model = new ServiceFromWsdlParamModel();
		String PARENT_DIR = ServiceSetupCleanupValidate.getParentDir();

		model.setServiceName("BlogsServiceV1");
		model.setWorkspaceRootDirectory(PARENT_DIR);

		final String projectName = model.getServiceName() + "Impl";
		final IProject project = WorkspaceUtil.getProject(projectName);

		ConsumeNewServiceWizardPage addRemoveSvcWizardPage = new ConsumeNewServiceWizardPage(
				project);
		AssetInfo service1 = new AssetInfo("Svc1", IAssetInfo.TYPE_PROJECT);
		addRemoveSvcWizardPage.getRemovedServices().add(service1); // we can
																	// access
																	// removedServices
																	// map like
																	// this...without
																	// set APIs
		try {
			boolean addSvc = addRemoveSvcWizard.performFinish();

			Assert.assertTrue(
					"Remove Services returned false. Check 'Add/Remove Services' context menu on SOA Consumer projects",
					addSvc == true);
			// validate Base<Service>Consumer.java is regenerated without
			// operations from the removed service

		} catch (Exception ex) {
			System.out.println("Exception in testCtxMenuAddRemoveService2: "
					+ ex.getLocalizedMessage());
			Assert.fail("Exception in testCtxMenuAddRemoveService2: "
					+ ex.getLocalizedMessage());
		}
	}
}