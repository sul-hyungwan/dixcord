package org.dixcord.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminInquiryVO {
	private int inquiryNo;
	private int userCode;
	private String userName;
	private String message;
	private Date inqdate;
}
