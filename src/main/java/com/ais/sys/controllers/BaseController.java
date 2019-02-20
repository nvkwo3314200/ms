package com.ais.sys.controllers;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ais.sys.models.ErrorData;
import com.ais.sys.services.RequestService;
import com.ais.sys.services.ResponseDataService;
import com.ais.sys.services.SessionService;

@Controller
public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Resource
	protected ResponseDataService responseDataService;

	@Resource
	protected SessionService sessionService;
	
	@Resource
	protected RequestService requestService;
	/**
	 * 
	 * @param excp
	 *            to support basic exception marshaling to JSON and XML. It will
	 *            determine the format based on the request's Accept header.
	 * 
	 * @throws IOException
	 * @return {@link ErrorData} as a response body
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler({ Exception.class })
	public ErrorData handleException(final Exception excp) throws IOException {
		logger.error(excp.getMessage(), excp);
		return handleErrorInternal(excp.getClass().getSimpleName(),
				excp.getMessage());
	}

	/**
	 * 
	 * @param excp
	 *            to support basic exception marshaling to JSON and XML. It will
	 *            determine the format based on the request's Accept header.
	 * 
	 * @throws IOException
	 * @return {@link ErrorData} as a response body
	 */
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	@ExceptionHandler({ AccessDeniedException.class })
	public ErrorData handleException(final AccessDeniedException excp)
			throws IOException {
		logger.error(excp.getMessage(), excp);
		return handleErrorInternal(excp.getClass().getSimpleName(),
				excp.getMessage());
	}

	protected ErrorData handleErrorInternal(final String errorClass,
			final String errorMsg) {
		final ErrorData errorData = new ErrorData();
		errorData.setClassName(errorClass);
		errorData.setMessage(errorMsg);
		return errorData;
	}
}