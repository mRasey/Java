import java.util.ArrayList;

class RequestQueue {
    private ArrayList<Request> requests;

    RequestQueue() {
        //REQUIRES:none
        //MODIFIES:requests
        //EFFECTS:初始化请求队列
        requests = new ArrayList<>();
    }

    synchronized void AddRequest(Request request) {
        //REQUIRES:线程锁控制
        //MODIFIES:requests
        //EFFECTS:若请求合法则将其加入请求队列
        if (CheckValid(request) && requests.size() <= 300) {
            requests.add(request);
            System.out.println("add");
        } else {
            System.out.println("invalid request");
        }
    }

    synchronized Request getRequest(int i) {
        //REQUIRES:线程锁控制
        //MODIFIES:none
        //EFFECTS:返回队列中的指定位置的请求
        Request request = null;
        try {
            request = requests.get(i);
        } catch (Exception exception) {
            System.out.print("");
        }
        return request;
    }

    synchronized Request RemoveRequest(int i) {
        //REQUIRES:线程锁控制
        //MODIFIES:requests
        //EFFECTS:返回队列中的指定位置的请求，并将该请求从队列中移除
        Request request = null;
        try {
            request = requests.remove(i);
        } catch (Exception exception) {
            System.out.print("");
        }
        return request;
    }

    synchronized int getLength() {
        //REQUIRES:线程锁控制
        //MODIFIES:none
        //EFFECTS:返回队列长度
        return requests.size();
    }

    private boolean CheckValid(Request request) {
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:判断请求合法性并返回布尔值
        return ((request.getPosition()[0] >= 0 && request.getPosition()[0] <= 79)
                && (request.getPosition()[1] >= 0 && request.getPosition()[1] <= 79)
                && (request.getTarget()[0] >= 0 && request.getTarget()[1] <= 79)
                && (request.getTarget()[0] >= 0 && request.getTarget()[1] <= 79)
                && (request.getPosition()[0] != request.getTarget()[0] || request.getPosition()[1] != request.getTarget()[1]));
    }
}
