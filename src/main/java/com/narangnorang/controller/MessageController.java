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
import com.narangnorang.service.MemberService;
import com.narangnorang.service.MessageService;

@RestController
public class MessageController {

	@Autowired
	MessageService messageService;
	@Autowired
	MemberService memberService;

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
			// 만약 이미 대화방이 표시된 사용자면 제거
			if (otherUsers.contains(messageDTO.getSenderId()) || otherUsers.contains(messageDTO.getRecieverId())) {
				iter.remove();
			} else {
				// sender/reciever가 본인이 아닌 경우 리스트 추가
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
