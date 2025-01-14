package org.dixcord.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BotWarnVO {
	
	private int roomnumber; //방번호
	private int userCode; // 유저 고유 코드
	
	
}
