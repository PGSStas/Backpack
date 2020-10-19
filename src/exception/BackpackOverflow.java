package exception;

public class BackpackOverflow extends RuntimeException {
    public BackpackOverflow() {
        super("Can`t add anymore to backpack");
    }
}
