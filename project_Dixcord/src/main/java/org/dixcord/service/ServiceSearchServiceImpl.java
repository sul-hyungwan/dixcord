package org.dixcord.service;

import java.util.List;

import javax.naming.directory.SearchResult;

import org.dixcord.domain.NoticeVO;
import org.dixcord.mapper.ServiceSearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class ServiceSearchServiceImpl implements ServiceSearchService{
	@Autowired
    private ServiceSearchMapper mapper;

	@Override
    public List<NoticeVO> search(String keyword) {
        return mapper.search(keyword);
    }
	
}
