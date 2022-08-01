package com.narangnorang.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("MessageDTO")
public class MessageDTO {

	private int id;
	private int senderId;
	private String senderName;
	private int senderPrivilege;
	private int recieverId;
	private String recieverName;
	private int recieverPrivilege;
	private String content;
	private String datetime;
	private int read;

}
