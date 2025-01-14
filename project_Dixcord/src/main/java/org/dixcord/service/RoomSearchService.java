package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.RoomVO;

public interface RoomSearchService {
	public List<RoomVO> searchRoomTitles(String query);
}
