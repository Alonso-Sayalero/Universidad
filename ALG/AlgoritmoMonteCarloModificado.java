import org.tc33.jheatchart.HeatChart;

import java.io.File;
import java.util.Random;

public class AlgoritmoMonteCarloModificado {
    private static final int ROTACIONES = 11000;
    private static final int FILAS = 2000;
    private static final int COLUMNAS = 2000;
    private static final double LIMITE_SUPERIOR_FILAS = 8;
    private static final double LIMITE_INFERIOR_FILAS = -4;
    private static final double LIMITE_SUPERIOR_COLUMNAS = 8;
    private static final double LIMITE_INFERIOR_COLUMNAS = -4;
    private static final int NUMERO_PUNTOS = 20;
    private static long SEMILLA = 700;

    private static double calcularDistancia(double a, double b){
        return b - a;
    }

    private static double valorFuncion(double x, double y){
        return 2*(-Math.sqrt(x*x+y*y)+(Math.cos(y)+Math.sin(x))*Math.sin(y+x)) + 15*(Math.sqrt((x+1)*(x+1)+y*y)-1)/((Math.sqrt((x+1)*(x+1)+y*y)-1)*(Math.sqrt(x*x+y*y)-1)+1);
    }

    private static double[][] crearMatriz(int filas, int columnas){
        double [][] matriz = new double[filas][columnas];
        for (int i = 0; i < filas; i++){
            for (int j = 0; j < columnas; j++){
                matriz[i][j] = -1000000;
            }
        }
        return matriz;
    }

    private static String[][] crearMatrizEscritura(int filas, int columnas){
        String[][] matriz = new String[filas][columnas];

        for (int i = 0; i < filas; i++){
            for (int j = 0; j < columnas; j++){
                matriz[i][j] = " ";
            }
        }
        return matriz;
    }

    private static void printMatriz(double[][] matriz){
        for (int i = 0; i < FILAS; i++){
            System.out.print("|");
            for (int j = 0; j < COLUMNAS; j++){
                System.out.print(matriz[i][j] + "|");
            }
            System.out.println();
        }
    }

    private static void printMatrizResultados(String[][] matriz){
        for (int i = 0; i < FILAS; i++){
            System.out.print("|");
            for (int j = 0; j < COLUMNAS; j++){
                System.out.print(matriz[i][j] + "|");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();
        double[][] matriz = crearMatriz(FILAS, COLUMNAS);
        long fin = System.currentTimeMillis();
        long tiempo = (fin - inicio);
        System.out.println("Tiempo de creación de la matriz: " + tiempo + " milisegundos");
        String[][] matrizCaminos = crearMatrizEscritura(FILAS, COLUMNAS);

        double maximoTotal = -1000000;
        double maximoTotalRotacion;
        int contador = ROTACIONES;
        long semilla = SEMILLA;
        double distanciaColumnas = calcularDistancia(LIMITE_INFERIOR_COLUMNAS, LIMITE_SUPERIOR_COLUMNAS)/(COLUMNAS - 1);
        double distanciaFilas = calcularDistancia(LIMITE_INFERIOR_FILAS, LIMITE_SUPERIOR_FILAS)/(FILAS - 1);
        double valorFilas = LIMITE_INFERIOR_FILAS;
        double valorColumnas = LIMITE_INFERIOR_COLUMNAS;

        inicio = System.currentTimeMillis();
        while (contador > 0){
            Random rd = new Random(semilla);
            maximoTotalRotacion = -1000000;
            for (int i = 0; i < NUMERO_PUNTOS; i++){
                int coordenadax = rd.nextInt(FILAS);
                int coordenaday = rd.nextInt(COLUMNAS);
                boolean terminado = false;
                double maximo = matriz[coordenadax][coordenaday];
                while(!terminado){
                    /*
                     * 0 = Fin
                     * 1 = Arriba
                     * 2 = Abajo
                     * 3 = Izquierda
                     * 4 = Derecha
                     */
                    int direccion = 0;
                    if (matrizCaminos[coordenadax][coordenaday].equals(" ")){
                        matrizCaminos[coordenadax][coordenaday] = "X";
                    }else{
                        break;
                    }

                    if (coordenadax != 0 && matriz[coordenadax - 1][coordenaday] == -1000000){
                        matriz[coordenadax - 1][coordenaday] = valorFuncion(valorFilas + (coordenadax - 1)*distanciaFilas, valorColumnas + coordenaday*distanciaColumnas);
                    }
                    if (coordenadax != FILAS - 1 && matriz[coordenadax + 1][coordenaday] == -1000000){
                        matriz[coordenadax + 1][coordenaday] = valorFuncion(valorFilas + (coordenadax + 1)*distanciaFilas, valorColumnas + coordenaday*distanciaColumnas);
                    }
                    if (coordenaday != 0 && matriz[coordenadax][coordenaday - 1] == -1000000){
                        matriz[coordenadax][coordenaday - 1] = valorFuncion(valorFilas + coordenadax*distanciaFilas, valorColumnas + (coordenaday - 1)*distanciaColumnas);
                    }
                    if (coordenaday != COLUMNAS - 1 && matriz[coordenadax][coordenaday + 1] == -1000000){
                        matriz[coordenadax][coordenaday + 1] = valorFuncion(valorFilas + (coordenadax - 1)*distanciaFilas, valorColumnas + (coordenaday + 1)*distanciaColumnas);
                    }

                    if (coordenadax != 0 && matriz[coordenadax - 1][coordenaday] > maximo){
                        maximo = matriz[coordenadax - 1][coordenaday];
                        direccion = 1;
                    }
                    if (coordenadax != FILAS - 1 && matriz[coordenadax + 1][coordenaday] > maximo){
                        maximo = matriz[coordenadax + 1][coordenaday];
                        direccion = 2;
                    }
                    if (coordenaday != 0 && matriz[coordenadax][coordenaday - 1] > maximo){
                        maximo = matriz[coordenadax][coordenaday - 1];
                        direccion = 3;
                    }
                    if (coordenaday != COLUMNAS - 1 && matriz[coordenadax][coordenaday + 1] > maximo){
                        maximo = matriz[coordenadax][coordenaday + 1];
                        direccion = 4;
                    }

                    switch (direccion){
                        case 0:
                            terminado = true;
                            matrizCaminos[coordenadax][coordenaday] = "O";
                            break;
                        case 1:
                            coordenadax = coordenadax - 1;
                            break;
                        case 2:
                            coordenadax = coordenadax + 1;
                            break;
                        case 3:
                            coordenaday = coordenaday - 1;
                            break;
                        case 4:
                            coordenaday = coordenaday + 1;
                            break;
                    }
                }

                if (maximo > maximoTotal){
                    maximoTotal = maximo;
                }
            }
            semilla = semilla + 1;
            contador--;
        }
        fin = System.currentTimeMillis();
        tiempo = (fin - inicio);
        System.out.println("Tiempo de busqueda: " + tiempo + " milisegundos");

        /*
        HeatChart matrizBase = new HeatChart(matriz);
        try{
            matrizBase.saveToFile(new File("imagen-prueba.png"));
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //printMatriz(matriz);
        //System.out.println();
        //printMatrizResultados(matrizCaminos);
        //System.out.println();
        System.out.println("Maximo final: " + maximoTotal);
    }
}
