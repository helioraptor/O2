package uk.ac.ebi.ddi.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ebi.ddi.security.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findById(Long id);

	User findByProviderIdAndProviderUserId(String providerId, String providerUserId);
}
