import java.util.Scanner;

public class TestShu {

    public static void main(String[] args) {
        /*Scanner sc = new Scanner(System.in);
        System.out.println("请输入a的值:");
        int a = sc.nextInt();
        int s = a;
        System.out.println("请输入有几个数相加:");
        int d = sc.nextInt();

        int g = 0;
        for(int i=0;i<d;i++){
            g = g + a;
            a  = a*10 + s ;
        }
        System.out.println("结果是: " + g);*/


        for(int i = 6;i<=1000;i++){
            int h = 0;
            for(int j = 1;j<i;j++){
                if(i%j==0){
                    h = h + j;
                    if(h==i){
                        System.out.println(i);
                    }
                }
            }
        }
    }





}
