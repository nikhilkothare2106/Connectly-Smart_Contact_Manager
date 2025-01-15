package com.scm.SCMProject.config;

import com.scm.SCMProject.Repository.UserRepo;
import com.scm.SCMProject.entity.Providers;
import com.scm.SCMProject.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken authentication1 = (OAuth2AuthenticationToken) authentication;

        String authorizedClient = authentication1.getAuthorizedClientRegistrationId();
        logger.info(authorizedClient);

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        // oAuth2User.getAttributes().forEach((key, value) -> {
        // logger.info("{}=>{}", key, value);
        // });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setEmailVerified(true);
        user.setEnabled(true);
        if (authorizedClient.equalsIgnoreCase("google")) {
            user.setEmail(oAuth2User.getAttribute("email").toString());
            user.setProfilePic(oAuth2User.getAttribute("picture").toString());
            String name = oAuth2User.getAttribute("name").toString();
            user.setName(name);
            user.setProviderUserId(oAuth2User.getName());
            user.setProvider(Providers.GOOGLE);
            user.setPassword(encoder.encode(name));
            user.setAbout("This account is created using google");
        } else if (authorizedClient.equalsIgnoreCase("github")) {
            String email = oAuth2User.getAttribute("email") != null
                    ? oAuth2User.getAttribute("email").toString()
                    : oAuth2User.getAttribute("login").toString() + "@gmail.com";
            user.setEmail(email);
            user.setProfilePic(oAuth2User.getAttribute("avatar_url").toString());
            String name = oAuth2User.getAttribute("name").toString();
            user.setName(name);
            user.setProviderUserId(oAuth2User.getName());
            user.setProvider(Providers.GITHUB);
            user.setPassword(encoder.encode(name));
            user.setAbout("This account is created using github");

        } else {
            logger.info("OAuthAuthenticationSuccessHandler: Unknown provider");
        }

        User user1 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (user1 == null) {
            userRepo.save(user);
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

}
