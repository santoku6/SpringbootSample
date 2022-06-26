package com.raxn.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.raxn.entity.JwtTokentable;
import com.raxn.repository.jwtTokentableRepository;
import com.raxn.util.service.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

	@Autowired
	CustomUserDetailsService userDetailsService;
	@Autowired
	JWTUtil jwtUtil;
	@Autowired
	jwtTokentableRepository jwtTokentableRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		LOGGER.info("uri=" + request.getRequestURI() + "");
		String username = null;
		/*
		 * SecurityContext context = SecurityContextHolder.getContext(); Authentication
		 * authentication = context.getAuthentication();
		 * 
		 * if (null == authentication) { LOGGER.info("user not found in session");
		 * response.sendError(HttpServletResponse.SC_FORBIDDEN,"Access denied"); }
		 */
		try {

			final String requestTokenHeader = request.getHeader("Authorization");
			LOGGER.info("requestTokenHeader=" + requestTokenHeader.substring(0, 17));

			String jwtToken = null;

			if (null != requestTokenHeader && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				// LOGGER.info("jwtToken="+jwtToken);
				username = jwtUtil.getUsername(jwtToken);
				request.setAttribute("usernametoken", username);

			} else {
				LOGGER.error("invalid token");
				SecurityContextHolder.clearContext();
				// response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid Token");
			}

			JwtTokentable jwtTableData = jwtTokentableRepo.findByUsername(username);
			if (!jwtToken.equalsIgnoreCase(jwtTableData.getToken())) {
				LOGGER.error("token is not matching");
				SecurityContextHolder.clearContext();
				throw new Exception("token is not matching");
			}

			if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
				final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
						.loadUserByUsername(username);

				if (jwtUtil.validateToken(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					upToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(upToken);
				}
			} else {
				LOGGER.error("invalid token");
				SecurityContextHolder.clearContext();
				// response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid Token");
			}

		} catch (Exception ex) {
			SecurityContextHolder.clearContext();
			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();

			// allow for Refresh Token creation if following conditions are true.
			if (isRefreshToken != null && isRefreshToken.equalsIgnoreCase("true") && requestURL.contains("refreshtoken")
					&& ex instanceof ExpiredJwtException) {
				allowForRefreshToken((ExpiredJwtException) ex, request);
			} else {
				if (ex instanceof ExpiredJwtException) {
					LOGGER.error("invalid token" + ex.getMessage());
					((HttpServletResponse) response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED,
							ex.getMessage());
					return;
				} else {
					LOGGER.error("invalid token" + ex.getMessage());
					// response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
					return;
				}

			}
		}
		filterChain.doFilter(request, response);
	}

	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		// LOGGER.info("in shouldNotFilter()");
		AntPathMatcher pathmatcher = new AntPathMatcher();

		List<String> excludeUrlPatterns = new ArrayList<String>();
		excludeUrlPatterns.add("/rechaxn/footer/**");
		excludeUrlPatterns.add("/rechaxn/login");
		excludeUrlPatterns.add("/rechaxn/offers");
		excludeUrlPatterns.add("/rechaxn/postsuggestion");
		excludeUrlPatterns.add("/rechaxn/faq");
		excludeUrlPatterns.add("/rechaxn/slider");
		excludeUrlPatterns.add("/rechaxn/serviceicons");
		excludeUrlPatterns.add("/rechaxn/register");
		excludeUrlPatterns.add("/rechaxn/getotp");
		excludeUrlPatterns.add("/rechaxn/verifyotp");
		excludeUrlPatterns.add("/rechaxn/resetpwd");
		excludeUrlPatterns.add("/rechaxn/nextlogin");
		excludeUrlPatterns.add("/rechaxn/delete/user/**");
		excludeUrlPatterns.add("/rechaxn/api/**");
		excludeUrlPatterns.add("/rechaxn/abc/**");
		excludeUrlPatterns.add("/rechaxn/mypage/**");
		excludeUrlPatterns.add("/rechaxn/nextlogin");
		excludeUrlPatterns.add("/rechaxn/byesite/**");
		excludeUrlPatterns.add("/rechaxn/checkcode/**");

		return excludeUrlPatterns.stream().anyMatch(p -> pathmatcher.match(p, request.getRequestURI()));

		// return request.getRequestURI().contains("/login");

	}

}
