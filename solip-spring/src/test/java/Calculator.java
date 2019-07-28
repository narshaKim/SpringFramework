import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer calcSum(String filepath) throws IOException {
        return lineReadTemplate(filepath, new LineCallback() {
            public Integer doSomethingWithLine(String line, int value) throws IOException {
                return value + Integer.parseInt(line);
            }
        }, 0);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        return lineReadTemplate(filepath, new LineCallback() {
            public Integer doSomethingWithLine(String line, int value) throws IOException {
                return value * Integer.parseInt(line);
            }
        }, 1);
    }

    private Integer lineReadTemplate(String filepath, LineCallback callback, int initValue) throws IOException {
        BufferedReader br = null;
        try {
            int res = initValue;
            String line = null;
            br = new BufferedReader(new FileReader(filepath));
            while ((line=br.readLine())!=null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            throw e;
        } finally {
            if(br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }

}
