package com.ais.sys.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.ais.sys.utils.ConstantUtil;

public class LoginAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(LoginAuthenticationFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException
	{

		String username =  null;
		Map map =request.getParameterMap();
		if(map!=null){
			String[] names= (String[] ) map.get("username");
			if(names!=null  && names.length>0){
				username = names[0];
			}
		}
		
		request.getSession().setAttribute(ConstantUtil.LOGIN_FAILURE_NAME, username);
		if(exception instanceof DisabledException){
			request.getSession().setAttribute(ConstantUtil.LOGIN_FAILURE_TYPE, ConstantUtil.LOGIN_FAILURE_TYPE_DISABLED);
		}else if(exception instanceof BadCredentialsException){
			request.getSession().setAttribute(ConstantUtil.LOGIN_FAILURE_TYPE, ConstantUtil.LOGIN_FAILURE_TYPE_BADCREDENTIALS);
		}else{
			LOG.error(exception.getMessage(), exception);
			request.getSession().setAttribute(ConstantUtil.LOGIN_FAILURE_TYPE, exception.getMessage());
		}

		super.onAuthenticationFailure(request, response, exception);
	}
}
