package rkis_8.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import rkis_8.Perfumery;


import javax.sql.DataSource;
import java.util.List;

/**
 * Соединение приложения с базой данных
 */
@Component
public class PerfumeryDAO {

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
     * SQL-запрос для вывода всех значений из базы данных perfumery
     * @return экземпляр List со всеми значениями базы данных perfumery
     */
    public List<Perfumery> findAll(){
        return jdbcTemplate.query("SELECT * FROM perfumery",
                new BeanPropertyRowMapper(Perfumery.class));
    }

    public Perfumery findById(int id){
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM perfumery WHERE id=?",
                    BeanPropertyRowMapper.newInstance(Perfumery.class), id);
        }
        catch (IncorrectResultSizeDataAccessException e){
            return null;
        }
    }

    /**
     * Добавление строки в базу данных
     * @param perfumery Экземпляр Perfumery, добавляемый в БД
     * @return Количество добавленных строк(1)
     */
    public int insert(Perfumery perfumery){
        if (perfumery.getId() != 0){
            return jdbcTemplate.update("INSERT INTO perfumery(id, type, color, aroma, volume, concentration)" +
                            "VALUES(?, ?, ?, ?, ?, ?)",
                    perfumery.getId(), perfumery.getType(), perfumery.getColor(), perfumery.getAroma(),
                    perfumery.getVolume(), perfumery.getConcentration());
        }
        else{
            return jdbcTemplate.update("INSERT INTO perfumery(type, color, aroma, volume, concentration)" +
                            "VALUES(?, ?, ?, ?, ?)",
                    perfumery.getType(), perfumery.getColor(), perfumery.getAroma(),
                    perfumery.getVolume(), perfumery.getConcentration());
        }

    }



    /**
     * Редактирование записи с заданным id
     * @param id Идентификатор редактируемой строки
     * @param perfumery Экземпляр Perfumery, значения из которого заменят значения старой строки
     * @return Количество измененных строк
     */
    public int update(int id, Perfumery perfumery){
        return jdbcTemplate.update("UPDATE perfumery SET type=?, color=?, aroma=?, volume=?," +
                        " concentration=? WHERE id = ?", perfumery.getType(), perfumery.getColor(),
                perfumery.getAroma(), perfumery.getVolume(), perfumery.getConcentration(), id);
    }

    /**
     * Удаление строки с заданым id
     * @param id Id удаляемой строки
     * @return Количество удаленных строк(1 или 0)
     */
    public int delete(int id){
        return jdbcTemplate.update("DELETE FROM perfumery WHERE id = ?", id);
    }

    /**
     * Фильтрация данных по значению столбца volume
     * @param minVolume минимальное значение volume, которое вернет метод
     * @return Отфильтрованный список List значений volume
     */
    public List<Perfumery> filterVolume(int minVolume){
        return jdbcTemplate.query("SELECT * FROM perfumery WHERE volume >= ?", new Object[]{minVolume},
                new BeanPropertyRowMapper(Perfumery.class));
    }
}


