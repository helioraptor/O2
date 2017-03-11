package uk.ac.ebi.ddi.security.Service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.security.SocialUserDetailsService;
import uk.ac.ebi.ddi.security.model.User;

public interface O2UserDetailsService extends SocialUserDetailsService, UserDetailsService {

    User loadUserByConnectionKey(ConnectionKey connectionKey);
    User loadUserByUserId(String userId);
    User loadUserByUsername(String username);
    void updateUserDetails(User user);
}
