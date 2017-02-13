package com.usermanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static com.usermanagement.constants.TestConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.usermanagement.boot.Application;
import com.usermanagement.model.User;
import com.usermanagement.model.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserControllerTest {
	
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertThat(mappingJackson2HttpMessageConverter).isNotNull();
    }

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.build();

        userRepository.deleteAllInBatch();
        setupUsers(USER_1, USER_2);
    }

    @Test
    public void testGetAllUsers() throws Exception { 
        mockMvc.perform(get("/users"))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(contentType))
        		.andExpect(jsonPath("$", hasSize(2)))
        		.andExpect(jsonPath("$[0].email", containsString("john.david@gmail.com")))
        		.andExpect(jsonPath("$[1].email", containsString("jennifer.rose@outlook.com")));
    }
    
    @Test
    public void testAddUser() throws Exception {
        String userJson = json(USER_3);

        mockMvc.perform(post("/users")
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isOk());

        List<User> users = userRepository.findAll();
        assertThat(users.iterator()).containsOnly(USER_1, USER_2, USER_3);
    }
    
    @Test
    public void testAddExistingEmailUser() throws Exception {
        String userJson = json(USER_4);

        mockMvc.perform(post("/users")
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isConflict());

        List<User> users = userRepository.findAll();
        assertThat(users.iterator()).containsOnly(USER_1, USER_2);
    }

    @Test
    public void testDeleteUser() throws Exception {
        String userJson = json(USER_2);

        mockMvc.perform(delete("/users")
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        List<User> users = userRepository.findAll();
        assertThat(users.iterator()).containsOnly(USER_1);
    }
    
    @Test
    public void testDeleteNonExistentUser() throws Exception {
        String userJson = json(USER_3);

        mockMvc.perform(delete("/users")
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        List<User> users = userRepository.findAll();
        assertThat(users.iterator()).containsOnly(USER_1, USER_2);
    }
    
    @Test
    public void testFindAverageAge() throws Exception {
    	long years = ChronoUnit.YEARS.between(AVG_AGE, NOW);
    	setupUsers(USER_3);
        
    	mockMvc.perform(get("/users/avgAge")
                .param("startDate", START_DATE)
                .param("endDate", END_DATE))
                .andExpect(status().isOk())
                .andExpect(content().string(Long.toString(years)));
    }
    
    private void setupUsers(User... users) throws Exception {
		for(User user: users) {
			userRepository.save(user);
		}
	}

	private String json(Object obj) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                obj, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
