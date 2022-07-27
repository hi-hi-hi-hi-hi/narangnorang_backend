package com.narangnorang.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.narangnorang.config.auth.PrincipalDetails;
import com.narangnorang.dto.ItemDTO;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.MyItemDTO;
import com.narangnorang.dto.MyRoomDTO;
import com.narangnorang.service.MemberService;
import com.narangnorang.service.MiniroomService;

@RestController
public class MiniroomController {

	@Autowired
	MiniroomService miniroomService;
	@Autowired
	MemberService memberService;

	// 홈 (로그인 O)
	@GetMapping("/api/home")
	public HashMap<String, Object> home(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		MemberDTO mDTO = (MemberDTO)principalDetails.getMemberDTO();
		//		String email = authentication.getName();
//		MemberDTO mDTO = memberService.selectByEmail(email);
		int id = mDTO.getId();
		int privilege = mDTO.getPrivilege();
		MemberDTO memberPointDTO = miniroomService.selectMemberPoint(id);
		int point = memberPointDTO.getPoint();

		MyRoomDTO myRoomDTO = new MyRoomDTO();
		if(privilege == 3){
			myRoomDTO = miniroomService.selectMyRoom(id);
		}
		HashMap<String, Object> result = new HashMap<>();
		result.put("point", point);
		result.put("privilege", privilege);
		result.put("myRoomDTO",myRoomDTO);
		return result;
	}
	
	// 유저 미니홈 불러오기
	@GetMapping("/api/home/profile")
	public MyRoomDTO getUserHome(int id) throws Exception {
		return miniroomService.selectMyRoom(id);
	}
  
	@GetMapping("/api/home/buy")
	public HashMap<String, Object> buy(@RequestParam(value="category",required=false,defaultValue="bed") String category
	,HttpSession session) throws Exception {
		MemberDTO mDTO = (MemberDTO)session.getAttribute("login");
		int id = mDTO.getId();
		HashMap<String, Object> result = new HashMap<>();
		HashMap<String, Object> map = new HashMap<>();
		map.put("category", category);
		map.put("id",id);
		List<ItemDTO> list =  miniroomService.selectAllItems(category);
		List<MyItemDTO> myItemList =  miniroomService.selectAllMyItems(map);
		result.put("itemList",list);
		result.put("myItemList",myItemList);
		return result;
	}

	@GetMapping("/api/home/wish")
	public HashMap<String, Object> wish(HttpSession session) throws Exception {
		MemberDTO mDTO = (MemberDTO)session.getAttribute("login");
		int id = mDTO.getId();
		HashMap<String, Object> result = new HashMap<>();
		List<ItemDTO> list =  miniroomService.selectAllWishItems(id);
		result.put("wishItemList",list);
		return result;
	}

	//물건 구매

	@PostMapping("/api/home/buy")
	public String buy(@RequestBody Map<String, Object> paramMap, HttpSession session) throws Exception {
		Map<String, Object> map2 = (Map<String, Object>) paramMap.get("data");
		int id = (int)map2.get("id");
		int price = (int)map2.get("price");
		MemberDTO mDTO = (MemberDTO)session.getAttribute("login");
		int memberId = mDTO.getId();
		// Check에 쓰임.
		HashMap<String, Object> map = new HashMap<>();
		map.put("itemId", id);
		map.put("memberId", memberId);
		MemberDTO memberPointDTO = miniroomService.selectMemberPoint(memberId);
		int point = memberPointDTO.getPoint();

		//price랑 memberId 등록.
		HashMap<String, Integer> pointMap = new HashMap<>();
		pointMap.put("price", price);
		pointMap.put("memberId", memberId);

		//아이디와 아이템id 이용해서 myItem에 구매할 아이템이 있는지 check로 받아옴.
		MyItemDTO check = miniroomService.selectByMyItemId(map);
		String mesg = null;

		//구매할 아이템이 myItem테이블에 없고 포인트가 price 이상이면 구매. point 없으면 포인트 부족메세지.
		if (check == null) {
				if (point >= price && point >= 0) {
					//point 차감.
					miniroomService.insertBuy(map, pointMap);
					mesg = "구매완료, 포인트가" + price + " 만큼 차감 되었습니다.";
				} else {
					mesg = "포인트가 부족합니다.";
				}

		}else{
			if (check.getWish()==0){
				mesg="이미 구매한 상품입니다.";
			}else if (point >= price && point >= 0){
				miniroomService.wishZero(map, pointMap);
				mesg="구매완료, 위시리스트 상품을 구매했습니다.";
			}else{
				mesg="포인트가 부족합니다.";
			}
		}

		return mesg;
	}

	// 위시리스트 추가
	@PostMapping("api//home/buy/{itemId}")
	public String wishupdate(@PathVariable("itemId") int itemId,HttpSession session) throws Exception{

		MemberDTO mDTO = (MemberDTO)session.getAttribute("login");
		int id = mDTO.getId();
		String mesg;
		int result=0;
		HashMap<String, Object> map = new HashMap<>();
		map.put("itemId",itemId);
		map.put("memberId",id);

		MyItemDTO check = miniroomService.selectByMyItemId(map);
		if(check == null){
			result = miniroomService.wishInsert(map);
			mesg = "위시리스트에 추가 되었습니다.";
		}else{
			if(check.getWish()==0){
				mesg="이미 구매한 상품이므로 위시리스트에 추가되지 않습니다.";
			}else{
				result = miniroomService.wishDelete(map);
				mesg = "위시리스트에서 제거 되었습니다.";
			}
		}


		return mesg;
	}

	//미니룸에 내아이템 적용
	@PutMapping("/api/home/style")
	public int applyMiniroom(HttpSession session,@RequestBody Map<String, Object> paramMap) throws Exception{
		HashMap<String,Object> map = (HashMap<String, Object>) paramMap.get("data");
		MemberDTO mDTO = (MemberDTO)session.getAttribute("login");
		int id = mDTO.getId();
		map.put("memberId", id);
		int result = miniroomService.applyMiniroom(map);
		return result;
	}
}