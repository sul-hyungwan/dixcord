package org.dixcord.mapper;

import java.util.List;

import org.dixcord.domain.NoticeVO;

public interface NoticeMapper {
   public List<NoticeVO> noticeList();
   public NoticeVO read(int idx);
   public int modify(NoticeVO nvo);
   public int delete(int idx);
   public int insert(NoticeVO nvo);
   
   public String readTitle(int idx);
}
