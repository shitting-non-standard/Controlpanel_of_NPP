package npp;

import npp.config.AppConfig;
import npp.menu.MenuController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Точка входа в приложение "Панель управления АЭС".
 *
 * Контекст создаётся, используется и закрывается здесь.
 * MenuController получается из контекста и запускает интерактивное меню.
 */
public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        // Принудительно переключаем stdout и stderr на UTF-8,
        // чтобы русские символы отображались корректно в любой среде.
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        // Контекст создаётся один раз в main — не передаётся по программе
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // Получаем единственный нужный бин из контекста — всё остальное через DI
        MenuController controller = context.getBean(MenuController.class);
        controller.start();

        // Контекст закрывается здесь же
        context.close();
    }
}
