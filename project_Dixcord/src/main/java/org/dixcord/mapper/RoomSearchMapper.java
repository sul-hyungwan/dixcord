package org.dixcord.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.dixcord.domain.RoomVO;
	
public interface RoomSearchMapper {
	public List<RoomVO> searchRoomInviteCode(@Param("inviteCode") String inviteCode);
	public List<RoomVO> searchRoomTitles(@Param("query") String query);
}
