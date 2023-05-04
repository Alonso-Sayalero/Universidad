import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class AlgoritmoQuicksortRecursivoEIterativo {
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

    private static void ordenarArray(Punto[] arrayOrdenar, int indiceInferior, int indiceSuperior){

        if (indiceInferior >= indiceSuperior || indiceInferior < 0){
            return;
        }

        int pivot = particionar(arrayOrdenar, indiceInferior, indiceSuperior);

        ordenarArray(arrayOrdenar, indiceInferior, pivot - 1);
        quicksortIterativo(arrayOrdenar, pivot + 1, indiceSuperior);
    }

    private static void quicksortIterativo(Punto[] arrayOrdenar, int indiceInferior, int indiceSuperior){
        if (indiceInferior >= indiceSuperior) return;
        int[] pila = new int[indiceSuperior - indiceInferior + 1];
        int indice = -1;

        pila[++indice] = indiceInferior;
        pila[++indice] = indiceSuperior;

        while(indice >= 0){
            indiceSuperior = pila[indice--];
            indiceInferior = pila[indice--];

            int pivote = particionar(arrayOrdenar, indiceInferior, indiceSuperior);

            if (pivote - 1 > indiceInferior){
                pila[++indice] = indiceInferior;
                pila[++indice] = pivote - 1;
            }

            if (pivote + 1 < indiceSuperior){
                pila[++indice] = pivote + 1;
                pila[++indice] = indiceSuperior;
            }
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

        long inicio = System.currentTimeMillis();

        Punto[] arrayDesordenado = crearArray(tam, semilla, pivote);

        long fin = System.currentTimeMillis();
        long tiempo = (fin - inicio);

        //System.out.println("Array desordenado: " + Arrays.toString(arrayDesordenado));
        System.out.println("Estadísticas: ******************");
        System.out.println("Tiempo de creación del array: " + tiempo + " milisegundos");
        inicio = System.currentTimeMillis();
        ordenarArray(arrayDesordenado, 0, arrayDesordenado.length - 1);
        fin = System.currentTimeMillis();
        tiempo = (fin - inicio);
        System.out.println("Tiempo total del algoritmo: " + tiempo + " milisegundos");
        System.out.println("********************************");
        //System.out.println("Array ordenado: " + Arrays.toString(arrayDesordenado));
    }
}
