package npp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Аспект логирования Spring Bean-ов панели управления АЭС.
 *
 * Логирует:
 * - вызовы фабрик (ButtonFactory, LampFactory) — @Before + @AfterReturning
 * - выполнение и отмену команд (CommandHistory) — @Around
 * - построение панели (RandomPanelBuilder) — @Before + @AfterReturning
 */
@Component
@Aspect
public class LoggingAspect {

    @Pointcut("execution(* npp.factory.ButtonFactory.*(..))")
    private void buttonFactoryMethods() {}

    @Pointcut("execution(* npp.factory.LampFactory.*(..))")
    private void lampFactoryMethods() {}

    @Pointcut("execution(* npp.command.CommandHistory.executeCommand(..))")
    private void executeCommandMethod() {}

    @Pointcut("execution(* npp.command.CommandHistory.undoLast(..))")
    private void undoLastMethod() {}

    @Pointcut("execution(* npp.builder.RandomPanelBuilder.build(..))")
    private void panelBuildMethod() {}

    @Pointcut("execution(* npp.builder.RandomPanelBuilder.placeComponents(..))")
    private void placeComponentsMethod() {}

    @Pointcut("execution(* npp.builder.RandomPanelBuilder.configureBindings(..))")
    private void configureBindingsMethod() {}

    @Before("buttonFactoryMethods()")
    public void beforeButtonFactory(final JoinPoint jp) {
        System.out.println("[LOG] ButtonFactory." + jp.getSignature().getName()
                + " вызван с параметрами: " + argsToString(jp.getArgs()));
    }

    @AfterReturning(pointcut = "buttonFactoryMethods()", returning = "result")
    public void afterReturningButtonFactory(final JoinPoint jp, final Object result) {
        System.out.println("[LOG] ButtonFactory." + jp.getSignature().getName()
                + " создал: " + result);
    }

    @Before("lampFactoryMethods()")
    public void beforeLampFactory(final JoinPoint jp) {
        System.out.println("[LOG] LampFactory." + jp.getSignature().getName()
                + " вызван с параметрами: " + argsToString(jp.getArgs()));
    }

    @AfterReturning(pointcut = "lampFactoryMethods()", returning = "result")
    public void afterReturningLampFactory(final JoinPoint jp, final Object result) {
        System.out.println("[LOG] LampFactory." + jp.getSignature().getName()
                + " создал: " + result);
    }

    @Around("executeCommandMethod()")
    public Object aroundExecuteCommand(final ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        String cmdDesc = args.length > 0 ? args[0].toString() : "?";
        System.out.println("[LOG] CommandHistory: начало выполнения команды — " + cmdDesc);
        Object result = pjp.proceed();
        System.out.println("[LOG] CommandHistory: команда успешно выполнена — " + cmdDesc);
        return result;
    }

    @Around("undoLastMethod()")
    public Object aroundUndoLast(final ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("[LOG] CommandHistory: запрос отмены последней команды");
        Object result = pjp.proceed();
        System.out.println("[LOG] CommandHistory: отмена завершена");
        return result;
    }

    @Before("panelBuildMethod()")
    public void beforeBuild() {
        System.out.println("[LOG] RandomPanelBuilder.build() — финализация панели");
    }

    @AfterReturning("panelBuildMethod()")
    public void afterBuild() {
        System.out.println("[LOG] RandomPanelBuilder.build() — панель успешно построена");
    }

    @Before("placeComponentsMethod()")
    public void beforePlaceComponents() {
        System.out.println("[LOG] RandomPanelBuilder.placeComponents() — размещение компонентов");
    }

    @Before("configureBindingsMethod()")
    public void beforeConfigureBindings() {
        System.out.println("[LOG] RandomPanelBuilder.configureBindings() — настройка связей");
    }

    @AfterThrowing(pointcut = "execution(* npp.factory..*(..)) || execution(* npp.command..*(..)) || execution(* npp.builder..*(..)) || execution(* npp.menu..*(..)) ", throwing = "ex")
    public void afterThrowing(final JoinPoint jp, final Throwable ex) {
        System.out.println("[LOG] ИСКЛЮЧЕНИЕ в " + jp.getSignature()
                + ": " + ex.getMessage());
    }

    private String argsToString(final Object[] args) {
        if (args == null || args.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(args[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
