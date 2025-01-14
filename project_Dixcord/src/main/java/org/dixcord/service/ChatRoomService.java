package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



public interface ChatRoomService{

	
	public RoomVO chatRoomInfo(int roomNumber); 
	
	public List<RoomVO> getChatList(int userCode);
	
	public String getUserName(int userCode);
	
	public int createRoom(RoomVO rvo);

}
