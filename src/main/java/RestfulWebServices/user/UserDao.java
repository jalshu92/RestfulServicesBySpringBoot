package RestfulWebServices.user;

import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDao {

	private static List<User> users = new ArrayList<>();
	
	private static int userCount = 3;
	
	static
	{
		users.add(new User(1, "Jalaj", new Date()));
		users.add(new User(2, "Kt", new Date()));
		users.add(new User(3, "Harsh", new Date()));
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public User getUser(int id) {
		for(User u: users) {
			if(u.getId() == id)
				return u;
		}
		return null;
	}
	
	public User save(User u) {
		if(u.getId() == null) {
			u.setId(++userCount);
		}
		users.add(u);
		return u;
	}
	
	public User deleteUser(int id) {
		Iterator<User> iterator= users.iterator();
		while(iterator.hasNext()) {
			User u = iterator.next();
			if(u.getId() == id)
				return u;
		}
		return null;
	}
}
