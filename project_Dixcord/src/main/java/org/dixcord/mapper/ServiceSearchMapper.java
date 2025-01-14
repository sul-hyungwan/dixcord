package org.dixcord.mapper;

import java.util.List;

import org.dixcord.domain.NoticeVO;

public interface ServiceSearchMapper {
	public List<NoticeVO> search(String keyword);
}