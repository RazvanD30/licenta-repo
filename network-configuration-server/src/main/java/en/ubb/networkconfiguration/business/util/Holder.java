package en.ubb.networkconfiguration.business.util;

public class Holder<T> {
    private T held = null;

    public Holder() {
    }

    public Holder(T it) {
        held = it;
    }

    public void hold(T it) {
        held = it;
    }

    public T held() {
        return held;
    }

    public boolean isEmpty() {
        return held == null;
    }

    @Override
    public String toString() {
        return String.valueOf(held);
    }

}