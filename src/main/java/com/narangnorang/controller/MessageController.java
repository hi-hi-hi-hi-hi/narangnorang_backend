package com.narangnorang.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.narangnorang.config.auth.PrincipalDetails;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.MessageDTO;
import com.narangnorang.service.MessageService;

@RestController
public class MessageController {

	@Autowired
	private MessageService messageService;

	@GetMapping("/api/message/list")
	public Map<String, Object> getMessageList(@AuthenticationPrincipal PrincipalDetails principalDetails)
			throws Exception {
		MemberDTO memberDTO = principalDetails.getMemberDTO();
		int userId = memberDTO.getId();
		List<MessageDTO> messageList = messageService.getMessageList(userId);

		Iterator<MessageDTO> iter = messageList.iterator();
		List<Integer> otherUsers = new ArrayList<Integer>();
		while (iter.hasNext()) {
			MessageDTO messageDTO = iter.next();
			if (otherUsers.contains(messageDTO.getSenderId()) || otherUsers.contains(messageDTO.getRecieverId())) {
				iter.remove();
			} else {
				if (userId != messageDTO.getSenderId()) {
					otherUsers.add(messageDTO.getSenderId());
				}
				if (userId != messageDTO.getRecieverId()) {
					otherUsers.add(messageDTO.getRecieverId());
				}
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("messageList", messageList);
		return result;
	}

	@GetMapping("/api/message/history")
	public Map<String, Object> getMessageHistory(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestParam Map<String, Object> map) throws Exception {
		MemberDTO memberDTO = principalDetails.getMemberDTO();
		int userId = memberDTO.getId();
		map.put("userId", userId);
		List<MessageDTO> messageHistory = messageService.getMessageHistory(map);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("messageHistory", messageHistory);
		return result;
	}

	@GetMapping("/api/message/unreads")
	public Map<String, Object> getUnreads(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		MemberDTO memberDTO = principalDetails.getMemberDTO();
		int userId = memberDTO.getId();
		List<Integer> unreads = messageService.getUnreads(userId);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("unreads", unreads);
		return result;
	}

	@GetMapping("/api/message/read")
	public Map<String, Object> readMessages(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestParam Map<String, Object> map) throws Exception {
		MemberDTO memberDTO = principalDetails.getMemberDTO();
		int userId = memberDTO.getId();
		map.put("userId", userId);
		int cnt = messageService.readMessages(map);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cnt", cnt);
		return result;
	}

}
