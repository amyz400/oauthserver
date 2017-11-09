package rest;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by aziring on 8/22/17.
 */
@RestController
public class AuthResource {

  /**
   * Used by a resouce server to validate an access token
   * @param principal
   * @return
   */
  @RequestMapping("/user")
  public Map<String, String> user(Principal principal) {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("name", principal.getName());
    return map;
  }


}
