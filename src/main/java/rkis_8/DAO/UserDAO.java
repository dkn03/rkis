package rkis_8.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import rkis_8.User;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс DAO, осуществляющий доступ к базе данных пользователей.
 */
@Component
public class UserDAO {

    JdbcTemplate jdbcTemplate;
    /**
     * Подключение к базе данных
     * @param dataSource Интерфейс базы данных
     */
    @Autowired
    public void setDataSource(DataSource dataSource){

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Получает пользователя из базы данных по его username. Для извлечения столбца-массива из БД использует
     * специально написанный Mapper(я не нашел нужную реализацию в spring)
     * @param username - Уникальное имя пользователя
     * @return Полученный из базы данных пользователь User
     */
    public User loadUserByUsername(String username){
        User user;
        user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE username=?", new UserDAOMapper(),
                new Object[]{username});
        return user;
    }

    /**
     * Добавление пользователя в БД
     * @param user экземпляр класса пользователя
     * @return количество добавленных строк(1 или 0)
     */
    public int addUser(User user){
        return jdbcTemplate.update("INSERT INTO users(username, password, roles) VALUES (?, ?, ?)",
                user.getUsername(), user.getPassword(), createSqlArray(user.getRoles()));
    }

    /**
     * Mapper для извлечения пользователя из базы данных. Необходим для извлечения массива из базы данных
     */
    private static class UserDAOMapper implements RowMapper<User>{

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            String username = rs.getString("username");
            String password = rs.getString("password");
            Array rolesArray = rs.getArray("roles");
            rolesArray.getArray();
            ArrayList<String> roles = new ArrayList<>(Arrays.asList((String[]) rolesArray.getArray()));
            return new User(username, password, roles);
        }
    }

    /**
     * Преобразование списка строк java в массив sql
     * @param list - Конвертируемый список строк
     * @return массив строк для sql.
     */
    private java.sql.Array createSqlArray(List<String> list){
        java.sql.Array stringArray = null;
        try {
            stringArray = jdbcTemplate.getDataSource().getConnection().createArrayOf("varchar", list.toArray());
        } catch (SQLException ignore) {
        }
        return stringArray;
    }
}
