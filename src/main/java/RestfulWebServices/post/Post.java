package RestfulWebServices.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import RestfulWebServices.user.User;

@Entity
public class Post {
	@Id @GeneratedValue
	private Integer postId;
	private String msg;
	@ElementCollection
	private List<String> comments = new ArrayList<>();
	private Date postDate;	
	@ManyToOne @JsonIgnore
	private User user;
	
	public Post() {
		
	}

	public Post(String msg, Date postDate) {
		super();
		this.msg = msg;
		this.postDate = postDate;
	}
	public Integer getPostId() {
		return postId;
	}
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<String> getComments() {
		return comments;
	}
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
