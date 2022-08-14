public class Point {
    double x;
    double y;

    /**
     * @param x stores the x cord of a point
     * @param y stores the y cord of a point
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return returns the x cord value
     */
    double getX(){
        return this.x;
    }
    /**
     * @return returns the y cord value
     */
    double getY(){
       return this.y;
    }
}
