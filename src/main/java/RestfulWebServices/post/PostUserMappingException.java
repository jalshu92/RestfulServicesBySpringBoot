package RestfulWebServices.post;

public class PostUserMappingException extends RuntimeException {

	public PostUserMappingException() {
		super("Post doesnt belong to the user");
	}
}
