package sample.Game;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends App {
    private WebView browser;
    private WebEngine webEngine;
    public Browser(Window window) {
        GridPane pane = new GridPane();
        getChildren().add(pane);

        browser = new WebView();
        webEngine = browser.getEngine();
        String url = "https://yandex.ru";
        browser.setPrefWidth(window.getPrefWidth());
        webEngine.load(url);
        pane.add(browser,0,1);

        TextField search = new TextField();
        search.setText(url);
        search.setPrefWidth(988);
        search.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                webEngine.load(search.getText());
            }
        });
        GridPane utils = new GridPane();
        utils.add(search,0,0);
        Button button = new Button("Apply URL");
        button.setOnMouseClicked(event -> {
            webEngine.load(search.getText());
        });
        utils.add(button,1,0);
        pane.add(utils,0,0);
    }

    @Override
    public void close() {
        super.close();
        webEngine.load("");
        browser = null;
        webEngine = null;
    }
}
