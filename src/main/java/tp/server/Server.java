package tp.server;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.JavalinRenderer;
import tp.server.handlers.AppHandlers;
import tp.server.middlewares.AuthMiddleware;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Server {
  private static Javalin app = null;
  public static EntityManagerFactory entityManagerFactory;

  public static Javalin app() {
    if(app == null)
      throw new RuntimeException("App no inicializada");
    return app;
  }

  public static void init() throws IOException {
    if(app == null) {
      int port = Integer.parseInt(System.getProperty("port", "8080"));
      app = Javalin.create(config()).start(port);
      entityManagerFactory = createEntityManagerFactory();
      initTemplateEngine();
      AppHandlers.applyHandlers(app);
      Router.init();
    }
  }

  private static EntityManagerFactory createEntityManagerFactory() {
    Map<String, String> env = System.getenv();
    Map<String, Object> configOverrides = new HashMap<String, Object>();

    String[] keys = new String[] {
            "jakarta.persistence.jdbc.url",
            "jakarta.persistence.jdbc.user",
            "jakarta.persistence.jdbc.password",
            "jakarta.persistence.jdbc.driver",
            "hibernate.hbm2ddl.auto"
    };

    for (String key : keys) {
      if (env.containsKey(key)) {
        String value = env.get(key);
        configOverrides.put(key, value);
      }
    }
    return Persistence.createEntityManagerFactory("db", configOverrides);
  }

  private static Consumer<JavalinConfig> config() {
    return config -> {
      config.staticFiles.add(staticFiles -> {
        staticFiles.hostedPath = "/";
        staticFiles.directory = "/public";
      });
      AuthMiddleware.apply(config);
    };
  }


  private static void initTemplateEngine() {
    JavalinRenderer.register(
        (path, model, context) -> { // Función que renderiza el template
          Handlebars handlebars = new Handlebars();
          Template template = null;
          try {
            template = handlebars.compile(
                "templates/" + path.replace(".hbs",""));
            return template.apply(model);
          } catch (IOException e) {
            e.printStackTrace();
            context.status(HttpStatus.NOT_FOUND);
            return "No se encuentra la página indicada...";
          }
        }, ".hbs" // Extensión del archivo de template
    );
  }
}
