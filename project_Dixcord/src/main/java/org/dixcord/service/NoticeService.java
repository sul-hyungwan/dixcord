package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.NoticeAttachVO;
import org.dixcord.domain.NoticeVO;

public interface NoticeService {
   public List<NoticeVO> noticeList();
   public NoticeVO read(int idx);
   public int modify(NoticeVO nvo);
   public int delete(int idx);
   public int insert(NoticeVO nvo, NoticeAttachVO navo);
//   public int insertImg(NoticeAttachVO navo);
   // 첨부 파일 리스트
   public List<NoticeAttachVO> getAttachList(int idx);
   
   public String readTitle(int idx);
   
   public String noticeImage(int idx);
}
