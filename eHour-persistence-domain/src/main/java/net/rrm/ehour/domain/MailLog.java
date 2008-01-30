package net.rrm.ehour.domain;

// Generated Apr 7, 2007 1:08:18 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * MailLog generated by hbm2java
 */
public class MailLog extends DomainObject<Integer, MailLog>
{

	// Fields    

	/**
	 * 
	 */
	private static final long serialVersionUID = -3984593984762448559L;

	private Integer mailLogId;

	private MailType mailType;

	private User toUser;

	private Date timestamp;

	private Boolean success;

	private String resultMsg;

	// Constructors

	/** default constructor */
	public MailLog()
	{
	}

	/** full constructor */
	public MailLog(MailType mailType, User toUser, Date timestamp, Boolean success, String resultMsg)
	{
		this.mailType = mailType;
		this.toUser = toUser;
		this.timestamp = timestamp;
		this.success = success;
		this.resultMsg = resultMsg;
	}

	// Property accessors
	public Integer getMailLogId()
	{
		return this.mailLogId;
	}

	public void setMailLogId(Integer mailLogId)
	{
		this.mailLogId = mailLogId;
	}

	public MailType getMailType()
	{
		return this.mailType;
	}

	public void setMailType(MailType mailType)
	{
		this.mailType = mailType;
	}

	public User getToUser()
	{
		return this.toUser;
	}

	public void setToUser(User toUser)
	{
		this.toUser = toUser;
	}

	public Date getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	public Boolean getSuccess()
	{
		return this.success;
	}

	public void setSuccess(Boolean success)
	{
		this.success = success;
	}

	public String getResultMsg()
	{
		return this.resultMsg;
	}

	public void setResultMsg(String resultMsg)
	{
		this.resultMsg = resultMsg;
	}

	@Override
	public Integer getPK()
	{
		return mailLogId; 
	}

	public int compareTo(MailLog o)
	{
		return timestamp.compareTo(timestamp);
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof MailLog))
		{
			return false;
		}
		MailLog rhs = (MailLog) object;
		return new EqualsBuilder().append(this.resultMsg, rhs.resultMsg).append(this.timestamp, rhs.timestamp).append(this.toUser, rhs.toUser).append(this.mailLogId, rhs.mailLogId).append(this.success, rhs.success)
				.append(this.mailType, rhs.mailType).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(1202909165, -339864927).appendSuper(super.hashCode()).append(this.resultMsg).append(this.timestamp).append(this.toUser).append(this.mailLogId).append(this.success).append(this.mailType).toHashCode();
	}

}
