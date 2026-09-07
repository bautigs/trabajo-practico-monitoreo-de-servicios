package tp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class App {
  public static void main(String[] args) throws IOException {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(App.class, args);
  }
}
