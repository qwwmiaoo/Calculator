import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        RPNCalculator rpnCalculator = new RPNCalculator(15);
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入运算式：");
        while (true) {
            String intputMsg = sc.nextLine();
            if (intputMsg.equals("exit")) {
                break;
            }
            System.out.println("stack:" + rpnCalculator.getResult(intputMsg));
        }
        System.out.println("运算结束");
    }
}