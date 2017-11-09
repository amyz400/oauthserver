package auth.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

/**
 * Created by aziring on 8/22/17.
 */
@Component
public class AuthManagerConfigurer extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private TokenStore tokenStore;

  /**
   * A custom token services allows the expiration time of a token be configurable.
   * @return
   */
  private AuthorizationServerTokenServices customTokenServices(){
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenStore(tokenStore);
    tokenServices.setSupportRefreshToken(true);
    tokenServices.setAccessTokenValiditySeconds(expirationTime);

    return tokenServices;
  }

  /**
   * Configure details about the endpoints that not related to security.  A custom
   * token services is used to control expiration times and the authentication provider
   * is supplied.
   * @param endpoints
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.tokenServices(customTokenServices());
    endpoints.authenticationManager(authenticationManager);

  }

  /**
   * Declare a client and its properties that define what it has permission to access.
   * @param configurer
   * @throws Exception
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
    configurer.inMemory().withClient(user).secret(secret).authorizedGrantTypes(grantTypes).scopes(scope);
  }
  /**
   * Client id would need to match the provided client secret requested by the OAuth2 Client.
   * This configuration assumes 1 unique client id/client secret combination.  If multiple
   * client id/client secret combinations are to grant access tokens, code modifications would be
   * required.  This may include changes to the application.properties file and/or a new database
   * table to be created and used.
   */
  @Value("${security.client.user}")
  private String user;

  @Value("${security.client.secret}")
  private String secret;

  /**
   * Grant types describe what type of grants can be provided with the access token
   */
  @Value("${security.client.auth-grant-types}")
  private String grantTypes;
  /**
   * Client scope would need to match the provided scope requested by the OAuth2 Client
   * @param scope
   */
  @Value("${security.client.scope}")
  private String scope;


  @Value("${token.expiration-duration}")
  private int expirationTime;


  @Autowired
  private AuthenticationManager authenticationManager;



}
