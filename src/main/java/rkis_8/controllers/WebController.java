package rkis_8.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rkis_8.Perfumery;
import rkis_8.DAO.PerfumeryDAO;
import rkis_8.jms.MessageSender;

import java.io.IOException;
import java.util.List;

@Controller
public class WebController {

    private String messagesQueue;

    PerfumeryDAO perfumeryDAO;
    MessageSender messageSender;


    public WebController(PerfumeryDAO perfumeryDAO, MessageSender messageSender, @Value("${queue.name}") String messagesQueue){
        this.messageSender = messageSender;
        this.perfumeryDAO = perfumeryDAO;
        this.messagesQueue = messagesQueue;

    }

    /**Домашняя страница сайта*/
    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("authentication", SecurityContextHolder.getContext().getAuthentication());
        return "home_page";
    }

    @ResponseBody
    @GetMapping("/perfumery")
    public List<Perfumery> getAllPerfumery(@RequestParam(value="minVolume", required=false) Integer minVolume){
        if (minVolume == null) {
            return perfumeryDAO.findAll();
        } else {
            return perfumeryDAO.filterVolume(minVolume);
        }
    }

    /**
     * HTML-страница, отображающая значения из базы данных. Выводит либо все значения из БД, либо отфильтрованные
     * согласно столбцу Volume
     * @param minVolume Необязательный параметр. Если передан, то выводит только те записи,
     *                  в которых значение Volume больше заданного
     * @param model Модель, в которую записываются значения из базы данных
     * @return имя html-страницы
     */
    @GetMapping(value = "/perfumery", headers = {"Accept=text/html"})
    public String getAllPerfumery(@RequestParam(value="minVolume", required=false) Integer minVolume, Model model) {
        model.addAttribute("perfumery", getAllPerfumery(minVolume));
        return "perfumery_list";
    }



    @ResponseBody
    @GetMapping(value="/perfumery/{id}")
    public Perfumery getPerfumery(@PathVariable("id") int id){
        return perfumeryDAO.findById(id);
    }


    @GetMapping(value = "/perfumery/{id}", headers = {"Accept=text/html"})
    public String getPerfumery(@PathVariable int id, Model model){
        model.addAttribute("perfumery", getPerfumery(id));
        return "show_perfumery";
    }

    @GetMapping("/perfumery/new")
    public String createPerfumery(Model model){
        model.addAttribute("perfumery", new Perfumery());
        return "new_perfumery";
    }

    @PostMapping(value = "/perfumery", consumes = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String createPerfumery(HttpServletRequest request) throws IOException {
        Perfumery perfumery = extractPerfumery(request);
        try {
            perfumeryDAO.insert(perfumery);
            this.messageSender.sendMessage(this.messagesQueue, "Добавлен объект" + perfumery);
            //this.jmsTemplate.convertAndSend("messageList", "Добавлен объект" + perfumery);


        }
        catch (Exception e){
            System.out.println(e.getMessage());

        }
        return "redirect:/perfumery";
    }

    @GetMapping("/perfumery/update")
    public String updatePerfumeryForm(Model model){
        model.addAttribute("perfumery", new Perfumery());
        return "edit_perfumery";
    }

    @PutMapping (value = "/perfumery/{id}", consumes={
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String updatePerfumery(@PathVariable("id") int id, HttpServletRequest request) throws IOException {
        Perfumery perfumery = extractPerfumery(request);

        if(perfumeryDAO.update(id, perfumery) != 0){
            messageSender.sendMessage(this.messagesQueue, "Изменен объект с id " + id);
        }
        else{
            messageSender.sendMessage(this.messagesQueue,
                    String.format("Попытка изменить объект с id %d. Объект с таким id не существует", id));
        }
        return "redirect:/perfumery";
    }


    /**
     * Извлечение экземпляра Perfumery в зависимости от типа содержимого запроса
     * @param request Запрос к серверу
     * @return прочитанный экземпляр Perfumery
     * @throws IOException
     */
    public Perfumery extractPerfumery(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        Perfumery perfumery;
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)){
            ObjectMapper mapper = new ObjectMapper();
            perfumery = mapper.readValue(request.getInputStream(), Perfumery.class);
        }
        //Если значения вводятся из формы
        else if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(contentType)){
            String type = request.getParameter("type");
            String color = request.getParameter("color");
            String aroma = request.getParameter("aroma");
            int volume = Integer.parseInt(request.getParameter("volume"));
            double concentration = Double.parseDouble(request.getParameter("concentration"));
            perfumery = new Perfumery(type, color, aroma, volume, concentration);
        }
        else{
            perfumery = null;
        }
        return perfumery;
    }

    @GetMapping("/perfumery/delete")
    public String deletePerfumery(){
        return "delete_perfumery";
    }

    @DeleteMapping(value = "/perfumery/{id}")
    public String deletePerfumery(@PathVariable("id") int id){
        if (perfumeryDAO.delete(id) != 0){
            messageSender.sendMessage(this.messagesQueue, "Удален объект с id " + id);
        }
        else{
            messageSender.sendMessage(this.messagesQueue,
                    String.format("Попытка удалить объект с id %d. Объект с таким id не существует", id));
        }
        return "redirect:/perfumery";
    }

    @DeleteMapping(value="/perfumery/buy/{id}")
    public String buy(@PathVariable("id") int id){
        System.out.println(id);
        messageSender.sendMessage(this.messagesQueue, "Покупка объекта с id " + id);
        perfumeryDAO.delete(id);
        return "redirect:/perfumery";
    }

}
