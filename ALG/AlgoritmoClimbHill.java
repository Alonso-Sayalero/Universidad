import org.tc33.jheatchart.HeatChart;

import java.io.File;

public class AlgoritmoClimbHill {
    private static final double LIMITE_SUPERIOR_FILAS = 8;
    private static final double LIMITE_INFERIOR_FILAS = -4;
    private static final double LIMITE_SUPERIOR_COLUMNAS = 8;
    private static final double LIMITE_INFERIOR_COLUMNAS = -4;

    private static double calcularDistancia(double a, double b){
        return b - a;
    }

    private static double valorFuncion(double x, double y){
        return 2*(-Math.sqrt(x*x+y*y)+(Math.cos(y)+Math.sin(x))*Math.sin(y+x)) + 15*(Math.sqrt((x+1)*(x+1)+y*y)-1)/((Math.sqrt((x+1)*(x+1)+y*y)-1)*(Math.sqrt(x*x+y*y)-1)+1);
    }

    private static double[][] crearMatriz(int filas, int columnas){
        double [][] matriz = new double[filas][columnas];
        double distanciaColumnas = calcularDistancia(LIMITE_INFERIOR_COLUMNAS, LIMITE_SUPERIOR_COLUMNAS)/(columnas - 1);
        double distanciaFilas = calcularDistancia(LIMITE_INFERIOR_FILAS, LIMITE_SUPERIOR_FILAS)/(filas - 1);
        double valorFilas = LIMITE_INFERIOR_FILAS;
        //System.out.println(distanciaColumnas);
        //System.out.println(distanciaFilas);

        for (int i = 0; i < filas; i++){
            double valorColumnas = LIMITE_INFERIOR_COLUMNAS;
            for (int j = 0; j < columnas; j++){
                //System.out.println(valorColumnas);
                matriz[i][j] = valorFuncion(valorFilas, valorColumnas);
                valorColumnas = valorColumnas + distanciaColumnas;
            }
            valorFilas = valorFilas + distanciaFilas;
        }
        return matriz;
    }

    public static void main(String[] args) {
        double[][] datos = crearMatriz(2000, 2000);

        double maximo = -10000000;
        for (int i = 0; i < 2000; i++){
            for (int j = 0; j < 2000; j++){
                if (datos[i][j] > maximo){
                    maximo = datos[i][j];
                }
            }
        }
        System.out.println(maximo);
        /*
        HeatChart matrizBase = new HeatChart(datos);
        try{
            matrizBase.saveToFile(new File("imagen-prueba.png"));
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
