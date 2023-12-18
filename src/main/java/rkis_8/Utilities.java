package rkis_8;

import java.util.InputMismatchException;
import java.util.Scanner;

/**Класс вспомогательных функций приложения*/
public class Utilities {

    static Scanner scanner = new Scanner(System.in);

    /**
     * Ввод целого числа с консоли. Ввод будет продолжаться, пока не будет введено верное число
     * @return Целое число int
     */
    static int inputInt(){
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Incorrect input.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Ввод числа с плавающей запятой с консоли. Ввод будет продолжаться, пока не будет введено верное число.
     * В качестве разделителя целой и дробной части используется запятая
     * @return число с плавающей запятой.
     */
    static float inputFloat() {
        while (true) {
            try {
                return scanner.nextFloat();
            } catch (InputMismatchException e) {
                System.out.println("Incorrect input.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Создание нового экземпляра класса Perfumery на основе значений, введенных пользователем с консоли
     * @return экземпляр класса Perfumery или null, если были введены некорректные значения
     */
    static public Perfumery createPerfumery(){
        scanner.nextLine();
        System.out.println("Введите значение type");
        String type = scanner.nextLine();
        System.out.println("Введите значение color");
        String color = scanner.nextLine();
        System.out.println("Введите значение aroma");
        String aroma = scanner.nextLine();
        System.out.println("Введите значение volume");
        int volume = inputInt();
        System.out.println("Введите значение concentration(от 0 до 1)");
        float concentration = inputFloat();
        try {
            return new Perfumery(0, type, color, aroma, volume, concentration);
        }
        catch (IllegalArgumentException e){
            System.out.println("Введены некорректные значения");
            return null;
        }
    }

}
