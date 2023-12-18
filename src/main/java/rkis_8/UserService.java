package rkis_8;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rkis_8.DAO.UserDAO;

/**
 * Класс, связывающий базу данных пользователей(UserDAO) и обработку пользователей SpringSecurity
 */
@Service
public class UserService implements UserDetailsService {

    UserDAO userDAO;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService(UserDAO userDAO, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userDAO = userDAO;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    /**
     * Извлечение данных пользователя по его имени
     * @param username уникальное имя пользователя
     * @return экземпляр класса User
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userDAO.loadUserByUsername(username);
            return user;
        }
        catch (EmptyResultDataAccessException e){
            throw new UsernameNotFoundException("Пользователь не найден");
        }

    }

    /**
     * Сохранение данных пользователя в БД
     * @param user экземпляр класса User с данными пользователя
     * @return true, если пользователь был сохранен, иначе false
     */
    public boolean saveUser(User user){
        // Если при попытке загрузить пользователя исключение не было получено,
        // значит он есть в БД, и новое значение не будет сохранено
        try {
            User userFromDB = userDAO.loadUserByUsername(user.getUsername());
            return false;
        }
        catch (UsernameNotFoundException e){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDAO.addUser(user);
            return true;
        }


    }

}
