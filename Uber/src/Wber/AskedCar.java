package Wber;

public class AskedCar {
    private Passenger passenger;
    private Car car;

    public AskedCar(Passenger passenger, Car car) {
        this.passenger = passenger;
        this.car = car;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Car getCar() {
        return car;
    }
}
