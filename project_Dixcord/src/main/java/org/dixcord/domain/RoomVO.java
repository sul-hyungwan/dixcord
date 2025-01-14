package org.dixcord.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RoomVO {
	
	private int roomNumber;
	private int userCode;
	private String roomTitle;
	private String roomCategory;
	private String roomIcon;
	private String roomBgImg;
	private int roomInviteCode;
	private String welcomeMessage;
	
	

}
