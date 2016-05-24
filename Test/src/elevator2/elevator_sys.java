package elevator2;

public class elevator_sys {
	public static void main(String[] args) {
		queue  que = new queue();
        input in = new input(que);
        scheduler sch = new scheduler(que);
        in.start();
        sch.start();
  } 
}
