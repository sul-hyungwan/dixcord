package org.dixcord.service;

import java.util.List;

import javax.naming.directory.SearchResult;

import org.dixcord.domain.NoticeVO;

public interface ServiceSearchService {
	public List<NoticeVO> search(String keyword);
}
