/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.model;

import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.EnumTypeWizardDetailsPage;

/**
 * UI Model for Enum Type creation
 * @author ramurthy
 *
 */

public class EnumTypeParamModel extends SimpleTypeParamModel {

	private EnumTypeWizardDetailsPage.EnumTableModel[] enumTableModel;

	public EnumTypeWizardDetailsPage.EnumTableModel[] getEnumTableModel() {
		return enumTableModel;
	}

	public void setEnumTableModel(EnumTypeWizardDetailsPage.EnumTableModel[] enumTableModel) {
		this.enumTableModel = enumTableModel;
	}
}