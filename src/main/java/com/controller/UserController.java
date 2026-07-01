package com.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model.User;
import com.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userServ;
	
	@PostMapping("/createuser")
	public String createUser(@ModelAttribute User user, HttpSession session)
	{
		String email = user.getEmail();
		
		if(userServ.getUserByEmail(email) != null)
		{
			session.setAttribute("fail", "Registration Failed, Please try different Email Id");
			
			return "redirect:/register";
		}
		else{
			 
			userServ.addUser(user);
			session.setAttribute("msg", "Registration successful");
			return "redirect:/register";
		}
		
	}
	
	@GetMapping("/user")
	public String dashboard(Model m, Principal p)
	{
		String email = p.getName(); // 
		
		User user  = userServ.getUserByEmail(email);
		
		m.addAttribute("user",user);
		m.addAttribute("title","DASHBOARD");
		
		return "user/dashboard";
		
		
	}
	
	// ---- Forgot Password ----
	
	@GetMapping("/forgot-password")
	public String forgotPasswordPage(Model m)
	{
		m.addAttribute("title", "Forgot Password");
		return "forgot-password";
	}
	
	@PostMapping("/verify-user")
	public String verifyUser(@RequestParam("email") String email,
	                          @RequestParam("phone") String phone,
	                          HttpSession session)
	{
		User user = userServ.getUserByEmailAndPhone(email, phone);
		
		if (user == null) {
			session.setAttribute("fpFail", "No account found with this Email and Phone combination");
			return "redirect:/forgot-password";
		}
		
		session.setAttribute("verifiedEmail", email);
		session.setAttribute("verifiedPhone", phone);
		return "redirect:/forgot-password?verified=true";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam("newPassword") String newPassword,
	                             @RequestParam("confirmPassword") String confirmPassword,
	                             HttpSession session)
	{
		String email = (String) session.getAttribute("verifiedEmail");
		String phone = (String) session.getAttribute("verifiedPhone");
		
		if (email == null || phone == null) {
			session.setAttribute("fpFail", "Session expired, please verify again");
			return "redirect:/forgot-password";
		}
		
		if (!newPassword.equals(confirmPassword)) {
			session.setAttribute("fpFail", "Passwords do not match");
			return "redirect:/forgot-password?verified=true";
		}
		
		boolean success = userServ.resetPassword(email, phone, newPassword);
		
		session.removeAttribute("verifiedEmail");
		session.removeAttribute("verifiedPhone");
		
		if (success) {
			session.setAttribute("vmsg", "Password reset successful. Please login.");
			return "redirect:/signin";
		} else {
			session.setAttribute("fpFail", "Something went wrong, please try again");
			return "redirect:/forgot-password";
		}
	}
		

}