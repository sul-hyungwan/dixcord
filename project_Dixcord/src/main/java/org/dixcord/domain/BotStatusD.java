package org.dixcord.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BotStatusD {
	
	private int roomnumber;	//방번호
	private String statusType;	//어떤 상태인지
	private int value;	// 상태의 값이 뭔지
	

}
