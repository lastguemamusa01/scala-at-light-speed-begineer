package playgroud;

public class JavaPlayground {

  // example of class level functionality

  public static void main(String args[]) {
    // access the constnat of the class Person, not the instance of Person(object) -> class level functionality
    System.out.println(Person.N_EYES);
  }

}

class Person {

  // universal -> all person have 2 eyes
  // define a constant in java that does not rely on an instance of person to access it
  public static final int N_EYES = 2;
}
