package com.example.carrentaldekstop;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Statisztika {
    private static List<Car> cars;
    public static void main(String[] args) {
        System.out.println("elindult a statisztika");
        try {
            beolvas();
            kiir();
        } catch (SQLException e) {
            System.out.println("Hiba történt a kapcsolódás során!");
            System.out.println(e.getMessage());
        }
    }

    private static void kiir() {
        System.out.printf("20.000 Ft-nál olcsóbb napidíjú autók száma : %d\n", count20Ezer());
        System.out.printf("%s az adatok között 26.000 Ft-nál drágább napidíjú autó\n", dragabb26()? "Van":"Nincs");
        Car legdragabb=mostExpensive();
        System.out.printf("Legdrágább napidíjú autó: %s - %s – %s –%s Ft\n",legdragabb.getLicense_plate_number(),legdragabb.getBrand(),legdragabb.getModel(),legdragabb.getDaily_cost());
        System.out.println("Autók száma:");
        Map<String, Long>groupBy=groupByBrand();
        for (Map.Entry<String, Long> count:groupBy.entrySet()) {
            System.out.printf("\t%s: %d\n",count.getKey(),count.getValue());
        }
        System.out.println("Adjon meg egy rendszámot: ");
        String plate=getPlate();
        nagyobbE(plate);
    }

    private static void nagyobbE(String plate) {
        int index=0;
        while (index<cars.size() && !(cars.get(index).getLicense_plate_number().equals(plate))){
            index++;
        }
        if (index==cars.size()){
            System.out.println("Nincs ilyen autó");
        }else{
            System.out.printf("A megadott autó napidíja %s mint 25.000 Ft", cars.get(index).getDaily_cost()>25000? "nagyobb" : "nem nagyobb");
        }
    }

    private static String getPlate() {
        Scanner scanner=new Scanner(System.in);
        return scanner.nextLine();
    }

    private static Map<String, Long> groupByBrand() {
        return cars.stream().collect(Collectors.groupingBy(car -> car.getBrand(),Collectors.counting()));
    }

    private static Car mostExpensive() {
        return cars.stream().max(Comparator.comparing(car -> car.getDaily_cost())).get();
    }

    private static boolean dragabb26() {
        return cars.stream().anyMatch(car -> car.getDaily_cost()>26000);
    }

    private static long count20Ezer() {
        return cars.stream().filter(car -> car.getDaily_cost()<20000).count();
    }

    private static void beolvas() throws SQLException {
        CarDB db =new CarDB();
        cars=db.getCars();
    }
}
