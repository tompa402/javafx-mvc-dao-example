package hr.java.vjezbe.javafx.model;

/**
 * Predstavlja entitet Radio sondažne mjerne postaje koji je definiran visinom mjerne postaje
 *
 * @author Tomislav
 */
public class RadioSondaznaMjernaPostaja extends MjernaPostaja implements RadioSondazna {

    private Integer visinaPostaje;

    public RadioSondaznaMjernaPostaja() {
        this(0, null, null, null, null);
    }

    /**
     * Inicijalizira podatak o identifikatoru, visini postaje, nazivu, mjestu, koordinatama te pripradajućim senzorima
     *
     * @param id              podatak o identifikatoru
     * @param naziv           podatak o nazivu
     * @param mjesto          podatak o mjestu
     * @param geografskaTocka podatak o koordinatama
     * @param visinaPostaje   podatak o visini postaje
     */
    public RadioSondaznaMjernaPostaja(Integer id, String naziv, Mjesto mjesto, GeografskaTocka geografskaTocka, Integer visinaPostaje) {
        super(id, naziv, mjesto, geografskaTocka);
        this.visinaPostaje = visinaPostaje;
    }

    @Override
    public void podesiVisinuPostaje(int visinaPostaje) {
        this.visinaPostaje = visinaPostaje;
    }

    @Override
    public int dohvatiVisinuPostaje() {
        return visinaPostaje;
    }
}
