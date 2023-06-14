package com.vandu.config;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.vandu.helper.AuthenticationType;
import com.vandu.helper.TypeRole;
import com.vandu.model.Role;
import com.vandu.model.User;
import com.vandu.model.UserRole;
import com.vandu.service.RoleService;
import com.vandu.service.UserRoleService;
import com.vandu.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String redirectUrl = "";

		processOAuth2UserAuthentication(authentication,request);
		
		redirectUrl = "/";
		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);

	}

	private void processOAuth2UserAuthentication(Authentication authentication,HttpServletRequest request) {

		if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
			DefaultOAuth2User auth2User = (DefaultOAuth2User) authentication.getPrincipal();
			
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			String clientName = oauthToken.getAuthorizedClientRegistrationId();
			
			AuthenticationType authType = AuthenticationType.valueOf(clientName.toUpperCase());
			System.err.println(authType.toString());
			String email = getEmailFromAuth2User(auth2User);
			
			User checkUser = new User();
			
			if(authType.equals(AuthenticationType.FACEBOOK)) {
				email = auth2User.getAttribute("id");
				checkUser = userService.findByUsernameAndAuthenticationType(email, authType).orElse(null);
			}else {
				checkUser = userService.findByUsernameAndAuthenticationType(email, authType).orElse(null);
			}
			
		
			
			saveLoggedUserToSession(request, email, authType);
			
			if (checkUser == null) {
				createUserFromAuth2User(auth2User, email, authType);
				
			}else {
				checkUser.setEmail(email);
				checkUser.setFullname(auth2User.getAttribute("name") != null ? auth2User.getAttribute("name")
	                    : auth2User.getAttribute("login"));
	            userService.save(checkUser);

			}
		}else {
			saveLoggedUserToSession(request,authentication.getName(),AuthenticationType.LOCAL);
		}
	}

	private String getEmailFromAuth2User(DefaultOAuth2User auth2User) {
		String email = auth2User.getAttribute("email") != null ? auth2User.getAttribute("email")
				: auth2User.getAttribute("id") + "@gmail.com";

		return email;
	}

	private void createUserFromAuth2User(DefaultOAuth2User auth2User, String email,
			AuthenticationType authType) {
		User user = new User();
		
		if (authType.equals(AuthenticationType.FACEBOOK)) {
			String username = auth2User.getAttribute("id");
			
			user.setUsername(username);
			System.out.println("demooooooooo");
		} else {
			user.setUsername(email);
		}
		user.setEmail(email);
		user.setAuthenticationType(authType);
		user.setCreateDate(new Date());
		user.setActive(true);
		user.setPassword("dangnhapbangoath2222");
		user.setFullname(auth2User.getAttribute("name") != null ? auth2User.getAttribute("name")
				: auth2User.getAttribute("login"));

		userService.save(user);

		createRoleByUser(user);

	}
	
	private void createRoleByUser(User user) {
		Role role = roleService.findByName(TypeRole.USER).orElse(null);

		UserRole userRole = new UserRole();
		userRole.setRole(role);
		userRole.setUser(user);

		userRoleService.save(userRole);
	}
	
	private void saveLoggedUserToSession(HttpServletRequest request,String username,AuthenticationType authType) {
		User user = new User();
		user.setUsername(username);
		user.setAuthenticationType(authType);
		request.getSession().setAttribute("loggedUser", user);
		System.err.println(authType);
	}

}
