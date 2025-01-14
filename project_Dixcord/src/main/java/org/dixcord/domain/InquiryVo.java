package org.dixcord.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InquiryVo {
	private int inquiryNo, userCode;
	private String userName, category, content, inquiryState;
	private Date inquiryDate;
}
