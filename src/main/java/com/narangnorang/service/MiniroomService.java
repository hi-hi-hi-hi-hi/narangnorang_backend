package com.narangnorang.service;

import java.util.HashMap;
import java.util.List;

import com.narangnorang.dto.ItemDTO;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.MyItemDTO;
import com.narangnorang.dto.MyRoomDTO;

public interface MiniroomService {

	public List<ItemDTO> selectAllItems(String category);

	public int insertBuy(HashMap<String, Object> map, HashMap<String, Integer> pointMap);

	public int wishInsert(HashMap<String, Object> map);

	public int wishDelete(HashMap<String, Object> map);

	public int applyMiniroom(HashMap<String, Object> map);

	public MyItemDTO selectByMyItemId(HashMap<String, Object> map);

	public List<MyItemDTO> selectAllMyItems(HashMap<String, Object> map);

	public MyRoomDTO selectMyRoom(int id);

	public int wishZero(HashMap<String, Object> map, HashMap<String, Integer> pointMap);

	public List<ItemDTO> selectAllWishItems(int id);

	public MemberDTO selectMemberPoint(int memberId);

}
