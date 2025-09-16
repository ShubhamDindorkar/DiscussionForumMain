package fi.solita.training.forum.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Message {

	@Id
    @SequenceGenerator(name="message_seq_gen", sequenceName="message_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="message_seq_gen")
	private long id;

	@Column(nullable=false)
	private Date createTime;

	@Column(nullable=false, length=1000)
	private String content;

	@ManyToOne(optional=false)
	private Topic topic;

	public Message(String content, Topic topic) {
		this.createTime = new Date();
		this.content = content;
		this.topic = topic;
	}

	Message() { }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

}
