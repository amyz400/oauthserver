package auth.application;

import auth.token.InMemoryTokenStoreService;
import auth.token.JdbcTokenStoreService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.client.RestTemplate;

/**
 * The Authorization Manager uses a Authenticator to determine if a client can retrieve an access
 * token.  The authentication configuration defines how a client can be authenticated and certain
 * client details specify access to this authorization manager.  When a resource server's endpoint
 * that requires authentication will validate the access token using the user request mapping.
 *
 * There are 2 ways to create a resource server that uses OAuth2 authentication - using Spring Boot
 * and Spring security.
 *
 * To use Spring Boot, add the @EnableResourceServer to class extending
 * ResourceServerConfigurerAdapter. Override the configure method to specify with request tneed to
 * be authenticated.
 *
 * Example:
 *
 * @Override public void configure(HttpSecurity http) throws Exception {
 * http.authorizeRequests().antMatchers("/test").permitAll() // any request matching /test does not
 * require an access token .anyRequest().authenticated(); }
 *
 * To use Spring Security, do the same as described for the Spring Boot implementation but also add
 * the annotation @EnableWebSecurity to the resource server. Create a resource-servlet.xml that
 * includes the following content:
 *
 * <context:component-scan base-package="packageName." /> <mvc:annotation-driven />
 *
 * Add the servlet mapping to the web.xml file.
 *
 * <servlet> <servlet-name>resource</servlet-name> <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
 * <load-on-startup>1</load-on-startup> </servlet>
 *
 * <servlet-mapping> <servlet-name>resource</servlet-name> <url-pattern>/</url-pattern>
 * </servlet-mapping>
 */
@SpringBootApplication
@EnableResourceServer
@EnableAuthorizationServer
@ComponentScan("rest")
public class AuthManager {

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(AuthManager.class, args);

  }

  @Bean(name = "TokenStore")
  @Conditional(value = InMemoryTokenStoreService.class)
  public TokenStore inMemoryTokenStore() {
    return new InMemoryTokenStore();
  }

  @Bean(name = "TokenStore")
  @Conditional(value = JdbcTokenStoreService.class)
  public TokenStore jdbcTokenStore() {
    return new JdbcTokenStore(dataSource);
  }

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }


}
