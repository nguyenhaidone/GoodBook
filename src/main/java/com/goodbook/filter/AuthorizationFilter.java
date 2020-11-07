package com.goodbook.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodbook.model.UserModel;
import com.goodbook.utils.SessionUtil;

public class AuthorizationFilter implements Filter {
	
	private ServletContext context;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.context = filterConfig.getServletContext();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String url = req.getRequestURI();
		if(url.startsWith("/goodbook-jdbc/admin")) {
			UserModel model = (UserModel) SessionUtil.getInstance().getValue(req, "USERMODEL");
			if(model != null) {
				if(model.getRole().getCode().equals("ADMIN")) {
					chain.doFilter(request, response);
				} else if(model.getRole().getCode().equals("USER")) {
					resp.sendRedirect(req.getContextPath() + "/dang-nhap?action=login&message=not_permission&alert=danger");
				}
			}else {
				resp.sendRedirect(req.getContextPath() + "/dang-nhap?action=login&message=not_login&alert=danger");
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		
	}

}
