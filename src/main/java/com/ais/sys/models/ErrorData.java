/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.ais.sys.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Error data wrapper.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ErrorData
{
	private String className;

	private String message;

	public String getClassName()
	{
		return className;
	}

	public void setClassName(final String className)
	{
		this.className = className;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(final String message)
	{
		this.message = message;
	}
}
