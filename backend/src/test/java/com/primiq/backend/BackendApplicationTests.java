package com.primiq.backend;

import com.primiq.backend.model.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
  properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration",
    "app.security.enabled=false",
    "app.keycloak.bootstrap-enabled=false"
  }
)
class BackendApplicationTests {

  @MockitoBean
  private BoardRepository boardRepository;

  @Test
  void contextLoads() {
  }

}
