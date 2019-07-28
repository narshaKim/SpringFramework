import java.io.IOException;

public interface LineCallback {

    Integer doSomethingWithLine(String line, int value) throws IOException;

}
