package sample.Game;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StatusBarGenerator {
    static Font raw;

    static {
        try {
            raw = Font.loadFont(new FileInputStream(new File("assets/cmdFont.ttf")),17);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Text[] generate(double progress) {
        Text pre = new Text("Processing... [ "+ ((int) (progress * 100))+ "% ] ");
        Text branchPre = new Text("[");
        Text in = new Text("                                                  ");
        for (int i = 0; i < in.getText().length()*progress; i++) {
            in.setText(in.getText().replaceFirst(" ","#"));
        }
        Text branchPost = new Text("]");

        Text[] res = new Text[] {pre,branchPre,in,branchPost};
        for (int i = 0; i < res.length; i++) {
            res[i].setFont(raw);
            res[i].setOpacity(0.8);
            res[i].setFill(Color.WHITE);
        }
        in.setFill(Color.color( 0, 0.8, 0.2));
        return res;
    }
}
