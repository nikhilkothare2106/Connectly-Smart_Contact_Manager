package com.scm.SCMProject.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class AuthenticatedUserDetails {
    public static String getEmailOfLoggedInUser(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
//            System.out.println(authentication);
//            System.out.println(principal);
            if (principal instanceof OAuth2User) {
                OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
                String clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
                DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

                if (clientId.equalsIgnoreCase("google")) {
                    System.out.println("Google");
                    return oAuth2User.getAttribute("email").toString();

                } else if (clientId.equalsIgnoreCase("github")) {
                    System.out.println("github");
                    String username = oAuth2User.getAttribute("email") != null
                            ? oAuth2User.getAttribute("email").toString()
                            : oAuth2User.getAttribute("login").toString() + "@gmail.com";
                    return username;
                } else {
                    return "default@gmail.com";
                }

            } else if (principal instanceof UserDetails userDetails) {
//                System.out.println("Manual login");
//                System.out.println(userDetails.getUsername());
                return userDetails.getUsername();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
}
