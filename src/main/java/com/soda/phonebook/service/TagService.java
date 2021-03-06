package com.soda.phonebook.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soda.phonebook.domain.Contact;
import com.soda.phonebook.domain.Tag;
import com.soda.phonebook.domain.User;
import com.soda.phonebook.dto.req.TagSaveRequestDto;
import com.soda.phonebook.dto.req.TagUpdateRequestDto;
import com.soda.phonebook.dto.res.ContactListReadResponseDto;
import com.soda.phonebook.dto.res.TagResponseDto;
import com.soda.phonebook.repository.TagRepository;

import lombok.AllArgsConstructor;


@Service
@Transactional
@AllArgsConstructor
public class TagService {
	
	private final TagRepository tagRepository;
	private final UserService userService;
	
	@Transactional(readOnly = true)
	public Set<Tag> findAllByContact(Long id){
		Set<Tag> findTags = tagRepository.findAllByContact(id);
		return findTags; 
	}
	
	@Transactional(readOnly = true)
	public Set<TagResponseDto> findAllByUser(User user){
		User findUser = userService.findByEmail(user.getEmail());
		Set<Tag> findTags = tagRepository.findByUser(findUser);
		Set<TagResponseDto> dtoList = new HashSet<>();
		for(Tag tag : findTags)
			dtoList.add(new TagResponseDto(tag));
		return dtoList;
	}
	
	public void delete(Long id) {
		tagRepository.delete(findById(id));
	}
	
	@Transactional(readOnly = true)
	public Set<ContactListReadResponseDto> findContactsByTag(Long id) {
		Tag findTag = findById(id);
		
		Set<Contact> findContactList = findTag.getContacts();
		Set<ContactListReadResponseDto> dtoList = new HashSet<>();
		for(Contact contact : findContactList) {
			dtoList.add(ContactListReadResponseDto.builder()
					.contact(contact)
					.build());
		}
		return dtoList;
	}
	
	public Tag findById(Long id) {
		return tagRepository.findById(id)
			.orElseThrow(()->new IllegalArgumentException("findById error : wrong id"));
	}
	
	public boolean create(TagSaveRequestDto dto, User user) {
		User findUser = userService.findByEmail(user.getEmail());
		Tag tag = dto.toEntity(findUser);
		return tagRepository.save(tag) != null ? true : false;
	}
	
	public boolean update(Long id, TagSaveRequestDto dto) {
		Tag findTag = findById(id);
		findTag.updateName(dto.getName());
		return tagRepository.save(findTag) != null ? true : false;
	}
	
	public void createDafaultTag(User user) {
		Tag tag1 = Tag.builder().name("가족").user(user).build();
		Tag tag2 = Tag.builder().name("친구").user(user).build();
        Tag tag3 = Tag.builder().name("학교").user(user).build();
        Tag tag4 = Tag.builder().name("직장").user(user).build();
        
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
        tagRepository.save(tag4);
	}
	
}
