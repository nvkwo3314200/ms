package com.ais.ms.report;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ais.ms.report.annotation.ReportFormHandler;

@Component
@ReportFormHandler
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SystemParametersHandler extends ReportHandler {
}
