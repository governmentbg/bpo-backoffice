package bg.duosoft.ipas.util.function;

/**
 * User: ggeorgiev
 * Date: 28.01.2021
 * Time: 13:06
 */
@FunctionalInterface
public interface FourArgsFunction<A, B, C, D, R> {
    R apply(A a, B b, C c, D d);
}
