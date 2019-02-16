package hr.java.vjezbe.model;

public interface RadioSondazna {

    void podesiVisinuPostaje(int visinaPostaje);

    int dohvatiVisinuPostaje();

    default void provjeriVisinu() {
        if (dohvatiVisinuPostaje() > 1000) {
            podesiVisinuPostaje(1000);
        }
    }

    default void povecajVisinu() {
        podesiVisinuPostaje(dohvatiVisinuPostaje() + 1);
        provjeriVisinu();
    }
}
