package com.primiq.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
  "media.storage.root=target/test-uploads"
})
class MediaControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void cleanStorage() throws IOException {
    Path path = Path.of("target/test-uploads");
    if (!Files.exists(path)) {
      Files.createDirectories(path);
      return;
    }

    try (var stream = Files.list(path)) {
      stream.forEach(file -> {
        try {
          Files.deleteIfExists(file);
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      });
    }
  }

  @Test
  void uploadAndFetchContentWorks() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile(
      "file",
      "hello.txt",
      "text/plain",
      "hello media".getBytes()
    );

    MvcResult uploadResult = mockMvc.perform(multipart("/api/media").file(multipartFile))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.originalFilename").value("hello.txt"))
      .andExpect(jsonPath("$.contentType").value("text/plain"))
      .andReturn();

    String id = JsonPath.read(uploadResult.getResponse().getContentAsString(), "$.id");

    mockMvc.perform(get("/api/media/{id}/content", id))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith("text/plain"))
      .andExpect(content().bytes("hello media".getBytes()));

    mockMvc.perform(get("/api/media/{id}", id))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(id))
      .andExpect(jsonPath("$.contentUrl").value("http://localhost/api/media/" + id + "/content"));

    assertThat(Files.exists(Path.of("target/test-uploads", id + ".bin"))).isTrue();
    assertThat(Files.exists(Path.of("target/test-uploads", id + ".json"))).isTrue();
  }
}
