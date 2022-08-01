package com.narangnorang.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narangnorang.dao.MessageDAO;
import com.narangnorang.dto.MessageDTO;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDAO messageDAO;

	@Override
	public List<MessageDTO> getMessageList(int userId) throws Exception {
		return messageDAO.getMessageList(userId);
	}

	@Transactional
	@Override
	public List<MessageDTO> getMessageHistory(Map<String, Object> map) throws Exception {
		messageDAO.readMessages(map);
		return messageDAO.getMessageHistory(map);
	}

	@Override
	public List<Integer> getUnreads(int userId) throws Exception {
		return messageDAO.getUnreads(userId);
	}

	@Override
	public int sendMessage(Map<String, Object> map) throws Exception {
		return messageDAO.sendMessage(map);
	}

	@Override
	public int readMessages(Map<String, Object> map) throws Exception {
		return messageDAO.readMessages(map);
	}

}
