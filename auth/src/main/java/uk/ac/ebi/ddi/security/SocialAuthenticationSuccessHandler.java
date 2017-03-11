package uk.ac.ebi.ddi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.security.Service.O2UserDetailsService;
import uk.ac.ebi.ddi.security.Service.TokenAuthenticationService;
import uk.ac.ebi.ddi.security.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SocialAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Autowired
	private O2UserDetailsService userDetialsService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

		// Lookup the complete User object from the database and create an Authentication for it
		final User authenticatedUser = userDetialsService.loadUserByUsername(authentication.getName());

		// Add UserAuthentication to the response
		final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

		tokenAuthenticationService.addAuthentication(response, userAuthentication);

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
