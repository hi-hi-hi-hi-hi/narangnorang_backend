package com.narangnorang.service;

import java.util.HashMap;
import java.util.List;

import com.narangnorang.dto.NotificationDTO;
import com.narangnorang.dto.PageDTO;
import com.narangnorang.dto.PostDTO;
import com.narangnorang.dto.PostLikerDTO;
import com.narangnorang.dto.ReplyDTO;
import com.narangnorang.dto.ReplyLikerDTO;

public interface PostService {
	// 각 카테고리별 게시글 목록 표시
	public List<PostDTO> selectAllByCategory(HashMap<String, Object> map) throws Exception;
	
	// 페이징에 사용하는 게시글 수
	public PageDTO<PostDTO> totalRecord(HashMap<String,Object> map) throws Exception;
	
	// 게시글 자세히 보기
	public PostDTO selectById(int id) throws Exception;
	
	// 댓글 목록 표시
	public List<ReplyDTO> selectAllReply(int id) throws Exception;
	
	// 추천수로 필터링
	public PostDTO selectByLikes(HashMap<String,Object> map) throws Exception;
	
	// 게시글 쓰기, 수정, 삭제
	public int insert(PostDTO dto) throws Exception;
	public int update(PostDTO dto) throws Exception;
	public int delete(int id) throws Exception;
	
	// 게시글 검색
	public List<PostDTO> search(HashMap<String,Object> map) throws Exception;
	
	// 페이징에 사용하는 검색 결과 수
	public PageDTO<PostDTO> searchRecord(HashMap<String, Object> map) throws Exception;
	
	// 댓글 등록
	public int insertReply(HashMap<String, Object> map) throws Exception;
	
	// 댓글 삭제
	public int deleteReply(HashMap<String, Object> map) throws Exception;
	
	// 댓글 내용 수정
	public int updateReplyContent(ReplyDTO dto) throws Exception;
	
	// 게시글 추천 기능
	public List<PostLikerDTO> selectPostLiker(PostLikerDTO dto) throws Exception;
	public int insertPostLiker(PostLikerDTO dto) throws Exception;
	public int deletePostLiker(PostLikerDTO dto) throws Exception;
	
	// 댓글 추천 기능
	public List<ReplyLikerDTO> selectReplyLiker(ReplyLikerDTO dto) throws Exception;
	public int insertReplyLiker(HashMap<String, Object> map) throws Exception;
	public int deleteReplyLiker(HashMap<String, Object> map) throws Exception;
	
	// 댓글 알림 기능
	public List<NotificationDTO> selectNoti(int memberId) throws Exception;
	public int deleteNoti(int id) throws Exception;
}