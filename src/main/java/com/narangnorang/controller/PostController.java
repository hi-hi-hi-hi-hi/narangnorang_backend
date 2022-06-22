package com.narangnorang.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.PageDTO;
import com.narangnorang.dto.PostDTO;
import com.narangnorang.dto.PostLikerDTO;
import com.narangnorang.dto.ReplyDTO;
import com.narangnorang.service.PostService;

@Controller
public class PostController {

	@Autowired
	PostService postService;
	
	// 게시판 목록 보기
	@ResponseBody
	@GetMapping("/api/post/list")
	public HashMap<String, Object> postList(@RequestParam(defaultValue="자유게시판") String category,
								@RequestParam(defaultValue="1") int p,
								@RequestParam(defaultValue="0") int likes) throws Exception{
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("category", category);
		map.put("likes", likes);
		
		// 페이징
		PageDTO<PostDTO> pageDto = new PageDTO<PostDTO>();
		pageDto.setCurrentPage(p);
		pageDto.setLimit(10);
		pageDto.setTotalRows(postService.totalRecord(map).getTotalRows());
		map.put("pageDto", pageDto);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		LocalDate localDate = LocalDate.now();	
		
		HashMap<String, Object> result = new HashMap<>();
		result.put("postDto", postService.selectAllByCategory(map));
		result.put("pageDto", pageDto);
		result.put("todayDate", dtf.format(localDate));
		
		return result;
	}

	
	// 글 검색
	@ResponseBody
	@GetMapping("/api/post/search")
	public HashMap<String, Object> search(@RequestParam(defaultValue="1") int p,
								String searchCol, String keyword, String category) throws Exception{
		HashMap<String, Object> map = new HashMap<>();
		map.put("searchCol", searchCol);
		map.put("keyword", keyword);
		map.put("category", category);
		
		PageDTO<PostDTO> pageDto = new PageDTO<PostDTO>();
		pageDto.setCurrentPage(p);
		pageDto.setLimit(10);
		pageDto.setTotalRows(postService.searchRecord(map).getTotalRows());
		map.put("pageDto", pageDto);
		
		HashMap<String, Object> result = new HashMap<>();
		result.put("PageDTO", pageDto);
		result.put("PostDTO", postService.search(map));

		return result;
	}
	
	// 자세히 보기
	@ResponseBody
	@GetMapping("/api/post/{id}")
	public PostDTO postRetrieve(@PathVariable int id) throws Exception{
		return postService.selectById(id);
	}
	
	// 댓글 목록
	@ResponseBody
	@GetMapping("/api/post/reply/{id}")
	public List<ReplyDTO> replyList(@PathVariable int id) throws Exception{
		List<ReplyDTO> replyList = postService.selectAllReply(id);
		return replyList;
	}

	// 글 등록
	@ResponseBody
	@PostMapping("/api/post/write")
	public int postWritePro(@RequestBody PostDTO pDto, HttpSession session) throws Exception{
		MemberDTO mDto = (MemberDTO)session.getAttribute("login");

		pDto.setMemberId(mDto.getId());
		pDto.setMemberName(mDto.getName());
		pDto.setMemberPrivilege(mDto.getPrivilege());

		return postService.insert(pDto);
	}
	
	// 게시글 삭제
	@ResponseBody
	@DeleteMapping("/api/post/{id}")
	public int postDelete(@PathVariable int id) throws Exception{
		return postService.delete(id);
	}
	
//	// 게시글 수정 페이지
//	@GetMapping("/post/edit/{id}")
//	public ModelAndView postEdit(@PathVariable int id) throws Exception{
//		PostDTO pDto = postService.selectById(id);
//		ModelAndView mav = new ModelAndView("postEdit");
//		mav.addObject("pDto", pDto);
//		return mav;
//	}
	
	// 게시글 수정
	@ResponseBody
	@PutMapping("/api/post/{id}")
	public int postEditPro(@PathVariable int id, @RequestBody PostDTO pDto) throws Exception{
		return postService.update(pDto);
	}
	
	// 댓글 등록
	@ResponseBody
	@PostMapping("/api/post/reply")
	public int insertReply(HttpSession session, @RequestBody ReplyDTO replyDto) throws Exception{
		HashMap<String, Object> map = new HashMap<>();
		
		MemberDTO mDto = (MemberDTO)session.getAttribute("login");
		replyDto.setMemberId(mDto.getId());
		replyDto.setMemberName(mDto.getName());
		
		map.put("amount", 1);
		map.put("postId", replyDto.getPostId());
		map.put("replyDto", replyDto);
		
		return postService.insertReply(map);
	}
	
	// 댓글 삭제
	@ResponseBody
	@DeleteMapping("/post/reply")
	public int deleteReply(int postId, int replyId) throws Exception{
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("amount", -1);
		map.put("postId", postId);
		map.put("replyId", replyId);
		
		int result = postService.deleteReply(map);
		return result;
	}
	
	// 댓글 수정
	@ResponseBody
	@PutMapping("/post/reply")
	public int updateReplyContent(ReplyDTO replyDto) throws Exception{
		return postService.updateReplyContent(replyDto);
	}
	
	// 게시글 추천
	@ResponseBody
	@PostMapping("/api/post/like/{id}")
	public int insertLiker(HttpSession session, @PathVariable int id)throws Exception{	
		MemberDTO mDto = (MemberDTO)session.getAttribute("login");
		PostLikerDTO postLikerDto = new PostLikerDTO();
		postLikerDto.setPostId(id);
		postLikerDto.setMemberId(mDto.getId());
		
		int result = 0;
		
		List<PostLikerDTO> list = postService.selectPostLiker(postLikerDto);
		if(list.size() >= 1) {
			postLikerDto.setId(list.get(0).getId());
			postService.deletePostLiker(postLikerDto);
			result = -1;
		}else {
			postService.insertPostLiker(postLikerDto);
			result = 1;
		}
		
		return result;
	}
	
	// 에러 처리
	@ExceptionHandler({ Exception.class })
	public String error() throws Exception {
		return "common/error";
	}
}
