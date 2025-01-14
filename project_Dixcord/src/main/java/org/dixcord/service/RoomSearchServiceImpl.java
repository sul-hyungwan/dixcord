package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.RoomVO;
import org.dixcord.mapper.RoomSearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class RoomSearchServiceImpl implements RoomSearchService {
	@Autowired
    private RoomSearchMapper mapper;

	@Override
	public List<RoomVO> searchRoomTitles(String query) {
	    if (query.startsWith("#")) {
	        // '#' 제거 후 초대 코드로 검색
	        String inviteCode = query.substring(1);
	        return mapper.searchRoomInviteCode(inviteCode);
	    } else {
	        // 제목으로 검색
	        return mapper.searchRoomTitles(query);
	    }
	}

}
