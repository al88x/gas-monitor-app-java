package model;

public class Location {

    private String id;
    private double x;
    private double y;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
