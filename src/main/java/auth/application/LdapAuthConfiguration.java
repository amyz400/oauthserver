package auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

/**
 * This class configures Active Directory LDAP authentication against the active
 * directory domain and url defined in the application.properties.  When this authentication
 * configuration is used, the client is expected to authenticate with its LDAP username and password.
 */
@Configuration
public class LdapAuthConfiguration extends WebSecurityConfigurerAdapter {

  /**
   * Create a bean that is configured to a specific LDAP repository.
   * @return
   */
  @Bean
  public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
    ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);
    //provider.setConvertSubErrorCodesToExceptions(true);

    return provider;
  }

  /**
   * Configure to authenticate using the Active Directory authentication provider
   * @param auth
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
  }

  @Value("${ldap.domain}")
  private String ldapDomain;

  @Value("${ldap.url}")
  private String ldapUrl;

}
