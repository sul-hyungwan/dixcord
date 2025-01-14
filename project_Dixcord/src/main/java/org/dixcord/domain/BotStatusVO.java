package org.dixcord.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BotStatusVO {
	
	private int roomnumber;	//방번호
	private int BWstatus;	
	private int WKstatus;	
	private int MPstatus;		

}
