public class MainClass {

    //  метод, который возвращает число 14
    public int getLocalNumber() {
        int number = 14;
        return number;

    }

    //приватное поле класса, которое равно 20
    private int class_number = 20;


    //публичный метод (getClassNumber), который возвращает переменную class_number
    public int getClassNumber() {
        return class_number;
    }


    //приватное поле класса, которое равно строке “Hello, world”
    private String class_string = "Hello, world";

    //публичный метод (назвать: getClassString), который возвращает строку class_string.

    public String getClassString() {
        return class_string;
    }

}
