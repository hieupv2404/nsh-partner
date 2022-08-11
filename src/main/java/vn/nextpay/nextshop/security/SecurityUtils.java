package vn.nextpay.nextshop.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.nextpay.nextshop.enums.EResponseStatus;
import vn.nextpay.nextshop.exception.NextshopException;
import vn.nextpay.nextshop.util.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SecurityUtils {

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional
                .ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof Actor)
                .map(authentication -> ((Actor) authentication.getCredentials()).getToken());
    }

    public static Optional<Actor> getCurrentActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getCredentials() instanceof Actor) {
            return Optional.of((Actor) authentication.getCredentials());
        }
        return Optional.empty();
    }

    public static Actor getRequireActor() {
        return getCurrentActor()
                .orElseThrow(() -> new NextshopException(EResponseStatus.UNAUTHENTICATED));
    }

    public static void setInsideActor() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Actor actor = Actor.builder()
                .isInside(true)
                .build();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(null, actor, authorities));
    }
}
