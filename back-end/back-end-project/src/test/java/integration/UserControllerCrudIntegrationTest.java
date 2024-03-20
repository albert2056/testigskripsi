package integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.controller.path.ProjectPath;
import com.project.helper.ErrorMessage;
import com.project.model.User;
import com.project.model.request.UserRequest;
import com.project.model.response.UserResponse;
import com.project.repository.UserRepository;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.Positive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

public class UserControllerCrudIntegrationTest extends BaseIntegrationTest{

  @Autowired
  private UserRepository userRepository;

  private UserRequest userRequest;

  @BeforeEach
  public void setUp() {
    userRequest = new UserRequest();
    userRequest.setRoleId(1);
    userRequest.setName("name");
    userRequest.setPhoneNumber("12345678");
    userRequest.setEmail("albert@gmail.com");
    userRequest.setPassword("Albert1234");
  }

  @Positive
  @Test
  public void createUser_shouldReturnResponse() throws Exception {
    MvcResult result = mockMvc.perform(
        post(ProjectPath.USER + ProjectPath.CREATE).accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest))).andReturn();
    UserResponse userResponse = getContent(result, new TypeReference<UserResponse>() {
    });

    assertNull(userResponse.getStatusCode());

    User user = this.userRepository.findByIdAndIsDeleted(userResponse.getId(), 0);
    assertNotNull(user);
  }

  @Negative
  @Test
  public void createUser_passwordNotContainUpperCase_shouldReturnErrorResponse() throws Exception {
    userRequest.setPassword("albert1234");

    MvcResult result = mockMvc.perform(
        post(ProjectPath.USER + ProjectPath.CREATE).accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest))).andReturn();
    UserResponse userResponse = getContent(result, new TypeReference<UserResponse>() {
    });

    assertEquals(401, userResponse.getStatusCode());
    assertEquals(ErrorMessage.PASSWORD_UPPERCASE, userResponse.getDescription());
  }

  @Negative
  @Test
  public void createUser_passwordNotContainLowerCase_shouldReturnErrorResponse() throws Exception {
    userRequest.setPassword("ALBERT1234");

    MvcResult result = mockMvc.perform(
        post(ProjectPath.USER + ProjectPath.CREATE).accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest))).andReturn();
    UserResponse userResponse = getContent(result, new TypeReference<UserResponse>() {
    });

    assertEquals(401, userResponse.getStatusCode());
    assertEquals(ErrorMessage.PASSWORD_LOWERCASE, userResponse.getDescription());
  }

  @Negative
  @Test
  public void createUser_passwordNotContainNumericDigit_shouldReturnErrorResponse() throws Exception {
    userRequest.setPassword("albertALBERT");

    MvcResult result = mockMvc.perform(
        post(ProjectPath.USER + ProjectPath.CREATE).accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest))).andReturn();
    UserResponse userResponse = getContent(result, new TypeReference<UserResponse>() {
    });

    assertEquals(401, userResponse.getStatusCode());
    assertEquals(ErrorMessage.PASSWORD_NUMBER, userResponse.getDescription());
  }

  @Negative
  @Test
  public void createUser_passwordLengthLessThan8_shouldReturnErrorResponse() throws Exception {
    userRequest.setPassword("albert");

    MvcResult result = mockMvc.perform(
        post(ProjectPath.USER + ProjectPath.CREATE).accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest))).andReturn();
    UserResponse userResponse = getContent(result, new TypeReference<UserResponse>() {
    });

    assertEquals(401, userResponse.getStatusCode());
    assertEquals(ErrorMessage.PASSWORD_LENGTH, userResponse.getDescription());
  }

  @Negative
  @Test
  public void createUser_invalidEmailFormat_shouldReturnErrorResponse() throws Exception {
    userRequest.setEmail("albert");

    MvcResult result = mockMvc.perform(
        post(ProjectPath.USER + ProjectPath.CREATE).accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest))).andReturn();
    UserResponse userResponse = getContent(result, new TypeReference<UserResponse>() {
    });

    assertEquals(401, userResponse.getStatusCode());
    assertEquals(ErrorMessage.EMAIL, userResponse.getDescription());
  }

  @Positive
  @Test
  public void findUser_shouldReturnResponse() throws Exception {
    User user = new User();
    user.setId(1);
    user.setRoleId(userRequest.getRoleId());
    user.setEmail(userRequest.getEmail());
    user.setName(userRequest.getName());
    user.setPassword(userRequest.getPassword());
    user.setPhoneNumber(userRequest.getPhoneNumber());
    user.setIsDeleted(0);
    userRepository.save(user);

    MvcResult result = mockMvc.perform(
        get(ProjectPath.USER + ProjectPath.FIND_ALL).accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest))).andReturn();
    List<UserResponse> userResponse = getContent(result, new TypeReference<List<UserResponse>>() {
    });

    assertNotNull(userResponse);
  }

}
