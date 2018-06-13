package com.example.util;

/* Spring SecurityのUser + idの情報 を持ったUserCustomクラスの定義 */
	import java.util.Set;
	import org.springframework.security.core.GrantedAuthority;
	import org.springframework.security.core.userdetails.User;

	public class UserCustom extends User {
	    private static final long serialVersionUID = 1L;
	    private Long id;

	    public UserCustom(Long id, String username, String password, Set<? extends GrantedAuthority> autorities) {
	        super(username, password, autorities);
	        this.id = id;
	    }

	    public Long getId() {
	        return this.id;
	    }
}
	/*  UserDetailsServiceImpl.java
		UserController.java
		TweetController.java         */
