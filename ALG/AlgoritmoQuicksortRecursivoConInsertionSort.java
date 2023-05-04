import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class AlgoritmoQuicksortRecursivoConInsertionSort {
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

    private static void ordenarArrayInsertion(Punto[] arrayOrdenar, int indiceInferior, int indiceSuperior){
        int i = indiceInferior + 1;
        while(i < indiceSuperior){
            Punto x = arrayOrdenar[i];
            int j = i - 1;
            while(j >= 0 && arrayOrdenar[j].getModulo() > x.getModulo()){
                arrayOrdenar[j + 1] = arrayOrdenar[j];
                j--;
            }
            arrayOrdenar[j+1] = x;
            i++;
        }
    }

    private static void ordenarArrayQuicksort(Punto[] arrayOrdenar, int indiceInferior, int indiceSuperior, int limite){

        if (indiceInferior >= indiceSuperior || indiceInferior < 0){
            return;
        }

        int pivot = particionar(arrayOrdenar, indiceInferior, indiceSuperior);

        if ((indiceSuperior - indiceInferior) <= limite){
            ordenarArrayInsertion(arrayOrdenar, indiceInferior, indiceSuperior);
        }else {
            ordenarArrayQuicksort(arrayOrdenar, indiceInferior, pivot - 1, limite);
            ordenarArrayQuicksort(arrayOrdenar, pivot + 1, indiceSuperior, limite);
        }
    }

    private static int particionar(Punto[] arrayOrdenar, int indiceInferior, int indiceSuperior){
        Punto pivote = arrayOrdenar[indiceSuperior];
        Punto auxiliar;

        int i = indiceInferior - 1;
        for (int j = indiceInferior; j < indiceSuperior; j++){
            if(arrayOrdenar[j].getModulo() <= pivote.getModulo()){
                i++;
                auxiliar = arrayOrdenar[i];
                arrayOrdenar[i] = arrayOrdenar[j];
                arrayOrdenar[j] = auxiliar;
            }
        }

        i++;
        auxiliar = arrayOrdenar[i];
        arrayOrdenar[i] = arrayOrdenar[indiceSuperior];
        arrayOrdenar[indiceSuperior] = auxiliar;
        return i;
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

        System.out.print("Introduzca el limite constante: ");
        int limite = input.nextInt();

        long inicio = System.currentTimeMillis();

        Punto[] arrayDesordenado = crearArray(tam, semilla, pivote);

        long fin = System.currentTimeMillis();
        long tiempo = (fin - inicio);

        //System.out.println("Array desordenado: " + Arrays.toString(arrayDesordenado));
        System.out.println("Estadísticas: ******************");
        System.out.println("Tiempo de creación del array: " + tiempo + " milisegundos");
        inicio = System.currentTimeMillis();
        ordenarArrayQuicksort(arrayDesordenado, 0, arrayDesordenado.length - 1, limite);
        fin = System.currentTimeMillis();
        tiempo = (fin - inicio);
        System.out.println("Tiempo total del algoritmo: " + tiempo + " milisegundos");
        System.out.println("********************************");
        //System.out.println("Array ordenado: " + Arrays.toString(arrayDesordenado));
    }
}