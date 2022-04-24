package com.SKP;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Jan", "Kowalski", "Wrocław" ,1970));
        customers.add(new Customer("Jan", "Nowak", "Poznań" ,1980));
        customers.add(new Customer("Mateusz", "Lipnicki", "Wrocław" ,1985));
        customers.add(new Customer("Anna", "Jankowska", "Warszawa" ,1973));

        // Pobrać wszystkich klientów w Wrocławia i wyświetlić ich imiona i nazwiska dużymi literami
        List<String> names = new ArrayList<>();
        for(Customer customer: customers){
            if(customer.getCity().equals("Wrocław")){
                names.add(customer.getFirstName().toUpperCase(Locale.ROOT)+ " "+customer.getLastName().toUpperCase(Locale.ROOT));
            }
        }
        System.out.println(names);

        //Strumien
        names = customers.stream()
                .filter(c -> c.getCity().equals("Wrocław"))
                .map(c-> c.getFirstName().toUpperCase(Locale.ROOT)+" "+c.getLastName().toUpperCase(Locale.ROOT))
                .collect(Collectors.toList());
        System.out.println(names);

        //Klienci według miejscowości
        Map<String, List<Customer>> customersByCity = customers.stream()
                .collect(Collectors.groupingBy(c-> c.getCity()));

        System.out.println(customersByCity);

        // przetwarzanie równoległe obiektu ze stanem
       // state();

        //modyfikowanie źródła
        //modifySource();

        //tworzenie strumieni
        //createStream();

        //gry
        boardGamesStream();

        //leniwe strumienie
       // lazyStream();

        //goodPractices();

        // zadamie z plikami
        //filesTxt();

        //wspolrzedne
       // points();


    }

    private static void state(){
        for(int i=0; i<5; i++){
            Stream<Integer> numbers = Stream.of(1,2,3,1,2,3,1,2,3);
            StatefullClass statefullClass = new StatefullClass();
            int sum = numbers.parallel()
                    .map(statefullClass::modify)
                    .mapToInt(n -> n.intValue()).sum();
            System.out.println(sum);
        }

    }

    private static void modifySource(){
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);

        numbers.stream()
                .map(n -> numbers.add(n))
                .forEach(System.out::println);
    }

    private static void createStream(){
        // strumien z Listy
        Stream<Integer> stream1 = new LinkedList<Integer>().stream();
        // strumien z tablicy
        Stream<Integer> stream2 = Arrays.stream(new Integer[]{});
        // strumien z tekstu
        Stream<String> stream3 = Pattern.compile(".").splitAsStream("jakis dlugi tekst");

        //strumien z int
        IntStream ints = IntStream.range(0,123);
        DoubleStream doubles = DoubleStream.of(1, 2.5, 7.96);
        LongStream longs = LongStream.generate(() -> 1L);

        // strumien losowych danych
        DoubleStream randomD = new Random().doubles(5);
        IntStream randomI = new Random().ints(10);

        // pusty strumień
        Stream.empty();

        //strumien z pliku
        try {
            Stream<String> lines = new BufferedReader(new FileReader("text.txt")).lines();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void boardGamesStream(){
        List<BoardGame> games = Arrays.asList(
                new BoardGame("Terraforming Mars", 8.38, new BigDecimal("123.49"), 1, 5),
                new BoardGame("Codenames", 7.82, new BigDecimal("64.95"), 2, 8),
                new BoardGame("Puerto Rico", 8.07, new BigDecimal("149.99"), 2, 5),
                new BoardGame("Terra Mystica", 8.26, new BigDecimal("252.99"), 2, 5),
                new BoardGame("Scythe", 8.3, new BigDecimal("314.95"), 1, 5),
                new BoardGame("Power Grid", 7.92, new BigDecimal("145"), 2, 6),
                new BoardGame("7 Wonders Duel", 8.15, new BigDecimal("109.95"), 2, 2),
                new BoardGame("Dominion: Intrigue", 7.77, new BigDecimal("159.95"), 2, 4),
                new BoardGame("Patchwork", 7.77, new BigDecimal("75"), 2, 2),
                new BoardGame("The Castles of Burgundy", 8.12, new BigDecimal("129.95"), 2, 4)
        );

        // gra dla 5 osób, z oceną większą niż 8 i ceną mniejszą niż 200zł
        Stream<BoardGame> stream = games.stream();
        stream.filter(g -> g.maxPlayers >4)
                .filter(g -> g.rating >8)
                .filter(g -> new BigDecimal(200).compareTo(g.price) > 0)
                .map(g-> g.name)
                .forEach(System.out::println);

        // gra o najlepszej ocenie zawierająca literę a
        /*
        double highestRanking = 0;
        BoardGame bestGame = null;
        for (BoardGame game : games) {
            if (game.name.contains("a")) {
                if (game.rating > highestRanking) {
                    highestRanking = game.rating;
                    bestGame = game;
                }
            }
        }
        System.out.println(bestGame.name);
         */
        BoardGame bestGame = games.stream()
                .filter(g -> g.name.contains("a"))
                .max(Comparator.comparingDouble(g -> g.rating)).get();

        System.out.println(bestGame.name);
    }

    private static void lazyStream(){
        IntStream intStream = IntStream.range(0,8);
        System.out.println("Przed");
        intStream = intStream.filter(n -> n%2==0);
        System.out.println("W trakcie 1");
        intStream = intStream.peek(n -> System.out.println("> " + n));
        System.out.println("W trakcie 2");
        intStream = intStream.limit(2);
        System.out.println("W trakcie 3");
        intStream.forEach(System.out::println);
        System.out.println("Po");


    }

    private static void goodPractices(){
        int slowNumber = IntStream.range(1950,2150)
                .map(Main::timeConsumingTransformation)
                .filter(n -> n == 2000)
                .sum();
        System.out.println(slowNumber);

        int fastNumber = IntStream.range(1950,2150)
                .filter(n -> n == 2000)
                .map(Main::timeConsumingTransformation)
                .sum();
        System.out.println(fastNumber);


    }

    private static int timeConsumingTransformation(int number){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return number;
    }

    private static void filesTxt(){
        try(Stream<Path> paths = Files.walk(Paths.get("./Pliki"))){
            List<String> files = paths.map(p -> p.toString())
                    .filter(p -> p.endsWith("txt"))
                    .collect(Collectors.toList());

            files.forEach(System.out::println);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void points(){
        List<Point> points = Arrays.asList(
                new Point(1,1),
                new Point(-1, -2),
                new Point(-6, 8),
                new Point(3, -2),
                new Point(10, 20)
        );

        // punkty, których współrzędna x jest większa od y
        points.stream().filter(p -> p.x > p.y)
                .forEach(System.out :: println);

        //punkty których obie współrzędne są dodatnie
        points.stream().filter(p -> p.x > 0)
                .filter(p-> p.y > 0)
                .forEach(System.out :: println);
    }
}
