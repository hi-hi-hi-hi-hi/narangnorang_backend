package com.narangnorang.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.MessageDTO;
import com.narangnorang.service.MessageService;

@RestController
public class MessageController {

	@Autowired
	MessageService messageService;

	@GetMapping("/message")
	public Map<String, Object> selectMessageList(HttpSession session) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");

		int userId = memberDTO.getId();

		List<MessageDTO> messageList = messageService.selectMessageList(userId);
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
		
		result.put("messageList", messageList);
		result.put("userId", userId);
		
		return result;
	}

	@PostMapping("/message/counsel")
	public Map<String, Object> sendMessageToCounselor(HttpSession session, @RequestParam Map<String, Object> messageInfo) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		
		messageInfo.put("senderId", memberDTO.getId());
		messageInfo.put("senderName", memberDTO.getName());
		messageInfo.put("senderPrivilege", memberDTO.getPrivilege());
		
		result.put("result", messageService.sendMessage(messageInfo));
		
		return result;
	}

	@GetMapping("/message/chats/{otherId}")
	public Map<String, Object> popupChats(HttpSession session, @PathVariable @ModelAttribute String otherId) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();

		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		int userId = memberDTO.getId();

		map.put("userId", userId);
		map.put("otherId", otherId);

		result.put("chats", messageService.getChats(map));
		
		return result;
	}

	@PostMapping("/message/send")
	public Map<String, Object> sendMessage(@RequestBody Map<String, Object> messageInfo) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("result", messageService.sendMessage(messageInfo));
		
		return result;
	}

	@GetMapping("/message/unread")
	public Map<String, Object> countUnread(HttpSession session) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		
		int userId = memberDTO.getId();
		int unreadCounts = messageService.countUnread(userId);
		
		result.put("unreadCounts", unreadCounts);
		
		return result;
	}
}
