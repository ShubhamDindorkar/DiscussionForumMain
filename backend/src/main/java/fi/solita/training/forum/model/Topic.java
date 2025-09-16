package fi.solita.training.forum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import org.hibernate.annotations.Formula;

@Entity
public class Topic {

	@Id
    @SequenceGenerator(name="topic_seq_gen", sequenceName="topic_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="topic_seq_gen")
	private long id;
	
	@Column(nullable=false, length=100)
	private String title;
	
	@Formula("(select count(*) from message m where m.topic_id=id)")
	private int messageCount = 0;
	
	public Topic(String title) {
		this.title = title;
	}

	Topic() { }

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getMessageCount() {
		return messageCount;
	}

}
