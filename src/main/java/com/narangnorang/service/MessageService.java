package com.narangnorang.service;

import java.util.List;
import java.util.Map;

import com.narangnorang.dto.MessageDTO;

public interface MessageService {

	public List<MessageDTO> getMessageList(int userId) throws Exception;

	public List<MessageDTO> getMessageHistory(Map<String, Object> map) throws Exception;

	public List<Integer> getUnreads(int userId) throws Exception;

	public int sendMessage(Map<String, Object> map) throws Exception;

	public int readMessages(Map<String, Object> map) throws Exception;

}
