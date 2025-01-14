package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.RoomVO;
import org.dixcord.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService{
	
	
	@Autowired 
	private RoomMapper rmapper;
	
	@Override
	public RoomVO chatRoomInfo(int roomNumber) {
		return rmapper.getRoomInfo(roomNumber);
	}
	
	@Override
	public List<RoomVO> getChatList(int userCode){
		return rmapper.getChatList(userCode);
	}
	
	@Override
	public String getUserName(int userCode) {
		return rmapper.getUserName(userCode);
	}
	
	@Override
	public int createRoom(RoomVO rvo) {
		return rmapper.createRoom(rvo);
	}
}



