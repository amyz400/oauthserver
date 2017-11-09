package auth.token;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by aziring on 8/22/17.
 */
public class JdbcTokenStoreService implements Condition {

  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metaData) {
    String tokenStoreType = context.getEnvironment().getProperty("token-store.src");
    return tokenStoreType.equalsIgnoreCase("h2");
  }


}
