package io.feaggle.driver;

public interface DrivenBy<Context> {
    String identifier();
    void drive(Context context);
}
