package vn.devpro.javaweb30.controller.backend;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.devpro.javaweb30.controller.BaseController;
import vn.devpro.javaweb30.dto.Jw30Contant;
import vn.devpro.javaweb30.model.User;
import vn.devpro.javaweb30.service.UserService;

@Controller
@RequestMapping("/admin/user/")
public class UserController extends BaseController implements Jw30Contant{

	@Autowired
	private UserService userService;
	
	@RequestMapping(value= "view", method = RequestMethod.GET)
	public String view(final Model model) {
		
		List<User> users = userService.findAllActive();
		model.addAttribute("users", users);
		
		return "backend/user/user-list";
	}
	
	@RequestMapping(value= "add", method = RequestMethod.GET)
	public String addView(final Model model) {

		User user= new User();
		model.addAttribute("user", user);
		
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
		
		return "backend/user/user-add";
	}
	
	public boolean isFileExist(MultipartFile file) {
		if(file !=null&& !StringUtils.isEmpty(file.getOriginalFilename())) {
			return true;
		}
		return false;
	}
	
	@RequestMapping(value= "add-save", method = RequestMethod.POST)
	public String addSave(final Model model,
			@ModelAttribute("user") User user,
			@RequestParam("avatarFile") MultipartFile avatarFile) throws IOException{
		
		if(isFileExist(avatarFile)) {
			//Co upload avatarFile
			//Luu file vao thu muc 
			String path= FOLDER_UPLOAD + "User/"+avatarFile.getOriginalFilename();
			File file = new File(path);
			avatarFile.transferTo(file);
			
			//Luu duong dan vao DB
			user.setAvatar("User/"+avatarFile.getOriginalFilename());
		}
		
		userService.saveOrUpdate(user);
		
		return "redirect:add";
	}
	
	@RequestMapping(value= "edit/{userID}", method = RequestMethod.GET)
	public String editView(final Model model,
			@PathVariable("userID") int userID) {
		
		//Lay du lieu trong DB
		User user= userService.getById(userID);
		model.addAttribute("user", user);
		
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
		
		return "backend/user/user-edit";
	}
	
	@RequestMapping(value= "edit-save", method = RequestMethod.POST)
	public String editSave(final Model model,
			@ModelAttribute("user") User user,@RequestParam("avatarFile") MultipartFile avatarFile) throws IOException {
		
		if(isFileExist(avatarFile)) {
			//Co upload avatarFile
			//Luu file vao thu muc 
			User dbUSer = userService.getById(user.getId());
			String path= FOLDER_UPLOAD + dbUSer.getAvatar();
			File file = new File(path);
			file.delete();
			
			user.setAvatar("User/"+avatarFile.getOriginalFilename());
			path= FOLDER_UPLOAD + "User/"+avatarFile.getOriginalFilename();
			file = new File(path);
			avatarFile.transferTo(file);
			
			//Luu duong dan vao DB
			
		}
		userService.saveOrUpdate(user);
		
		return "redirect:view";
	}
	
	@RequestMapping(value= "delete/{userID}", method = RequestMethod.GET)
	public String deleteView(final Model model,
			@PathVariable("userID") int userID) {
		
		//Lay du lieu trong DB
		User user = userService.getById(userID);
		user.setStatus(false);
		userService.saveOrUpdate(user);
		
		List<User> users = userService.findAllActive();
		model.addAttribute("users", users);
		return "backend/user/user-list";
	}
	
}
