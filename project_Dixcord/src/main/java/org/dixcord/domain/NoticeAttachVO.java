package org.dixcord.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeAttachVO {
    private String uuid;
    private String uploadPath;
    private String uploadName;
    private int idx;
}
