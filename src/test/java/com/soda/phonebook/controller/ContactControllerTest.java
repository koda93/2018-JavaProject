package com.soda.phonebook.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.google.gson.Gson;
import com.soda.phonebook.domain.Contact;
import com.soda.phonebook.domain.User;
import com.soda.phonebook.dto.req.ContactSaveRequestDto;
import com.soda.phonebook.dto.res.ContactResponseDto;
import com.soda.phonebook.security.SessionConstants;
import com.soda.phonebook.service.ContactService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest(ContactController.class)
public class ContactControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean(name = "contactService")
	private ContactService contactService;
	
	@Test
	public void test_ContactController에서POST로Contact생성() throws Exception {
		Contact contact = Contact.builder()
				.name("테스트연락처").build();
		ContactSaveRequestDto dto= ContactSaveRequestDto.builder()
				.type(contact.getType())
				.name(contact.getName())
				.memo(contact.getMemo()).build();
		Gson gson = new Gson();
		String json = gson.toJson(dto);
		
		mvc.perform(post("/api/contacts/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated())
				.andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void test_ContactController에서Get으로Contact조회() throws Exception {
		
		User user = User.builder()
				.email("test@test.com")
				.name("테스트유저")
				.role(SessionConstants.LOGIN_USER).build();
				
		Contact contact = Contact.builder()
				.id(1l)
				.name("테스트연락처")
				.photo("".getBytes())
				.build();
		ContactResponseDto dto = new ContactResponseDto(contact);
		ContactSaveRequestDto saveDto = ContactSaveRequestDto.builder()
				.type(contact.getType())
				.name(contact.getName())
				.memo(contact.getMemo()).build();
		contactService.create(saveDto, user);
		
		given(contactService.getContacts(1l))
			.willReturn(dto);
		
		mvc.perform(get("/api/contacts/1").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("테스트연락처"))
			.andExpect(jsonPath("$.type").value("DEFAULT"))
			.andDo(MockMvcResultHandlers.print());
	}
}
