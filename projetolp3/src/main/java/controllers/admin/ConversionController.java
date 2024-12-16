package controllers.admin;

public class ConversionController {
    public double metrosParaCentimetros(double metros) {
        return metros * 100;
    }
    public double centimetrosParaMetros(double centimetros) {
        return centimetros / 100;
    }
    public int minutosParaMilissegundos(double minutos) {
        return (int) (minutos * 60 * 1000);
    }
    public Double milissegundosParaMinutos(int milissegundos) {
        return (milissegundos / (60.0 * 1000));
    }
    public double milissegundosParaSegundos(long milissegundos) {
        return milissegundos / 1000.0;
    }
    public long segundosParaMilissegundos(double segundos) {
        return (long) (segundos * 1000);
    }
}
