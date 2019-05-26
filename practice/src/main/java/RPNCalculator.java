import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class RPNCalculator {
    private final int scale;

    private Stack<BigDecimal> nums;

    private Stack<BigDecimal> numsBackUp;

    private Stack<String> operatorBackUp;


    public RPNCalculator(int scale) {
        this.scale = scale;

        if (nums == null) {
            nums = new Stack<>();
        }

        if (numsBackUp == null) {
            numsBackUp = new Stack<>();
        }

        if (operatorBackUp == null) {
            operatorBackUp = new Stack<>();
        }
    }

    public String getResult(String inputStr) {
        String[] arrayList = inputStr.split(" ");
        Queue<String> strQueue = buildQueue(arrayList);
        return compute(strQueue);
    }

    private Queue<String> buildQueue(String[] arrayList) {
        Queue<String> queue = new LinkedList<String>();
        for (String arr : arrayList) {
            queue.offer(arr);
        }
        return queue;
    }

    private String compute(Queue<String> msgQueue) {
        if (msgQueue.isEmpty() || msgQueue.size() == 0) {
            return null;
        }

        while (msgQueue.size() != 0) {
            String val = msgQueue.poll();

            if (isNumber(val)) {
                nums.push(new BigDecimal(val));
                continue;
            }

            operatorBackUp.push(val);
            switch (val) {
                case "clear":
                    clearStack();
                    break;
                case "sqrt":
                    sqrt();
                    break;
                case "undo":
                    undo();
                    break;
                default:
                    if (nums.size() == 1 || nums.size() == 0) {
                        return nums.toString() + "\n" + "操作符与数字不符,只输入可计算部分:" + val;
                    }
                    basicOperate(val);
            }
        }
        return nums.toString();
    }

    private static boolean isNumber(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        for (int i = str.length(); --i >= 0; ) {
            char val = str.charAt(i);
            if (!Character.isDigit(val) || val == '.') {
                return false;
            }
        }
        return true;
    }

    private void undo() {
        if (nums.size() == 0) {
            return;
        }
        nums.pop();

        if (operatorBackUp.pop().equals("sqrt")) {
            nums.push(numsBackUp.pop());
        } else {
            nums.push(numsBackUp.pop());
        }
    }


    private void sqrt() {
        BigDecimal num = nums.pop();
        numsBackUp.push(num);
        //numsBackUp.push("sqrt");
        nums.push(new BigDecimal(bigDecimalSqrt(num)));
    }

    private void clearStack() {
        nums.clear();
        numsBackUp.clear();
        operatorBackUp.clear();
    }

    private String bigDecimalSqrt(BigDecimal in) {
        BigDecimal sqrt = new BigDecimal(1);
        sqrt.setScale(scale + 3, RoundingMode.FLOOR);
        BigDecimal store = new BigDecimal(in.toString());
        boolean first = true;
        do {
            if (!first) {
                store = new BigDecimal(sqrt.toString());
            } else first = false;
            store.setScale(scale + 3, RoundingMode.FLOOR);
            sqrt = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store).divide(
                    BigDecimal.valueOf(2), scale + 3, RoundingMode.FLOOR);
        } while (!store.equals(sqrt));
        return sqrt.setScale(scale, RoundingMode.FLOOR).stripTrailingZeros().toPlainString();
    }

    private void basicOperate(String val) {
        BigDecimal num2 = nums.pop();
        BigDecimal num1 = nums.pop();
        BigDecimal result = basicOperateResult(val.charAt(0), num1, num2);
        nums.push(result);
        numsBackUp.push(num2);
        numsBackUp.push(num1);
    }

    private BigDecimal basicOperateResult(char op, BigDecimal num1, BigDecimal num2) {
        switch (op) {
            case '+':
                return num1.add(num2);
            case '-':
                return num1.subtract(num2);
            case '*':
                return num1.multiply(num2);
            case '/':
                return num1.divide(num2);
            default:
                return null;
        }
    }

}
