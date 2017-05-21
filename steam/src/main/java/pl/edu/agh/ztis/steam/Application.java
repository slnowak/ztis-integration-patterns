package pl.edu.agh.ztis.steam;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;


public class Application {

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("works!");

        while (true) {
            TimeUnit.SECONDS.sleep(15);
        }
    }
}
