
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class Mapp {
    public static void main(String[] args) throws IOException {

        FileInputStream fis;
        ObjectInputStream ois;



        fis = new FileInputStream("login_password.ser");
        ois = new ObjectInputStream(fis);
        try {
            Map anotherMap = (Map) ois.readObject();
            String s = "I_AM_STUPID";
            System.out.println(anotherMap.get(s).equals("123"));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();


    }
}
