package org.dixcord.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomMemberVO {

	private int roomNumber;
	private int userCode;
	private String auth;
	private	String userIcon;
	
}
