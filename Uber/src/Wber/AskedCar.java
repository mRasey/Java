package Wber;

public class AskedCar {
    private Passenger passenger;
    private Car car;

    public AskedCar(Passenger passenger, Car car) {
        //Requires: passenger, car
        //Modifies: none
        //Effects: 构造器
        this.passenger = passenger;
        this.car = car;
    }

    public void setPassenger(Passenger passenger) {
        //Requires: 乘客
        //Modifies: none
        //Effects: 设置乘客属性
        this.passenger = passenger;
    }

    public void setCar(Car car) {
        //Requires: 车
        //Modifies: none
        //Effects: 设置车属性
        this.car = car;
    }

    public Passenger getPassenger() {
        //Requires: none
        //Modifies: none
        //Effects: 返回当前乘客属相
        return passenger;
    }

    public Car getCar() {
        //Requires: none
        //Modifies: none
        //Effects: 返回当前车属性
        return car;
    }
}
