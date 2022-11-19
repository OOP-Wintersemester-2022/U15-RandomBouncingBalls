import de.ur.mi.oop.app.GraphicsApp;
import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.util.Random;

public class RandomBouncingBalls extends GraphicsApp {

    /* private Constants */
    private static final int CANVAS_HEIGHT = 800;
    private static final int CANVAS_WIDTH = 800;
    private static final int FRAME_RATE = 60;
    private static final Color BACKGROUND_COLOR = Colors.WHITE;
    private static final float MIN_BALL_VELOCITY = 2;
    private static final float MAX_BALL_VELOCITY = 10;
    private static final int MIN_DIAMETER = 50;
    private static final int MAX_DIAMETER = 100;
    private static final int RGB_MAX = 255;

    // Private Attribute
    private Random rand;
    private Circle ball1;
    private Circle ball2;
    /*
        In den Arrays werden die x- und y-Geschwindigkeit der Bälle gespeichert, die Arrays funktionieren dabei analog
        zu Vektoren in der Physik und Mathematik, an der Stelle mit Index 0 findet sich die x-Geschwindigkeit,
        an der Stelle mit Index 1 die y-Geschwindigkeit
     */
    private float[] ball1Vector;
    private float[] ball2Vector;

    /**
     * Die Initialize Methode wird einmalig zum Start des Programms aufgerufen
     **/

    @Override
    public void initialize() {
        setupCanvas();
        rand = new Random();
        setupRandomBalls();
        setupSpeeds();
    }

    // Die setupRandomBalls-Methode initialisiert die beiden Bälle.
    private void setupRandomBalls() {
        ball1 = setupBall();
        ball2 = setupBall();
    }

    /*
        Die setupBall-Methode gibt ein neues Circle-Objekt zurück, dass mit zufälligem Radius, zufälliger x- und
        y-Position und zufälliger Farbe initialisiert wurde.
     */
    private Circle setupBall() {
        int randomRadius = getRandomNumber(MIN_DIAMETER/2, MAX_DIAMETER/2);
        int randomXPos = getRandomNumber(randomRadius, CANVAS_WIDTH - randomRadius);
        int randomYPos = getRandomNumber(randomRadius, CANVAS_HEIGHT - randomRadius);
        Color randomColor = getRandomColor();
        return new Circle(randomXPos, randomYPos, randomRadius, randomColor);
    }


    /*
        Die setupSpeeds-Methode initialisiert die beiden Geschwindigkeitsvektoren.
     */
    private void setupSpeeds() {
        ball1Vector = getRandomVector();
        ball2Vector = getRandomVector();
    }

    /*
        Die getRandomVector-Methode gibt ein neues float-Array mit zwei Einträgen zurück, die mit zufälligen Fließkomma-
        zahlen befüllt werden.
     */
    private float[] getRandomVector() {
        float[] vector = new float[2];
        vector[0] = getRandomFloat(MIN_BALL_VELOCITY, MAX_BALL_VELOCITY);
        vector[1] = getRandomFloat(MIN_BALL_VELOCITY, MAX_BALL_VELOCITY);
        return vector;
    }

    private void setupCanvas() {
        setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        setFrameRate(FRAME_RATE);
    }

    /*
        Die getRandomFloat-Methode gibt eine zufällige Fließkommazahl zwischen min und max zurück.
        Die nextFloat-Methode des Random-Objekts gibt eine Zahl zwischen 0 und 1 zurück. Multipliziert man diesen Wert
        mit einer Zahl x, so erhält man also einen Wert zwischen 0 und x.
        Um nun einen Minimalwert festzulegen zieht man von x den Minimalwert y ab, man erhält also erstmal eine Zahl zwischen
        0 und (x-y). Addiert man auf dieses Ergebnis y so erhält man demnach eine Zahl zwischen y und x.
     */
    private float getRandomFloat(float min, float max) {
        return rand.nextFloat() * (max - min) + min;
    }

    /*
        Die getRandomNumber-Methode gibt eine Ganzzahl zwischen min und max zurück.
        Die nextInt Methode des Random Objekts gibt eine Zahl zwischen 0 und dem übergebenen Wert zurück.
        Will man eine untere Grenze einführen so übergibt man den Maximalwert minus den Minimalwert und erhält damit
        eine Zahl zwischen 0 und max-min. Addiert man auf dieses Ergebnis nun den Minimalwert erhält man folglich
        eine Zahl zwischen 0+min = min und (max-min) + min = max also eine Zahl zwischen min und max.
     */
    private int getRandomNumber(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    /*
        Die getRandomColor-Methode erzeugt eine neue zufällige Farbe in dem für jeden Farbkanal eine Zufallszahl zwischen 1 und 255 erzeugt wird und die Farbkanäle dann gemischt werden.
     */
    private Color getRandomColor() {
        int r = getRandomNumber(0, RGB_MAX);
        int g = getRandomNumber(0, RGB_MAX);
        int b = getRandomNumber(0, RGB_MAX);

        return new Color(r, g, b);
    }

    /**
     * Die draw-Methode wird wiederholt aufgerufen solange das Programm läuft.
     **/
    @Override
    public void draw() {
        drawBackground(BACKGROUND_COLOR);
        drawBalls();
    }

    // Die drawBalls-Methode aktualisiert die beiden Bälle und zeichnet sie.
    private void drawBalls() {
        drawAndUpdateBall(ball1, ball1Vector);
        drawAndUpdateBall(ball2, ball2Vector);
    }

    /*
        Die drawAndUpdateBall-Methode aktualisiert die Position des übergebenen Balls entsprechend des übergebenen
        Vektors, passt dann wenn nötig den Vektor an und zeichnet dann den Ball.
     */
    private void drawAndUpdateBall(Circle ball, float[] vector) {
        update(ball, vector);
        checkWallCollision(ball, vector);
        ball.draw();
    }

    /*
        Die update-Methode aktualisiert die Position des übergebenen Balls entsprechend des übergebenen Vektors.
        Die move-Methode der Circle-Klasse erwartet eine Veränderung in x-Richtung (dx) und eine Veränderung in
        y-Richtung (dy). Diese sind im Vektor-Array an der Stelle 0 und 1 gespeichert.
     */
    private void update(Circle ball, float[] vector) {
        ball.move(vector[0], vector[1]);
    }

    /*
        Die checkWallCollision-Methode überprüft, ob der Ball die linke oder rechte Wand oder die untere oder obere
        Wand berührt. Falls ja werden die Vektoren und die Farbe verändert.
     */
    private void checkWallCollision(Circle ball, float[] vector) {
        if(checkXWallCollision(ball, vector) || checkYWallCollision(ball, vector)) {
            ball.setColor(getRandomColor());
        }
    }

    /*
        Die checkXWallCollision Methode überprüft, ob der Ball die linke oder rechte Wand berührt.
        Das ist dann der Fall, wenn die x-Position des Balls minus der Radius (also der linkeste Punkt des Balls) kleiner
        oder gleich 0 ist oder die x-Position des Balls plus der Radius (also der rechteste Punkt des Balls) größer
        oder gleich der Breite der Zeichenfläche ist.
        Falls eine diese Bedingungen zutrifft wird die x-Geschwindigkeit des Balls umgekehrt und true zurückgegeben,
        falls nicht wird false zurückgegeben.
        Es ist nicht notwendig, return false in einen else-Block zu schreiben, da wenn die Bedingung zutrifft die
        Ausführung der Methode direkt nach return true beendet wird.
     */
    private boolean checkXWallCollision(Circle ball, float[] vector) {
        float ballXPos = ball.getXPos();
        float ballRadius = ball.getRadius();

        if(ballXPos - ballRadius <= 0 || ballXPos + ballRadius >= CANVAS_WIDTH) {
            vector[0] *= -1;
            return true;
        }
        return false;
    }

    /*
        Die checkYWallCollision Methode überprüft, ob der Ball die obere oder untere Wand berührt.
        Das ist dann der Fall, wenn die y-Position des Balls minus der Radius (also der oberste Punkt des Balls) kleiner
        oder gleich 0 ist oder die x-Position des Balls plus der Radius (also der unterste Punkt des Balls) größer
        oder gleich der Breite der Zeichenfläche ist.
        Falls eine diese Bedingungen zutrifft wird die y-Geschwindigkeit des Balls umgekehrt und true zurückgegeben,
        falls nicht wird false zurückgegeben.
        Es ist nicht notwendig, return false in einen else-Block zu schreiben, da wenn die Bedingung zutrifft die
        Ausführung der Methode direkt nach return true beendet wird.
     */
    private boolean checkYWallCollision(Circle ball, float[] vector) {
        float ballYPos = ball.getYPos();
        float ballRadius = ball.getRadius();

        if(ballYPos - ballRadius <= 0 || ballYPos + ballRadius >= CANVAS_HEIGHT) {
            vector[1] *= -1;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
