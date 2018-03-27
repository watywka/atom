
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class Mapp {
    public static void main(String[] args) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("root","qwerty123");

        FileOutputStream fos = new FileOutputStream("login_password.ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(map);
            oos.close();
        }
        FileInputStream fis;
        ObjectInputStream ois;



        fis = new FileInputStream("login_password.ser");
        ois = new ObjectInputStream(fis);
        try {
            Map anotherMap = (Map) ois.readObject();
            System.out.println(anotherMap);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();


    }
}
