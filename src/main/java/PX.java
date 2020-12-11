import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PX {


    public static void main(String[] args) {

        Random random = new Random();
        List list = new ArrayList();
        for (int i=0;i<10;i++){
            list.add(random.nextInt(1000));
        }
        Collections.sort(list);
        int count = 0;

        for (Object o : list) {
            System.out.println( ++count + ":"  +o);
        }

    }
}
