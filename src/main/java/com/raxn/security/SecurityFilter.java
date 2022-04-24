package com.raxn.security;

/*
 * @Component public class SecurityFilter extends OncePerRequestFilter {
 * 
 * @Autowired private JWTUtil jwtUtil;
 * 
 * @Autowired private UserDetailsService userDetailsService;
 * 
 * protected void doFilterInternal(HttpServletRequest request,
 * HttpServletResponse response, FilterChain filterChain) throws
 * ServletException, IOException {
 * 
 * String token = request.getHeader("Authorization");
 * 
 * 
 * if(null == token || !token.startsWith("Bearer ")) { ((HttpServletResponse)
 * response).sendError(HttpServletResponse.SC_BAD_REQUEST,
 * "Authorization token is not valid"); }
 * 
 * 
 * 
 * 
 * if (token != null) { token = token.substring(7); // read username from token
 * String username = jwtUtil.getUsername(token); // user name exist, user did
 * not login before if (username != null &&
 * SecurityContextHolder.getContext().getAuthentication() == null) { // load
 * current user from Database UserDetails user =
 * userDetailsService.loadUserByUsername(username); //validate token boolean
 * isValid = jwtUtil.validateToken(token, user.getUsername()); if(isValid) { //
 * Authentication Impl object UsernamePasswordAuthenticationToken authToken =
 * new UsernamePasswordAuthenticationToken( user.getUsername(),
 * user.getPassword(), user.getAuthorities()); // link with request
 * authToken.setDetails(new
 * WebAuthenticationDetailsSource().buildDetails(request)); // Link
 * Authentication into SecurityContext
 * SecurityContextHolder.getContext().setAuthentication(authToken); } } }
 * filterChain.doFilter(request, response); } }
 */
