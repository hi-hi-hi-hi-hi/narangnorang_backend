package com.narangnorang.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("NotificationDTO")
public class NotificationDTO {

	private int id;
	private int memberId;
	private int postId;
	// 게시글 정보
	private String postTitle;
	private String postCategory;
}
