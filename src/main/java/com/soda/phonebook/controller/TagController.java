package com.soda.phonebook.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.soda.phonebook.dto.res.TagResponseDto;
import com.soda.phonebook.service.TagService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("/api/tags")
@RestController
public class TagController {
	
	private final TagService tagService;
	
	// tag 전체 목록
	@GetMapping("/")
	@ResponseStatus(value = HttpStatus.OK)
	public Set<TagResponseDto> getAllTags() {
		return tagService.findAllByUser();
	}
	
	// tag 선택시 tag에 속한 연락처 목록 출력 
	// tag 수정
	// tag 삭제 
}