package org.academiadecodigo.splicegirls.Jorema.Server;

public class QCard {

<<<<<<< HEAD
    public String getMessage() {
        return "A Q Card";
=======
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
>>>>>>> 028ee617235f68c40b83d2bfb83a9eb103d3b319
    }
}
