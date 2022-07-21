package com.narangnorang.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.narangnorang.dto.MessageDTO;

@Repository("messageDAO")
public class MessageDAO {

	@Autowired
	SqlSession sqlSession;

	public List<MessageDTO> getMessageList(int userId) throws Exception {
		return sqlSession.selectList("com.config.MessageMapper.getMessageList", userId);
	}

	public int readMessages(Map<String, Object> map) {
		return sqlSession.update("com.config.MessageMapper.readMessages", map);
	}

	public List<MessageDTO> getMessageHistory(Map<String, Object> map) throws Exception {
		return sqlSession.selectList("com.config.MessageMapper.getMessageHistory", map);
	}

	public List<MessageDTO> getUnreads(int userId) throws Exception {
		return sqlSession.selectList("com.config.MessageMapper.getUnreads", userId);
	}

	public int sendMessage(Map<String, Object> map) throws Exception {
		return sqlSession.insert("com.config.MessageMapper.sendMessage", map);
	}

}
