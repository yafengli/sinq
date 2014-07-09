package test;

import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;

public class FeatureTest {
    @Test
    public void testCritiera() {
        try (TestClose tc = new TestClose()) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        say();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void say() {
        TestClose tc = new TestClose();
        System.out.printf("%s\n", tc);
    }
}

class TestClose implements Closeable {
    @Override
    public void close() throws IOException {
        System.out.println("#What the fucking.");
    }
}
