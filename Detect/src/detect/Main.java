package detect;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        try {
            int detecterNumber = 0;
            Detecter[] detecters = new Detecter[9];
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            while (!input.equals("END")) {
                detecters[detecterNumber] = new Detecter(input, detecterNumber);
                detecterNumber++;
                if(detecterNumber > 8) {
                    System.out.println("线程过多，程序退出");
                    System.exit(0);
                }
                input = scanner.nextLine();
            }
            for (int i = 0; i < detecterNumber; i++) {/*开始所有有效线程*/
                new Thread(detecters[i]).start();
            }
            new Thread(new test()).start();/*在这里创建测试文件*/
            while(true){
                if(scanner.hasNextLine())
                    input = scanner.nextLine();
                if(input.equals("exit")) {
                    System.out.println("程序退出");
                    System.exit(0);
                }
            }
        }
        catch (Throwable t){
            //t.printStackTrace();
        }
        finally {
            System.out.println("程序发生故障，程序结束");
            System.exit(0);
        }
    }
}
