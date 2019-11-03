package org.academiadecodigo.splicegirls.Jorema.Server;

public class QCard {


    private String message;

    public QCard(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
