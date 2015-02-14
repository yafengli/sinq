package test;

import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

import static java.lang.String.valueOf;
import static java.lang.System.out;

public class FeatureTest {

  @Test
  public void testCritiera() {
    try (TestClose tc = new TestClose()) {
      say((t) -> -1L);
      say((t) -> {
        out.println(t);
      });

      IntConsumer func = String::valueOf;
      "ABCDEFG".chars().forEach(c -> out.println(valueOf(c)));
      "ABCDEFG".chars().forEach(out::println);
      "ABCDEFG".chars().forEach(func);
      "ABCDEFG".chars().forEach(tc::printChar);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void say(Function<String, Long> call) {
    out.println("Function<String,Long>=" + call.apply("Hello"));
  }

  public void say(Consumer<String> call) {
    out.print("Consumer<String>:");
    call.accept("test");
  }
}

class TestClose implements Closeable {
  @Override
  public void close() throws IOException {
    out.println("#What the fucking.");
  }

  public void printChar(int aChar) {
    out.println((char) aChar);
  }

  public static void sprintChar(int aChar) {
    out.println((char) aChar);
  }
}
