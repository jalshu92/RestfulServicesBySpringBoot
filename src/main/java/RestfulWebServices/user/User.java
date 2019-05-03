package RestfulWebServices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import RestfulWebServices.post.Post;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//@JsonFilter("UserFilter")
@ApiModel(description="Details about user")
@Entity
public class User {

	@Id @GeneratedValue
	private Integer id;
	
	@Size(min=2,message="name should be min 2 chars")
	@ApiModelProperty(notes="Should contain min 2 chars")
	private String name;
	
	@Past(message="birthdate cannotbe future date")
	@ApiModelProperty(notes="cannot be a future date")
	private Date date;
	
	@OneToMany(mappedBy="user")
	private List<Post> posts = new ArrayList<>();
	
	public User(int id, String name, Date date) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
		//this.posts.add(new Post(name, new Date()));
	}
	
	protected User() {
			
	}

	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
}
