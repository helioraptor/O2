package uk.ac.ebi.ddi.security.repository;

import org.springframework.security.core.AuthenticationException;
import org.springframework.social.connect.*;
import uk.ac.ebi.ddi.security.Service.O2UserDetailsService;
import uk.ac.ebi.ddi.security.model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Simplified version of the JdbcUsersConnectionRepository that does not persist multiple connections to/from users.
 * This repository works with a one-to-one relation between between User and Connection
 * Note that it uses the JPA based UserService so no custom SQL is used
 */
public class SimpleUsersConnectionRepository implements UsersConnectionRepository {

    private O2UserDetailsService userDetailsService;

    private ConnectionFactoryLocator connectionFactoryLocator;

    private ConnectionSignUp connectionSignUp;

    public SimpleUsersConnectionRepository(O2UserDetailsService userService, ConnectionFactoryLocator connectionFactoryLocator) {
        System.out.println("-----> SimpleUsersConnectionRepository.ctor");

        this.userDetailsService = userService;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        System.out.println("-----> SimpleUsersConnectionRepository.findUserIdsWithConnection");
        try {
            User user = userDetailsService.loadUserByConnectionKey(connection.getKey());
            user.setAccessToken(connection.createData().getAccessToken());
            userDetailsService.updateUserDetails(user);
            return Arrays.asList(user.getUserId());
        } catch (AuthenticationException ae) {
            System.out.println("-----> SimpleUsersConnectionRepository.findUserIdsWithConnection exception, connectionSignUp.execute");
            return Arrays.asList(connectionSignUp.execute(connection));
        }
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        System.out.println("-----> SimpleUsersConnectionRepository.findUserIdsConnectedTo");
        Set<String> keys = new HashSet<>();
        for (String userId: providerUserIds) {
            ConnectionKey ck = new ConnectionKey(providerId, userId);
            try {
                keys.add(userDetailsService.loadUserByConnectionKey(ck).getUserId());
            } catch (AuthenticationException ae) {
                //ignore
            }
        }
        return keys;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        System.out.println("-----> SimpleUsersConnectionRepository.createConnectionRepository");

        final ConnectionRepository connectionRepository = (new TemporaryConnectionRepository(connectionFactoryLocator)).createConnectionRepository(userId);

        final User user = userDetailsService.loadUserByUserId(userId);
        final ConnectionData connectionData = new ConnectionData(
                user.getProviderId(),
                user.getProviderUserId(),
                null, null, null,
                user.getAccessToken(),
                null, null, null);

        final Connection connection = connectionFactoryLocator
                .getConnectionFactory(user.getProviderId()).createConnection(connectionData);
        connectionRepository.addConnection(connection);
        return connectionRepository;
    }

    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }
}
