package rkis_8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.web.reactive.function.client.WebClient;
import rkis_8.jms.MessageReceiver;

import java.util.List;

@SpringBootApplication

public class RKISApplication {




    public static void main(String[] args) throws Exception {



        ConfigurableApplicationContext context = SpringApplication.run(RKISApplication.class, args);
        String url = "http://localhost:8080/perfumery";
        WebClient webClient = WebClient.builder().build();
        WebClientPerfumeryRequests client = new WebClientPerfumeryRequests(webClient);


        MessageReceiver messageReceiver = context.getBean(MessageReceiver.class);

        String messagesQueue = context.getEnvironment().getProperty("queue.name");


        List perfumeryList;
        Perfumery newPerfumery;
        int id;
        int choice;
        boolean restart = true;
        String menu = "1 - Получить все значения\n2 - Получить значение по id\n3 - Добавить запись\n" +
                "4 - Редактировать запись по id\n5 - Удалить запись\n6 - Вывести сообщения\n7 - Вывести меню\n8 - Выход";
        System.out.println(menu);
        while (restart){

            choice = Utilities.inputInt();

            switch (choice){
                case 1:
                    perfumeryList = client.getAllRequest(url);
                    for (Object perfumery: perfumeryList){
                        System.out.println(perfumery);
                    }
                    break;

                case 2:
                    id = Utilities.inputInt();
                    System.out.println(client.getItemRequest(url + "/" + id));
                    break;

                case 3:
                    newPerfumery = Utilities.createPerfumery();

                    client.postRequest(url, newPerfumery);
                    break;
                case 4:
                    System.out.println("Введите id");
                    id = Utilities.inputInt();
                    newPerfumery = Utilities.createPerfumery();
                    client.putRequest(url + "/" + id, newPerfumery);
                    break;
                case 5:
                    System.out.println("Введите id");
                    id = Utilities.inputInt();
                    client.deleteRequest(url + "/" + id);
                    break;
                case 6:
                    try {
                        System.out.println(messageReceiver.receiveMessage(messagesQueue));
                    }
                    catch (NullPointerException e){
                        System.out.println("Новых сообщений нет");
                    }
                    break;
                case 7:
                    System.out.println(menu);
                    break;
                case 8:
                    restart = false;
                    break;
                default:
                    System.out.println("Неверный ввод");

            }
        }

    }


}
