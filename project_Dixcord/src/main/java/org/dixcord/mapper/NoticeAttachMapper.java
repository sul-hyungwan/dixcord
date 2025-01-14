package org.dixcord.mapper;

import java.util.List;

import org.dixcord.domain.NoticeAttachVO;

public interface NoticeAttachMapper {
	public int insertImg(NoticeAttachVO navo);
	public int deleteNotice(int idx);
	public List<NoticeAttachVO> findByIdx(int idx);
	public String noticeImage(int idx);
}
