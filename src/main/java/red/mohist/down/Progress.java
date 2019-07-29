package red.mohist.down;

import red.mohist.i18n.Message;

import java.text.DecimalFormat;

public class Progress {

    private int barLen;

    private char showChar;

    private DecimalFormat formater = new DecimalFormat("#.##%");

    public Progress(int barLen, char showChar) {
        this.barLen = barLen;
        this.showChar = showChar;
    }

    public void show(String filename, int value) {
        if (value < 0 || value > 100) {
            return;
        }

        reset();

        float rate = (float) (value*1.0 / 100);
        draw(filename, barLen, rate);
        if (value == 100L) {
            afterComplete();
        }
    }

    private void draw(String filename, int barLen, float rate) {
        int len = (int) (rate * barLen);
        System.out.print(Message.getString("file.download.progress"));
        for (int i = 0; i < len; i++) {
            System.out.print(showChar);
        }
        for (int i = 0; i < barLen-len; i++) {
            System.out.print(" ");
        }
        System.out.print(" |" + format(rate));
    }

    private void reset() {
        System.out.print('\r');
    }

    private void afterComplete() {
        System.out.print('\n');
    }

    private String format(float num) {
        return formater.format(num);
    }
}
