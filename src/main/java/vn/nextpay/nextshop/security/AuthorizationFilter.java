package vn.nextpay.nextshop.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import vn.nextpay.nextshop.constant.Constant;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.enums.EResponseStatus;
import vn.nextpay.nextshop.enums.ResponseStatus;
import vn.nextpay.nextshop.exception.NextshopException;
import vn.nextpay.nextshop.util.Actor;
import vn.nextpay.nextshop.util.TokenHelpers;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public static String MERCHANT_ID = null;
    public static String BEARER_TOKEN = null;
    public static String NP_LOCATION_ID = null;
    public static String NP_USER_ID = null;
    public static String NP_APP_ID = null;
//    private StringRedisTemplate stringRedisTemplate;

    public AuthorizationFilter(AuthenticationManager authenticationManager
//            , StringRedisTemplate stringRedisTemplate
    ) {
        super(authenticationManager);
//        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        // check if existed token header
        if (Strings.isNullOrEmpty(header)) {
            chain.doFilter(req, res);
            return;
        }
        String token = header.replace(Constant.TOKEN_PREFIX, "");
        Claims claims = TokenHelpers.verifyToken(token);
        if (claims == null ){
            res.setHeader("Detail-Message","Wrong Token Authentication");
            res.getWriter().write("Wrong Token Authentication");
            res.getWriter().flush();
            throw new NextshopException(EResponseStatus.FAILED_IN_AUTHENTICATION, "FAILED IN AUTHENTICATION: Claims is null");
        }
        String userId = (String) claims.get(Constant.PREFIX_USER_ID);
        MERCHANT_ID = (String) claims.get(Constant.PREFIX_MERCHANT_ID);
        BEARER_TOKEN = token;
        List<GrantedAuthority> authorities = new ArrayList<>();
        Actor actor = Actor.builder()
                .token(token)
                .merchantId((String) claims.get(Constant.PREFIX_MERCHANT_ID))
                .userId(userId)
                .channel((String) claims.get(Constant.PREFIX_CHANNEL))
                .appParentId((String) claims.get(Constant.PREFIX_APP_PARENT_ID))
                .permissions(claims.get(Constant.PREFIX_PERMISSIONS))
                .isInside(false)
                .build();
//        log.debug(Constant.PREFIX_LOG + " TOKEN = " + JSONFactory.toString(actor));

        context.setAuthentication(new UsernamePasswordAuthenticationToken(userId, actor, authorities));
        chain.doFilter(req, res);
    }
}
