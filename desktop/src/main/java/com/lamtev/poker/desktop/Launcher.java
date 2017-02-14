package com.lamtev.poker.desktop;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    //TODO хотелось бы видеть документацию к коду, чтобы была возможность сравнить что должен делать метод, и что он делает (если писать её во время создания новых методов, то это почти не занимает времени)
    //TODO добавь правила игры, потому что сейчас у тебя получается узкоспециализированное приложение (только для тех, кто знает правила игры в определённый тип покера), и тому кто будет делать ревью в будущем помогут лучше разобраться

    //TODO можно задать пустое имя и можно задать слишком длинное имя (если задать слишком длинное, то играть становиться критически неудобно), по-момему такого допускать нельзя
    //TODO задать минимальный размер окна, для которого все будет хорошо видно (потому что, когда я могу сжатьв всё так, чтобы стало невозможно играть - это ненормально)
    //TODO если дать ботам имена(человеческие), то это будет выглядеть приятнее
    //TODO сделать кнопку "Старт" в настройках больше и отодвинуть от остального (пользователь не должен долго думать куда ему нажать для начала игры, это должно сразу бросаться в глаза)

    private StartMenu startMenu;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Poker");
        startMenu = new StartMenu();
        setScene(primaryStage);
        primaryStage.show();
    }

    private void setScene(Stage primaryStage) {
        startMenu.setToStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
