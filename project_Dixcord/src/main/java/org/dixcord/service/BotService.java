package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.BotStatusD;
import org.dixcord.domain.BotStatusVO;
import org.dixcord.domain.BotWarnVO;
import org.dixcord.domain.BotWorkVO;

public interface BotService {
	
	public int banword(BotWorkVO botVO);
	public int bigbrother(BotWorkVO botVO);
	public int help(BotWarnVO warnVO);
	public int kick(BotWorkVO botVO);
	public int deathcount(BotWarnVO botWarnVO);
	public List<String> banWordList(int roomnumber);
	public int warning (int roomnumber);
	public int deleteBanword(BotWorkVO botVO);
	public int updateStatus(BotStatusD botstatusVO);
	public int checkStatus(int roomnumber);
	public int initStatus(int roomnumber);
	public BotStatusVO roomStatus(int roomnumber);
	public int indulgence(BotWorkVO botVO);
	public int botFriendAdd(int userCode);
	public int botFriendAdd2(int userCode);


	
	
}
