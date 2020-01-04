package com.kakaopay.ecosystem.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kakaopay.ecosystem.exception.BadRequestException;
import com.kakaopay.ecosystem.exception.ExceptionResponse;
import com.kakaopay.ecosystem.util.EcosystemUtil;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class ExceptionHandlingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			filterChain.doFilter(request, response);
		} catch (BadRequestException badRequestException) {

			ExceptionResponse exceptionResponse = new ExceptionResponse(badRequestException.getBody());
			response.getWriter().write(EcosystemUtil.convertExceptionToJson(exceptionResponse));
			response.setStatus(HttpStatus.BAD_REQUEST.value());

		} catch (Exception e) {
			BadRequestException badRequestException = new BadRequestException(e);
			ExceptionResponse exceptionResponse = new ExceptionResponse(badRequestException.getBody());
			response.getWriter().write(EcosystemUtil.convertExceptionToJson(exceptionResponse));
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		}
	}

}
