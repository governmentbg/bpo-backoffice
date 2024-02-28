package bg.duosoft.ipas.test.diff;

import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 24.11.2021
 * Time: 14:50
 */
public class DiffTest {
    @Data
    @AllArgsConstructor
    public static class Main {
        private Integer id;
        private String name;
        private List<Child> children;
    }
    @Data
    @AllArgsConstructor
    public static class Child {
        private Integer id;
        private String name;
        private List<SubChild> subchildren;
    }

    @Data
    @AllArgsConstructor
    public static class SubChild {
        private String id;
    }

    public static void main(String[] args) {
        Child c1 = new Child(1, "test-1", Arrays.asList(new SubChild("1"), new SubChild("2")));
        Child c2 = new Child(2, "test-2", Arrays.asList(new SubChild("3"), new SubChild("4")));
        Child c21 = new Child(2, "test-21", Arrays.asList(new SubChild("4"), new SubChild("5")));
        Child c3 = new Child(3, "test-3", Arrays.asList(new SubChild("7"), new SubChild("8")));
        Main original = new Main(1, "main-test", Arrays.asList(c1, c2));
        Main changed = new Main(1, "main-test", Arrays.asList(c1, c21, c3));

        DiffGenerator generator = DiffGenerator.create(original, changed);
        System.out.println(">" + generator.getResult() + "<");
    }
}
