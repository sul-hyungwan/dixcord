package org.dixcord.domain;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeVO {
	private int idx, usercode;
	private String writer, title, content, userIcon;
	private Date regDate;
	private Date updateDate;
	
	private List<NoticeAttachVO> attachList;
}
