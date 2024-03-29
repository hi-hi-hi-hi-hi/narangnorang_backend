package com.narangnorang.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.narangnorang.dto.ItemDTO;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.MyItemDTO;
import com.narangnorang.dto.MyRoomDTO;

@Repository
public class MiniroomDAO {

	@Autowired
	private SqlSession sqlSession;

	public List<ItemDTO> selectAllItems(String category) {
		return sqlSession.selectList("com.config.MiniroomMapper.selectAllItems", category);
	}

	public int insertBuy(HashMap<String, Object> map) {
		return sqlSession.insert("com.config.MiniroomMapper.insertBuy", map);
	}

	public int insertDefaultItems(String name) {
		return sqlSession.insert("com.config.MiniroomMapper.insertDefaultItems", name);
	}

	public int insertDefaultMyItems1() {
		return sqlSession.insert("com.config.MiniroomMapper.insertDefaultMyItems1");
	}

	public int insertDefaultMyItems2() {
		return sqlSession.insert("com.config.MiniroomMapper.insertDefaultMyItems2");
	}

	public int applyMiniroom(HashMap<String, Object> map) {
		return sqlSession.update("com.config.MiniroomMapper.applyMiniroom", map);
	}

	public MyItemDTO selectByMyItemId(HashMap<String, Object> map) {
		return sqlSession.selectOne("com.config.MiniroomMapper.selectByMyItemId", map);
	}

	public List<MyItemDTO> selectAllMyItems(HashMap<String, Object> map) {
		return sqlSession.selectList("com.config.MiniroomMapper.selectAllMyItems", map);
	}

	public MyRoomDTO selectMyRoom(int id) {
		return sqlSession.selectOne("com.config.MiniroomMapper.selectMyRoom", id);
	}

	public int updatePoint(HashMap<String, Integer> pointMap) {
		return sqlSession.update("com.config.MiniroomMapper.updatePoint", pointMap);
	}

	public int wishInsert(HashMap<String, Object> map) {
		return sqlSession.insert("com.config.MiniroomMapper.wishInsert", map);
	}

	public int wishDelete(HashMap<String, Object> map) {
		return sqlSession.delete("com.config.MiniroomMapper.wishDelete", map);
	}

	public int wishZero(HashMap<String, Object> map) {
		return sqlSession.update("com.config.MiniroomMapper.wishZero", map);
	}

	public List<ItemDTO> selectAllWishItems(int id) {
		return sqlSession.selectList("com.config.MiniroomMapper.selectAllWishItems", id);
	}

	public MemberDTO selectMemberPoint(int memberId) {
		return sqlSession.selectOne("com.config.MiniroomMapper.selectMemberPoint", memberId);
	}

}
