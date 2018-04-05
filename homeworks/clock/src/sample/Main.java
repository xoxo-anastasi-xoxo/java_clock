package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Date;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage myStage) throws Exception {
        // Присвоить заголовок подмосткам
        myStage.setTitle("JavaFX");
        // Создать корневой узел и часы в нём
        StackPane root = new StackPane();
        Clock clock = new Clock();
        root.getChildren().add(clock.getContainer());

        // создать сцену
        Scene myScene = new Scene(root, 250, 250);

        // установить сцену на подмостках
        myStage.setScene(myScene);

        // показать подмостки и сцену на них
        myStage.show();
    }

    class Clock {
        private final Canvas container;
        private final GraphicsContext gc;
        private final Image clockImage;
        private final Color clockColor;
        private int secCount;
        private Thread timer;


        Clock() {
            // Инициализируем контейнер для рисования часов
            container = new Canvas(250, 250);
            gc = container.getGraphicsContext2D();

            // задаем необходимые начальные параметры
            URL imgURL = getClass().getResource("myphoto.jpg");
            clockImage = new Image(imgURL.toString());
            clockColor = Color.GREEN;
            secCount = 0;

            // запускаем таймер
            timer = new Thread(new TimerThread());
            timer.setDaemon(true);
            timer.start();

        }

        private void initClock() {
            // Получаем время
            Date now = new Date();
            long sec = now.getTime() / 1000 % 60;
            long min = now.getTime() / 60000 % 60;
            long h = now.getTime() / 3600000 % 12;

            // Меняем тип прорисовки часов
            if (sec == 0) secCount++;
            long angle = 360;
            if (secCount > 0)
                angle = secCount % 2 == 0 ? -sec * 6 : 360 - sec * 6;

            // Чистим контейнер
            gc.clearRect(0, 0, 300, 300);

            // Добавляем циферблат
            gc.drawImage(clockImage, 25, 25, 200, 200);

            gc.setFill(clockColor);
            gc.fillArc(25, 25, 200, 200, 90, angle, ArcType.ROUND);

            gc.setFill(Color.BLACK);
            gc.fillArc(121, 121, 8, 8, 90, 360, ArcType.ROUND);

            // Добавляем стрелки с нужным временем
            gc.setStroke(Color.BLACK);
            setTime(sec, min, h);

            // Добавляем деления для циферблата
            for (int i = 0; i < 6; ++i) {
                double y = -95 * Math.cos(2 * Math.PI * i / 12) + 122;
                double x = 95 * Math.sin(2 * Math.PI * i / 12) + 122;
                gc.fillArc(x, y, 3, 3, 90, 360, ArcType.ROUND);
            }
            for (int i = 6; i < 12; ++i) {
                double y = -95 * Math.cos(2 * Math.PI * i / 12) + 125;
                double x = 95 * Math.sin(2 * Math.PI * i / 12) + 125;
                gc.fillArc(x, y, 3, 3, 90, 360, ArcType.ROUND);
            }
        }

        private void setTime(long sec, long min, long h) {
            // Рассчитаем координаты конца секундной стрелки
            double y = -97 * Math.cos(2 * Math.PI * sec / 60) + 125;
            double x = 97 * Math.sin(2 * Math.PI * sec / 60) + 125;
            gc.setLineWidth(1);
            gc.strokeLine(x, y, 125, 125);

            // Рассчитаем координаты конца минутной стрелки
            y = -77 * Math.cos(2 * Math.PI * min / 60) + 125;
            x = 77 * Math.sin(2 * Math.PI * min / 60) + 125;
            gc.setLineWidth(2);
            gc.strokeLine(x, y, 125, 125);

            // Рассчитаем координаты конца часовой стрелки
            y = -57 * Math.cos(2 * Math.PI * h / 12) + 125;
            x = 57 * Math.sin(2 * Math.PI * h / 12) + 125;
            gc.setLineWidth(3);
            gc.strokeLine(x, y, 125, 125);
        }

        Canvas getContainer() {
            return container;
        }

        class TimerThread implements Runnable {
            public void run() {
                while (true) {
                    try {
                        initClock();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }
}
