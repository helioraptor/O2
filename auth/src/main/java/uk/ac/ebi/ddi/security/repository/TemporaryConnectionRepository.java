package uk.ac.ebi.ddi.security.repository;

import org.springframework.social.connect.ConnectionFactoryLocator;

/**
 * A short-lived (per request) ConnectionRepository for a single user
 */
public class TemporaryConnectionRepository extends org.springframework.social.connect.mem.InMemoryUsersConnectionRepository{
    public TemporaryConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        super(connectionFactoryLocator);
    }
}
