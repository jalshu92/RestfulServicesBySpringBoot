package RestfulWebServices.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import RestfulWebServices.user.User;
import RestfulWebServices.user.UserDao;
import RestfulWebServices.user.UserNotFoundException;

@RestController
public class PostResource {

	@Autowired
	private UserDao userDao;
	
	/*@GetMapping("/users/{id}/posts")
	public List<Post> getAllPosts(@PathVariable Integer id) {
		return userDao.getUser(id).getPosts();
	}
	
	@GetMapping("/users/{id}/posts/{postId}")
	public Post getPost(@PathVariable Integer id, @PathVariable Integer postId) {
		User u = userDao.getUser(id);
		if(u==null)
			throw new UserNotFoundException("User not found with id="+id);
		Post p = u.getPosts().get(postId);
		if(p==null) {
			throw new PostNotFoundException("Post not found with id="+postId);
		}
		return p;
	}*/
}
