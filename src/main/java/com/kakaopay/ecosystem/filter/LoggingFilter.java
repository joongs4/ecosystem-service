package com.kakaopay.ecosystem.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kakaopay.ecosystem.terminology.LoggingTerminology;
import com.kakaopay.ecosystem.util.EcosystemUtil;

@Component
public class LoggingFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class.getCanonicalName());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String transactionId = EcosystemUtil.generateGuid();
		try {

			MDC.put(LoggingTerminology.TRANSACTION_ID, transactionId);

			logger.debug("Logging Request ID :{}, IP : {}, Method : {}, URL : {}", transactionId,
					request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
			filterChain.doFilter(request, response);

		} finally {
			logger.debug("Logging Response ID :{}, Status : {}", transactionId, response.getStatus());
			MDC.clear();
		}

	}

}
