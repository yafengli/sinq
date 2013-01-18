package test;

import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;

/**
 * User: YaFengLi
 * Date: 12-12-28
 * Time: 上午10:16
 */
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
    }
}

class TestClose implements Closeable {
    @Override
    public void close() throws IOException {
        System.out.println("#What the fucking.");

    }
}
