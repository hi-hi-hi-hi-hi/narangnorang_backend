package com.narangnorang.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.narangnorang.config.auth.PrincipalDetails;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.NotificationDTO;
import com.narangnorang.dto.PageDTO;
import com.narangnorang.dto.PostDTO;
import com.narangnorang.dto.PostLikerDTO;
import com.narangnorang.dto.ReplyDTO;
import com.narangnorang.dto.ReplyLikerDTO;
import com.narangnorang.service.PostService;

@RestController
public class PostController {

	@Autowired
	private PostService postService;

	// 게시판 목록 보기
	@GetMapping("/api/post/list")
	public HashMap<String, Object> postList(@RequestParam(defaultValue = "자유게시판") String category,
			@RequestParam(defaultValue = "1") int p, @RequestParam(defaultValue = "0") int likes) throws Exception {

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
	@GetMapping("/api/post/search")
	public HashMap<String, Object> search(@RequestParam(defaultValue = "1") int p, String searchCol, String keyword,
			String category) throws Exception {
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
	@GetMapping("/api/post/{id}")
	public PostDTO postRetrieve(@PathVariable int id) throws Exception {
		return postService.selectById(id);
	}

	// 댓글 목록
	@GetMapping("/api/post/reply/{id}")
	public List<ReplyDTO> replyList(@PathVariable int id) throws Exception {
		List<ReplyDTO> replyList = postService.selectAllReply(id);
		return replyList;
	}

	// 글 등록
	@PostMapping("/api/post/write")
	public int postWritePro(@RequestBody PostDTO pDto, @AuthenticationPrincipal PrincipalDetails principalDetails)
			throws Exception {
		MemberDTO mDto = principalDetails.getMemberDTO();

		pDto.setMemberId(mDto.getId());
		pDto.setMemberName(mDto.getName());
		pDto.setMemberPrivilege(mDto.getPrivilege());

		return postService.insert(pDto);
	}

	// 게시글 삭제
	@DeleteMapping("/api/post/{id}")
	public int postDelete(@PathVariable int id) throws Exception {
		return postService.delete(id);
	}

	// 게시글 수정
	@PutMapping("/api/post/{id}")
	public int postEditPro(@PathVariable int id, @RequestBody PostDTO pDto) throws Exception {
		return postService.update(pDto);
	}

	// 댓글 등록
	@PostMapping("/api/post/reply")
	public int insertReply(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody ReplyDTO replyDto)
			throws Exception {
		HashMap<String, Object> map = new HashMap<>();

		MemberDTO mDto = principalDetails.getMemberDTO();
		replyDto.setMemberId(mDto.getId());
		replyDto.setMemberName(mDto.getName());
		PostDTO pDto = postService.selectById(replyDto.getPostId());

		map.put("amount", 1);
		map.put("replyDto", replyDto);
		map.put("postDto", pDto);

		return postService.insertReply(map);
	}

	// 댓글 삭제
	@DeleteMapping("/api/post/reply")
	public int deleteReply(int postId, int replyId) throws Exception {
		HashMap<String, Object> map = new HashMap<>();

		map.put("amount", -1);
		map.put("replyId", replyId);
		PostDTO pDto = postService.selectById(postId);
		map.put("postDto", pDto);

		int result = postService.deleteReply(map);
		return result;
	}

	// 댓글 수정
	@PutMapping("/api/post/reply")
	public int updateReplyContent(@RequestBody ReplyDTO replyDto) throws Exception {
		return postService.updateReplyContent(replyDto);
	}

	// 게시글 추천
	@PostMapping("/api/post/like/{id}")
	public int insertLiker(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int id)
			throws Exception {
		MemberDTO mDto = principalDetails.getMemberDTO();
		PostLikerDTO postLikerDto = new PostLikerDTO();
		postLikerDto.setPostId(id);
		postLikerDto.setMemberId(mDto.getId());

		int result = 0;

		List<PostLikerDTO> list = postService.selectPostLiker(postLikerDto);
		if (list.size() >= 1) {
			postLikerDto.setId(list.get(0).getId());
			postService.deletePostLiker(postLikerDto);
			result = -1;
		} else {
			postService.insertPostLiker(postLikerDto);
			result = 1;
		}
		return result;
	}

	// 댓글 추천
	@PostMapping("/api/post/reply/like/{id}")
	public int insertReplyLiker(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int id)
			throws Exception {
		MemberDTO mDto = principalDetails.getMemberDTO();
		ReplyLikerDTO replyLikerDto = new ReplyLikerDTO();
		replyLikerDto.setReplyId(id);
		replyLikerDto.setMemberId(mDto.getId());

		int result = 0;

		List<ReplyLikerDTO> list = postService.selectReplyLiker(replyLikerDto);

		HashMap<String, Object> map = new HashMap<>();
		map.put("replyLikerDto", replyLikerDto);

		if (list.size() >= 1) {
			replyLikerDto.setId(list.get(0).getId());
			map.put("amount", -1);
			postService.deleteReplyLiker(map);
			result = -1;
		} else {
			map.put("amount", 1);
			postService.insertReplyLiker(map);
			result = 1;
		}
		return result;
	}

	// 댓글 알림 리스트
	@GetMapping("/api/noti")
	public List<NotificationDTO> selectNoti(int memberId) throws Exception {
		return postService.selectNoti(memberId);
	}

	// 댓글 알림 삭제
	@DeleteMapping("/api/noti/{id}")
	public int notiDelete(@PathVariable int id) throws Exception {
		return postService.deleteNoti(id);
	}

	// 에러 처리
	@ExceptionHandler({ Exception.class })
	public String error() throws Exception {
		return "common/error";
	}

}
