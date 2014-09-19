package utils;

public class Preconditions {
  public static <T> T checkNotNull(T t) {
    if (t == null) {
      System.err.println("Object is null");
    }
    return t;
  }
}
