package rkis_8.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import rkis_8.User;
import rkis_8.UserService;

/**
 * Класс контролера, отвечающий за авторизацию, регистрацию и выход из приложения
 */
@Controller
public class SecurityController {


    UserService userService;

    @Autowired
    SecurityController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("UserForm", new User());
        return "registration";
    }

    /**
     * POST-запрос на добавление пользователя в базу данных.
     * @param user Данные пользователя, полученные из формы
     * @param model Модель, в которую передается сообщение об ошибке
     * @return Редирект на главную страницу при успешной регистрации.
     * Обновляет страницу, если пользователь уже существует
     */
    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm")User user, Model model){
        user.addRole("USER");
        if (!userService.saveUser(user)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";

        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/logout")
    public String logout(){
        return "redirect:/";
    }

}

