import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class PruebaAlgoritmoInsertionSort {

    private static Punto[] crearArray(int tam, long semilla, Punto pivote){
        Punto[] respuesta = new Punto[tam];
        Random rd = new Random(semilla);
        double rangeMin = 0.0;
        double rangeMax = (double) tam;

        for (int i = 0; i < respuesta.length; i++){
            respuesta[i] = new Punto(rangeMin + (rangeMax - rangeMin) * rd.nextDouble(), rangeMin + (rangeMax - rangeMin) * rd.nextDouble(), pivote);
        }

        return respuesta;
    }

    private static void ordenarArray(Punto[] arrayOrdenar){
        int i = 1;
        long inicio = System.currentTimeMillis();
        while(i < arrayOrdenar.length){
            Punto x = arrayOrdenar[i];
            int j = i - 1;
            while(j >= 0 && arrayOrdenar[j].getModulo() > x.getModulo()){
                arrayOrdenar[j + 1] = arrayOrdenar[j];
                j--;
            }
            arrayOrdenar[j+1] = x;
            i++;
        }
        long fin = System.currentTimeMillis();
        double tiempo = (double) ((fin - inicio)/1000);
        System.out.println("Tiempo total del algoritmo: " + tiempo + " segundos");
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.print("Escriba la coordenada x: ");
        double x = input.nextDouble();

        System.out.print("Escriba la coordenada y: ");
        double y = input.nextDouble();
        Punto pivote = new Punto(x, y);

        System.out.print("Indique el tamaño del array a ordenar: ");
        int tam = input.nextInt();

        System.out.print("Introduzca la semilla de generación de números: ");
        long semilla = input.nextLong();

        long inicio = System.currentTimeMillis();

        Punto[] arrayDesordenado = crearArray(tam, semilla, pivote);

        long fin = System.currentTimeMillis();
        double tiempo = (double) ((fin - inicio)/1000);

        System.out.println("Array desordenado: " + Arrays.toString(arrayDesordenado));
        System.out.println("Estadísticas: ******************");
        System.out.println("Tiempo de creación del array: " + tiempo + " segundos");
        ordenarArray(arrayDesordenado);
        System.out.println("********************************");
        System.out.println("Array ordenado: " + Arrays.toString(arrayDesordenado));


    }
}
