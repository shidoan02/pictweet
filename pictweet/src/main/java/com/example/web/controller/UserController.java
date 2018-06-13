package com.example.web.controller;

/*ユーザー機能*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.business.domain.User;
import com.example.business.repository.UserRepository;
import com.example.util.UserCustom;

@Controller
public class UserController {

//	ユーザー登録画面(user/registration.html)へ遷移
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value = "/user/registration", method = RequestMethod.GET)
	public ModelAndView registration(@ModelAttribute("user") User user, ModelAndView mav) {
		//userRepository.save(user);
		mav.setViewName("/user/registration");
		return mav;
	}
	
//	@ModelAttribute(name = "login_user")
//	public UserDetails setLoginUser(@AuthenticationPrincipal UserDetails userDetails) {
//	    return userDetails;
//	}
	@ModelAttribute(name = "login_user")
	public UserDetails setLoginUser(@AuthenticationPrincipal UserCustom userCustom) {
	  return userCustom;
	}
    
	//(user/login.html)を表示する
    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public ModelAndView login(ModelAndView mav) {
    	mav.addObject("user", new User());
        mav.setViewName("/user/login");
        return mav;
    }
    
	
	/* ユーザー登録・ログイン */
	//PWが一致しなかった場合registration.htmlへリダイレクト
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	public ModelAndView registrarion(@ModelAttribute("user") User user, ModelAndView mav) {
		if (!user.getPassword().equals(user.getPasswordConfirmation())) {
			mav.setViewName("redirect:/user/registration");
			return mav;
		}
		//DBに保存
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		mav.setViewName("redirect:/user/login");
		return mav;
	}
	
	
}
