package io.rashed.bank.config;

import io.rashed.bank.jwt.ReactiveJwtAuthConverter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ReactiveJwtAuthConverter reactiveJwtAuthConverter;

    public SecurityConfig(ReactiveJwtAuthConverter reactiveJwtAuthConverter) {
        this.reactiveJwtAuthConverter = reactiveJwtAuthConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**")
                        .permitAll()
                        .anyExchange().authenticated()
                ).oauth2ResourceServer((oauth) ->
                        oauth.jwt(spec -> spec
                                .jwtAuthenticationConverter(reactiveJwtAuthConverter)))
                .build();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("resource", r -> r.path("/resource")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("http://localhost:9000"))
                .build();
    }
}
