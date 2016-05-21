package Wber;

public class AskedCar {
    /*Overview
    这个类保存一个配对关系，既每个乘客和乘客最终请求的出租车，用以向控制中心发出请求，passenger表示发出请求的乘客，car表示乘客选择的车
    */
    private Passenger passenger;
    private Car car;

    public AskedCar(Passenger passenger, Car car) {
        //Requires: passenger和car不为NULL
        //Effects: 构造器
        this.passenger = passenger;
        this.car = car;
    }

    public boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
        if(passenger == null) return false;
        if(car == null) return false;
        return true;
    }

    public void setPassenger(Passenger passenger) {
        //Requires: passenger不为NULL
        //Modifies: passenger
        //Effects: 设置类里的passenger属性为passenger
        this.passenger = passenger;
    }

    public void setCar(Car car) {
        //Requires: car不为NULL
        //Modifies: none
        //Effects: 设置类中car属性为car
        this.car = car;
    }

    public Passenger getPassenger() {
        //Requires: none
        //Modifies: none
        //Effects: 返回passenger属性
        return passenger;
    }

    public Car getCar() {
        //Requires: none
        //Modifies: none
        //Effects: 返回当car属性
        return car;
    }
}
