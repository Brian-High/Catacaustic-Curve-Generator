import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.ArrayList;


public class Main extends Application {
    /**
     * Setting up the JavaFX elements that are accessed in methods
     *    other than start
     * These are instance varibles because their are too many varibles
     *    to input through each methods parameter
     */
    TextField multiplierTxtFld;
    TextField multiplierAnimationTxtFld;
    CheckBox multiplierAnimationChkBox;
    TextField numPtsTxtFld;
    TextField numPtsAnimationTxtFld;
    CheckBox numPtsAnimationChkBox;

    Slider fpsSlider;
    Label fpsLbl2;

    Canvas canvas;
    GraphicsContext gc;

    ArrayList<Point> pointsList;

    String numPointsString;
    double numPoints;
    String multiplierString;
    double multiplier;
    double height;
    double width;
    ColorPicker colorPicker;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /**
         * Setting up all of the JavaFx Elements
         */
        primaryStage.setX(600);
        width = primaryStage.getX();
        primaryStage.setY(600);
        height = primaryStage.getY();
        VBox bottomRtVbox = new VBox();
        VBox bottomLftVbox = new VBox();

        HBox bottomHbox = new HBox();
        StackPane animationPane = new StackPane();
        VBox bigVbox = new VBox();

        Label multiplierAnimationLbl = new Label("Animation Increment");
        multiplierAnimationTxtFld = new TextField();
        multiplierAnimationChkBox = new CheckBox("Animate Multiplier");

        Label numPtsAnimationLbl = new Label("Animation Increment");
        numPtsAnimationTxtFld = new TextField();
        numPtsAnimationChkBox = new CheckBox("Animate Number of Points");

        Circle circ = new Circle(width * .5, height * .3, width * .5, Color.WHITE);
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        animationPane.getChildren().addAll(circ, canvas);


        numPtsTxtFld = new TextField();
        Button runBtn = new Button("Run");
        Button pauseBtn = new Button("Pause");
        HBox btnHbox = new HBox();
        btnHbox.getChildren().addAll(runBtn,pauseBtn);
        Label numPtsLbl = new Label("Number of Points");
        Scene scene = new Scene(bigVbox);
        bottomLftVbox.getChildren().addAll(numPtsLbl, numPtsTxtFld,numPtsAnimationLbl, numPtsAnimationTxtFld,
                numPtsAnimationChkBox, btnHbox);

        Label multiplierLbl = new Label("Multiplier Value");
        multiplierTxtFld = new TextField();
        bottomRtVbox.getChildren().addAll(multiplierLbl, multiplierTxtFld, multiplierAnimationLbl, multiplierAnimationTxtFld,
                multiplierAnimationChkBox);

        VBox bottomMidVbox = new VBox();
        Label colorSetLbl = new Label("Color");
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        Label fpsLbl = new Label("Set Frame Rate");
        fpsLbl2 = new Label();
        fpsSlider = new Slider();
        fpsSlider.setMinorTickCount(1);
        fpsSlider.setMajorTickUnit(2);
        fpsSlider.setBlockIncrement(1);
        fpsSlider.setSnapToTicks(true);
        fpsSlider.setShowTickMarks(true);
        fpsSlider.setMin(1);
        fpsSlider.setMax(60);
        fpsLbl2.setText(Double.toString(fpsSlider.getValue()));
        bottomMidVbox.getChildren().addAll(colorSetLbl,colorPicker,fpsLbl,fpsSlider,fpsLbl2);

        bottomHbox.getChildren().addAll(bottomLftVbox,bottomMidVbox,bottomRtVbox);
        bottomHbox.setAlignment(Pos.CENTER);
        bottomHbox.setSpacing(200);
        bigVbox.getChildren().addAll(animationPane, bottomHbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello World");
        primaryStage.show();

        /**
         * Animation Timer set up
         * If condition is set up to run setPoints() and drawLines()
         *   when NextTime is less than the current time
         * NextTime is calculated by taking the Value of the fpsSlider,
         *   converting it to nano seconds, and adding it to the current time
         * Then the fpsLabel is updated
         * Then the graphics context is cleared
         * Then setPoints() and drawLines() are run
         */

        AnimationTimer timer = new AnimationTimer() {
            private long nextTime = 0;

            @Override
            public void handle(long now) {
                if(now > nextTime) {
                    long temp = (long)(1000000000*(1/fpsSlider.getValue()));
                    nextTime = now + temp;
                    fpsLbl2.setText(Double.toString((int)fpsSlider.getValue()));
                    gc.clearRect(0, 0 , canvas.getWidth(), canvas.getHeight());
                    setPoints();
                    drawLines();
                }
            }
        };


        /**
         * The run button starts the animation
         * The number of points and the multiplier value are received
         *    from their corresponding text fields and converted into doubles
         * Then the fpsLabel is updated
         * And the timer is started
         */
        runBtn.setOnAction((event) -> {
            numPointsString = numPtsTxtFld.getCharacters().toString();
            numPoints = Double.parseDouble(numPointsString);
            multiplierString = multiplierTxtFld.getCharacters().toString();
            multiplier = Double.parseDouble(multiplierString);
            fpsLbl2.setText(Double.toString((int)fpsSlider.getValue()));
            timer.start();
        });

        /**
          The pause button pauses the simulation by stopping the timer
         */
        pauseBtn.setOnAction((event) -> {
            timer.stop();
        });
    }

    public static void main(String[] args) {
        launch();
    }


    /**
     * setPoints() caluates the cordinates of the external points and draws them in
     *    the graphics context
     * The color of the stroke of the graphics context is set from the color picker
     * Rads is the angle from the center of the circle to the first external point
     * (note) rads * the number of points = 2pi
     * A for loop runs from 0 to 2pi and is incremented by rads
     *    each x and y coordinate for each point is calculated by taking the sine or
     *    cossine from the center of the circle and adding the radius of the circle
     * These coordinates are used to create a new point p which is stored in an arraylist
     * Then each point is drawn in the graphics context
     */
    private void setPoints(){
        pointsList = new ArrayList();
        double rads = 2 * (Math.PI) / numPoints;
        double yCord;
        double xCord;
        gc.setStroke(colorPicker.getValue());

        for (double i = 0; i < 2 * Math.PI; i += rads) {
            yCord = (Math.sin(i) * height/2) + height/2;
            xCord = (Math.cos(i) * width/2) + width/2;
            Point p = new Point(xCord,yCord);
            pointsList.add(p);
            gc.strokeOval(xCord - 2, yCord - 2, 4, 4);
            gc.fillOval(xCord - 2, yCord - 2, 4, 4);
        }

    }

    /**
     * drawlines() increments the multiplier value and the numpoints value if applicable
     *    then draws the lines between corresponding points according to the multiplier value
     */
    void drawLines() {
        int pt1;
        int pt2;
        /**
         * This if condition checks if the multiplier value should be incremented
         * if it should be incremented it gets the increment value from its corresponding
         *    text field (note- if this text field is empty a default value is chosen of .01)
         * The multiplier value is incremented by adding the increment value
         * Then this incremented multiplier value is shown in its corresponding label
         */
        if(multiplierAnimationChkBox.isSelected() && multiplier>0) {
            String tempString = multiplierAnimationTxtFld.getText();

            if(tempString.length() > 0){
                multiplier += Double.parseDouble(tempString);
                System.out.println(multiplier);
            } else {
                multiplier += .01;
            }
            multiplierTxtFld.setText(Double.toString(multiplier));
        }
        /**
         * This if condition checks if the numPoints value should be incremented
         * if it should be incremented it gets the increment value from its corresponding
         *    text field (note- if this text field is empty a default value is chosen of .1)
         * The numPoints value is incremented by adding the increment value
         * Then this incremented numPoints value is shown in its corresponding label
         */
        if(numPtsAnimationChkBox.isSelected() && numPoints>0) {
            String tempString;
            tempString =numPtsAnimationTxtFld.getText();
            if(tempString.length() > 0){
                numPoints += Double.parseDouble(tempString);
            } else {
                numPoints += .1;
            }
            numPtsTxtFld.setText(Double.toString((int)numPoints));
        }
        /**
         * This for loop calculates the beginning and end points of each line
         * Starting from 0 pt1 is chosen by incrementing i
         * (note if the index exceeds the size of the pointsList it is decremented by numPoints
         *    until in bounds)
         * This represents index of the beginning point of each line
         * The end point is calculated by taking the next index and multiplying by the multiplier
         *    value
         * (note if the index exceeds the size of the pointsList it is decremented by numPoints
         *    until in bounds)
         * This represents index of the ending point of each line
         * Then pointsList is accessed using the beginning point index and ending point index and
         *    a line is drawn
         * After all the lines have been drawn pointsList is cleared
         */
        for (int i = 0; i < pointsList.size(); i++) {
            pt1 = i;
            while (pt1 > pointsList.size() - 1) {
                if (pt1 > pointsList.size() - 1) {
                    pt1 -= numPoints;
                }
            }
            pt2 = (int) (i * multiplier);
            while (pt2 > pointsList.size() - 1) {
                if (pt2 > pointsList.size() - 1) {
                    pt2 -= numPoints;
                }
            }

            gc.strokeLine((pointsList.get(pt1)).getX(),(pointsList.get(pt1)).getY(),
                    (pointsList.get(pt2)).getX(),(pointsList.get(pt2)).getY());
        }
        pointsList.clear();
    }
}
