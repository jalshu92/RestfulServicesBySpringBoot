package RestfulWebServices.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import RestfulWebServices.post.Post;
import RestfulWebServices.post.PostRepository;
import RestfulWebServices.post.PostUserMappingException;

@RestController
public class UserResource {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	private FilterProvider getFilterProviderForUser(String... fieldsToDisplay) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fieldsToDisplay);
		return new SimpleFilterProvider().addFilter("UserFilter", filter);
	}
	
	@GetMapping("/users")
	public MappingJacksonValue getAllUsers() {		
		MappingJacksonValue value = new MappingJacksonValue(userDao.getUsers());
		value.setFilters(getFilterProviderForUser("name","id"));
		return value;
	}
	
	@GetMapping(value= "/users/{id}",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public MappingJacksonValue getUser(@PathVariable int id) {
		User u = userDao.getUser(id);
		if(u == null) {
			throw new UserNotFoundException("User not found with id="+id);
		}		
		Resource<User> user = new Resource<User>(u);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllUsers());
		user.add(linkTo.withRel("users"));
		MappingJacksonValue value = new MappingJacksonValue(user);
		value.setFilters(getFilterProviderForUser("name","posts"));
		return value;
	}
	
	@GetMapping("/jpa/users")
	public List<User> getJpaUsers() {
		
		return userRepository.findAll();
	}
	
	@GetMapping("/jpa/users/{id}")
	public Resource<User> getJpaUser(@PathVariable Integer id) {
		Optional<User> u = userRepository.findById(id);
		if(!u.isPresent())
			throw new UserNotFoundException("User not found with id:"+id);
		Resource<User> user = new Resource<User>(u.get());
		user.add(linkTo(methodOn(this.getClass()).getJpaUsers()).withRel("JPAUsers"));
		return user;
	}
	
	@PostMapping("/users")
	public ResponseEntity<Object> addUser(@Valid @RequestBody User u) {
		User savedUser = userDao.save(u);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/jpa/users")
	public ResponseEntity<Object> createJPAUser(@Valid @RequestBody User u) {
		User newUser = userRepository.save(u);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(newUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		User u = userDao.deleteUser(id);
		if(u==null)
			throw new UserNotFoundException("User not found with id="+id);		
	}
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteJPAUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}
	
	@DeleteMapping("/jpa/users/{id}/posts/{postId}")
	public ResponseEntity<Object> deletePost(@PathVariable Integer id, @PathVariable Integer postId) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User NF id:"+id);
		Optional<Post> p = postRepository.findById(postId);
		if(!p.isPresent())
			throw new UserNotFoundException("Post NF id:"+id);
		Post post = p.get();
		if(user.get().getPosts().indexOf(post) < 0)
			throw new PostUserMappingException();
		postRepository.deleteById(postId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> getPosts(@PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User NF id:"+id);
		return user.get().getPosts();
	}
	
	@GetMapping("/jpa/users/{id}/posts/{postId}")
	public Resource<Post> getPost(@PathVariable Integer id, @PathVariable Integer postId) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User NF id:"+id);
		
		Optional<Post> p = postRepository.findById(postId);
		if(!p.isPresent())
			throw new UserNotFoundException("Post NF id:"+id);
		Post post = p.get();
		if(user.get().getPosts().indexOf(post) < 0)
			throw new PostUserMappingException();
		Resource<Post> rp = new Resource<>(post);
		Link l = linkTo(methodOn(this.getClass()).getPosts(id)).withRel("Posts");
		rp.add(l);
		return rp;
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable Integer id,@RequestBody Post post) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User NF id:"+id);
		post.setUser(user.get());
		Post newPost = postRepository.save(post);
		URI loc = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{postid}").buildAndExpand(newPost.getPostId()).toUri();
		return ResponseEntity.created(loc).build();
	}
	
	@PutMapping("/jpa/users/{id}/posts/{postId}")
	public Resource<Post> updatePost(@PathVariable Integer id, @PathVariable Integer postId,@RequestBody Post p1) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User NF id:"+id);
		
		Optional<Post> p = postRepository.findById(postId);
		if(!p.isPresent())
			throw new UserNotFoundException("Post NF id:"+id);
		Post post = p.get();
		if(user.get().getPosts().indexOf(post) < 0)
			throw new PostUserMappingException();
		post.setMsg(p1.getMsg());
		post.setPostDate(p1.getPostDate());
		post.setComments(p1.getComments());
		postRepository.save(post);
		Resource<Post> rp = new Resource<Post>(p1);
		rp.add(linkTo(methodOn(this.getClass()).getPost(id, postId)).withSelfRel());
		rp.add(linkTo(methodOn(this.getClass()).getPosts(id)).withRel("Posts"));
		return rp;
	}
	
	@GetMapping("/gm")
	public String goodMorning(@RequestHeader(name="Accept-Language",required=false) Locale locale ) {
		return messageSource.getMessage("good.morning.message", null, locale);
	}
}
