package npp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Конфигурационный класс Spring-приложения.
 *
 * @ComponentScan — сканирует весь пакет npp для обнаружения бинов.
 * @EnableAspectJAutoProxy — включает поддержку AOP аспектов.
 * proxyTargetClass = true — позволяет создавать прокси для классов без интерфейсов.
 */
@Configuration
@ComponentScan(basePackages = "npp")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
}
