import org.junit.Assert;
import org.junit.Test;

public class MainClassTest extends MainClass {


    // тест, проверяющий, что метод getLocalNumber возвращает число 14
    @Test
    public void testGetLocalNumber() {
        int expected = 14;
        Assert.assertEquals("Ожидаемое число неверное", expected, getLocalNumber());

    }

    // тест , который проверяет, что метод getClassNumber возвращает число больше 45.
    @Test
    public void testGetClassNumber() {
        int classNumber = getClassNumber();

        Assert.assertTrue("Возвращаемое число должно быть больше 45", classNumber > 45);

    }

    //тест, который проверяет, что метод getClassString возвращает строку, в которой есть подстрока “hello” или “Hello”, если нет ни одной из подстрок - тест падает.
    @Test
    public void testGetClassString(){
        String classString = getClassString();
        Assert.assertTrue("Строка класса должна содержать «hello» или «Hello».",classString.toLowerCase().contains("hello"));

    }
}
