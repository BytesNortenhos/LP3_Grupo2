package controllers.admin;

public class ConversionController {
    public double kilometrosParaCentimetros(double kilometros) {
        return kilometros * 100000;
    }
    public double metrosParaCentimetros(double metros) {
        return metros * 100;
    }
    public double centimetrosParaMetros(double centimetros) {
        return centimetros / 100;
    }
    public double centimetrosParaKilometros(double centimetros) {
        return centimetros / 100000;
    }

    public double horasParaMilissegundos(double horas) {
        return horas * 60 * 60 * 1000;
    }
    public double minutosParaMilissegundos(double minutos) {
        return (int) (minutos * 60 * 1000);
    }
    public double segundosParaMilissegundos(double segundos) {
        return (long) (segundos * 1000);
    }
    public double milissegundosParaSegundos(long milissegundos) {
        return milissegundos / 1000.0;
    }
    public double milissegundosParaMinutos(int milissegundos) {
        return (milissegundos / (60.0 * 1000));
    }
    public double milissegundosParaHoras(long milissegundos) {
        return milissegundos / (60.0 * 60 * 1000);
    }
}
