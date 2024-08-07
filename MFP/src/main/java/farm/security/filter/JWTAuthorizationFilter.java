package farm.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import farm.member.domain.Member;
import farm.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final MemberRepository memberRepository;

    public JWTAuthorizationFilter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String srcToken = request.getHeader("Authorization");
        if (srcToken == null || !srcToken.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwtToken = srcToken.replace("Bearer ", "");
        String username = JWT.require(Algorithm.HMAC256("project.farm")).build().verify(jwtToken).getClaim("username").asString();
        Optional<Member> opt = memberRepository.findByUsername(username);

        if (opt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        Member findmember = opt.get();

        User user = new User(findmember.getUsername(), findmember.getPassword(),
                AuthorityUtils.createAuthorityList(findmember.getRole().toString()));

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

}
