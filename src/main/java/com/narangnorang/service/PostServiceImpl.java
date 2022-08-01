package com.narangnorang.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narangnorang.dao.PostDAO;
import com.narangnorang.dto.NotificationDTO;
import com.narangnorang.dto.PageDTO;
import com.narangnorang.dto.PostDTO;
import com.narangnorang.dto.PostLikerDTO;
import com.narangnorang.dto.ReplyDTO;
import com.narangnorang.dto.ReplyLikerDTO;

@Service("postService")
public class PostServiceImpl implements PostService {

	@Autowired
	private PostDAO dao;

	@Override
	public List<PostDTO> selectAllByCategory(HashMap<String, Object> map) throws Exception {
		return dao.selectAllByCategory(map);
	}

	@Override
	public PageDTO<PostDTO> totalRecord(HashMap<String, Object> map) throws Exception {
		return dao.totalRecord(map);
	}

	@Transactional
	@Override
	public PostDTO selectById(int id) throws Exception {
		dao.updateViews(id);
		return dao.selectById(id);
	}

	@Override
	public List<ReplyDTO> selectAllReply(int id) throws Exception {
		return dao.selectAllReply(id);
	}

	@Override
	public PostDTO selectByLikes(HashMap<String, Object> map) throws Exception {
		return dao.selectByLikes(map);
	}

	@Override
	public int insert(PostDTO dto) throws Exception {
		return dao.insert(dto);
	}

	@Override
	public int update(PostDTO dto) throws Exception {
		return dao.update(dto);
	}

	@Override
	public int delete(int id) throws Exception {
		return dao.delete(id);
	}

	@Override
	public List<PostDTO> search(HashMap<String, Object> map) throws Exception {
		return dao.search(map);
	}

	@Override
	public PageDTO<PostDTO> searchRecord(HashMap<String, Object> map) throws Exception {
		return dao.searchRecord(map);
	}

	@Transactional
	@Override
	public int insertReply(HashMap<String, Object> map) throws Exception {
		dao.updateReplies(map);
		dao.insertNoti((PostDTO) map.get("postDto"));
		return dao.insertReply((ReplyDTO) map.get("replyDto"));
	}

	@Transactional
	@Override
	public int deleteReply(HashMap<String, Object> map) throws Exception {
		dao.updateReplies(map);
		return dao.deleteReply((int) map.get("replyId"));
	}

	@Override
	public int updateReplyContent(ReplyDTO dto) throws Exception {
		return dao.updateReplyContent(dto);
	}

	@Transactional
	@Override
	public int insertPostLiker(PostLikerDTO dto) throws Exception {
		dao.plusPostLike(dto.getPostId());
		return dao.insertPostLiker(dto);
	}

	@Override
	public List<PostLikerDTO> selectPostLiker(PostLikerDTO dto) throws Exception {
		return dao.selectPostLiker(dto);
	}

	@Transactional
	@Override
	public int deletePostLiker(PostLikerDTO dto) throws Exception {
		dao.minusPostLike(dto.getPostId());
		return dao.deletePostLiker(dto.getId());
	}

	@Override
	public List<ReplyLikerDTO> selectReplyLiker(ReplyLikerDTO dto) throws Exception {
		return dao.selectReplyLiker(dto);
	}

	@Override
	public int insertReplyLiker(HashMap<String, Object> map) throws Exception {
		dao.updateReplyLike(map);
		return dao.insertReplyLiker((ReplyLikerDTO) map.get("replyLikerDto"));
	}

	@Override
	public int deleteReplyLiker(HashMap<String, Object> map) throws Exception {
		dao.updateReplyLike(map);
		return dao.deleteReplyLiker((ReplyLikerDTO) map.get("replyLikerDto"));
	}

	@Override
	public List<NotificationDTO> selectNoti(int memberId) throws Exception {
		return dao.selectNoti(memberId);
	}

	@Override
	public int deleteNoti(int id) throws Exception {
		return dao.deleteNoti(id);
	}

}
