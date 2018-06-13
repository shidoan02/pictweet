//package com.example.web.controller;

package com.example.web.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.business.domain.Tweet;
import com.example.business.domain.User;
import com.example.business.repository.TweetRepository;
import com.example.business.repository.UserRepository;
import com.example.util.UserCustom;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@Controller: コントローラとしての役割を持つクラスだとSpring Bootに識別させる
@Controller
public class TweetController {

	@Autowired
	private TweetRepository tweetRepository;

	// @RequestMappingの役割:引数に応じたメソッドを呼び出す
	// HTTPメソッドのGET で localhost:8080/ というURLにアクセスされると、hogeメソッドが作動
	// その後、メソッドの内容が処理され、「src/main/resources」以下のtemplatesファイルの中に置かれた
	// index.html という名前のファイルがビューとして表示
	@RequestMapping(value = "/", method = RequestMethod.GET)
	//// addObjectメソッドはModelAndViewオブジェクトに対し使えるメソッド
	//@AuthenticationPrincipal UserDetails userDetails :引数内に追加することで、そのメソッド内でログイン中のユーザーの情報を取得できます。それ以外null
	public ModelAndView index(@PageableDefault(size = 5) Pageable pageable, ModelAndView mav, @AuthenticationPrincipal UserDetails userDetails) { // (ModelAndView mav) {
		Page<Tweet> tweets = tweetRepository.findAllByOrderByIdDesc(pageable);

		// List<Tweet> tweets = tweetRepository.findAllByOrderByIdDesc();
		// List<Tweet> tweets = tweetRepository.findAll();
		mav.addObject("tweets", tweets);
		mav.addObject("login_user", userDetails);//ユーザー認証・未認証切り替え

		String hello = "Hello, Spring Boot!";
		// Tweet tweet = tweetRepository.findOne(1L);
		// addObjectメソッド（第一引数に文字列で識別子名，第二引数にビューに渡したいデータ(変数)）
		// mav.addObject("hello", hello); //add ① String型変数helloに特定の文字列を指定し初期化。
		// mav.addObject("tweet", tweet);//add ② ビューにデータを渡すため、mav.addObject("hello",
		// hello); を追記。
		mav.setViewName("tweet/index"); // パス指定 viewに飛ぶ
		return mav;
	}

	/*  */
	@ModelAttribute(name = "login_user")
	public UserDetails setLoginUser(@AuthenticationPrincipal UserDetails userDetails) {
	    return userDetails;
	}
	
	/* ツイートの新規投稿画面表示 */
	@RequestMapping(value = "/tweet/new", method = RequestMethod.GET)
	public ModelAndView newTweet(ModelAndView mav) {
		mav.setViewName("tweet/new");
		return mav;
	}
	
	/* ツイートの編集画面表示 */ 
	//@PathVariable {引き数}をmethodに送る
	@RequestMapping(value = "/tweet/{id}/edit", method = RequestMethod.GET)
	public ModelAndView editTweet(@PathVariable("id") Long id, ModelAndView mav) {
	    Tweet tweet = tweetRepository.findOne(id);
	    mav.addObject("tweet", tweet);
	    mav.setViewName("tweet/edit");
	    return mav;
	}
	
	/* ツイートの編集をDBへ送る */ 
	@RequestMapping(value = "/tweet/{id}/edit", method = RequestMethod.POST)
	public ModelAndView updateTweet(@ModelAttribute Tweet editTweet, @PathVariable("id") Long id, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav) {        
	    Tweet tweet = tweetRepository.findOne(id); //DBからid取得
	    //ユーザー本人であるかの確認
	    if (!tweet.getUser().getId().equals(userCustom.getId())) {
	        mav.setViewName("redirect:/tweet/" + id + "/edit");
	        return mav;
	    }
	    BeanUtils.copyProperties(editTweet, tweet); //フォームから送られてきたツイートの情報を、データベースから取得したツイートにコピー 
	    tweetRepository.save(tweet);
	    mav.setViewName("tweet/update");
	    return mav;
	}

	/* ツイートをDBへ送る */
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value = "/tweet/new", method = RequestMethod.POST)
	public ModelAndView createTweet(@ModelAttribute Tweet tweet, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav) {
	    User user = userRepository.findOne(userCustom.getId());
	    tweet.setUser(user);
		tweetRepository.saveAndFlush(tweet);
		mav.setViewName("tweet/create");
		return mav;
	}
	
	/* ツイート詳細画面 アクション */
    @RequestMapping(value = "/tweet/{id}", method = RequestMethod.GET)
    ModelAndView show(@PathVariable Long id, ModelAndView mav) {
        Tweet tweet = tweetRepository.findOne(id);
        mav.addObject("tweet", tweet);
        mav.setViewName("tweet/show");
        return mav;
    }
	
	/* 削除画面を表示 */
	@RequestMapping(value = "/tweet/{id}/delete", method = RequestMethod.POST)
	public ModelAndView deleteTweet(@PathVariable("id") Long id, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav) {
	    Tweet tweet = tweetRepository.findOne(id);
	  //ユーザー本人であるかの確認
	    if (!tweet.getUser().getId().equals(userCustom.getId())) {
	        mav.setViewName("redirect:/");
	        return mav;
	    }
	    tweetRepository.delete(tweet);
	    mav.setViewName("redirect:/");
	    return mav;
	}
	
	/*  */
	

}
