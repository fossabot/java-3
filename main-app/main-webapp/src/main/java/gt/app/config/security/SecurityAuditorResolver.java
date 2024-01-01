package gt.app.config.security;

import gt.app.domain.AppUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class SecurityAuditorResolver implements AuditorAware<AppUser> {

    private final EntityManager entityManager;

    @Override
    public Optional<AppUser> getCurrentAuditor() {

        UUID userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(entityManager.getReference(AppUser.class, userId));
    }
}
