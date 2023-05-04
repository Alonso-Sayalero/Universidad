import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class Grafos1 {
    private static int funcionfila(int id, int longitud){
        return id / longitud;
    }

    private static int funcioncolumna(int id, int longitud){
        return id % longitud;
    }

    private static int funcionid(int fila, int columna, int longitud){
        return fila * longitud + columna;
    }

    private static void dfsMatriz(int[][] matriz, Nodo[] nodos, int id, int[] padres){
        nodos[id].setVisitado(true);
        for (int i = 0; i < matriz.length; i++){
            if (matriz[id][i] == 1 && !nodos[i].isVisitado()){
                padres[i] = id;
                dfsMatriz(matriz, nodos, i, padres);
            }
        }
    }

    private static void dfsLista(ArrayList<Integer>[] lista, Nodo[] nodos, int id, int[] padres){
        nodos[id].setVisitado(true);
        for (int i = 0; i < lista[id].size(); i++){
            int hijo = lista[id].get(i);
            if (!nodos[hijo].isVisitado()){
                padres[hijo] = id;
                dfsLista(lista, nodos, hijo, padres);
            }
        }
    }

    //Las dos funciones siguientes forman parte del segundo laboratorio
    private static void bfsMatriz(int[][] matriz, Nodo[] nodos, int id, int[] padres){
        nodos[id].setVisitado(true);
        ArrayDeque<Integer> cola = new ArrayDeque<>();
        cola.push(id);
        while(!cola.isEmpty()){
            int nodo = cola.pop();
            for (int i = 0; i < matriz.length; i++){
                if (matriz[nodo][i] == 1 && !nodos[i].isVisitado()){
                    padres[i] = nodo;
                    cola.push(i);
                    nodos[i].setVisitado(true);
                }
            }
        }
    }

    private static void bfsLista(ArrayList<Integer>[] lista, Nodo[] nodos, int id, int[] padres){
        nodos[id].setVisitado(true);
        ArrayDeque<Integer> cola = new ArrayDeque<>();
        cola.push(id);
        while(!cola.isEmpty()){
            int nodo = cola.pop();
            for (int i : lista[nodo]){
                if (lista[nodo].contains(i) && !nodos[i].isVisitado()){
                    padres[i] = nodo;
                    cola.push(i);
                    nodos[i].setVisitado(true);
                }
            }
        }
    }
    //*****************************************************************

    public static void main(String[] args) {
        final int FILAS = 100;
        final int COLUMNAS = 100;
        final long SEMILLA = 12;
        final double PROBABILIDAD = 0.1;

        //Creacion del generador de numeros random
        Random rd = new Random(SEMILLA);

        //Nodos
        Nodo[] nodos = new Nodo[FILAS*COLUMNAS];

        long inicio = System.currentTimeMillis();
        //Matriz de adyacencia
        int[][] matrizAdyacencia = new int[FILAS*COLUMNAS][FILAS*COLUMNAS];
        //Inicializacion de los nodos y la matriz de adyacencia
        for (int i = 0; i < FILAS*COLUMNAS; i++){
            nodos[i] = new Nodo(i);
            for (int j = 0; j < FILAS*COLUMNAS; j++){
                matrizAdyacencia[i][j] = 0;
            }
        }

        //Lista de adyacencia
        /*ArrayList<Integer>[] listaAdyacencia = new ArrayList[FILAS*COLUMNAS];
        for (int i = 0; i < FILAS*COLUMNAS; i++){
            nodos[i] = new Nodo(i);
            listaAdyacencia[i] = new ArrayList<Integer>();
        }*/

        //Generar laberinto
        for (int i = 0; i < FILAS; i++){
            for (int j = 0; j < COLUMNAS; j++){
                double probabilidadGenerada = rd.nextDouble();
                if (i > 0 && probabilidadGenerada < PROBABILIDAD){
                    matrizAdyacencia[funcionid(i, j, COLUMNAS)][funcionid(i - 1, j, COLUMNAS)] = 1;
                    matrizAdyacencia[funcionid(i - 1, j, COLUMNAS)][funcionid(i, j, COLUMNAS)] = 1;
                    //listaAdyacencia[funcionid(i, j, COLUMNAS)].add(funcionid(i - 1, j, COLUMNAS));
                    //listaAdyacencia[funcionid(i - 1, j, COLUMNAS)].add(funcionid(i, j, COLUMNAS));
                }
                if (j > 0 && probabilidadGenerada < PROBABILIDAD){
                    matrizAdyacencia[funcionid(i, j, COLUMNAS)][funcionid(i, j - 1, COLUMNAS)] = 1;
                    matrizAdyacencia[funcionid(i, j - 1, COLUMNAS)][funcionid(i, j, COLUMNAS)] = 1;
                    //listaAdyacencia[funcionid(i, j, COLUMNAS)].add(funcionid(i, j - 1, COLUMNAS));
                    //listaAdyacencia[funcionid(i, j - 1, COLUMNAS)].add(funcionid(i, j, COLUMNAS));
                }
            }
        }
        long fin = System.currentTimeMillis();
        long tiempo = (fin - inicio);
        System.out.println("Tiempo total de creacion: " + tiempo + " milisegundos");

        //Generar mapa de calor
        int[][] mapa = new int[FILAS*2+1][COLUMNAS*2+1];
        for (int i = 0; i < FILAS*2+1; i++){
            for (int j = 0; j < COLUMNAS*2+1; j++){
                mapa[i][j] = 0;
            }
        }
        for (int i = 0; i < FILAS; i++){
            for (int j = 0; j < COLUMNAS; j++){
                mapa[i*2 + 1][j*2 + 1] = 10;

                if(i < FILAS - 1 && matrizAdyacencia[funcionid(i, j, COLUMNAS)][funcionid(i + 1, j, COLUMNAS)] == 1){
                    mapa[i*2 + 2][j*2 + 1] = 11;
                }
                if(j < COLUMNAS - 1 && matrizAdyacencia[funcionid(i, j, COLUMNAS)][funcionid(i, j + 1, COLUMNAS)] == 1){
                    mapa[i*2 + 1][j*2 + 2] = 11;
                }/*
                if(i < FILAS - 1 && listaAdyacencia[funcionid(i, j, COLUMNAS)].contains(funcionid(i + 1, j, COLUMNAS))){
                    mapa[i*2 + 2][j*2 + 1] = 11;
                }
                if(j < COLUMNAS - 1 && listaAdyacencia[funcionid(i, j, COLUMNAS)].contains(funcionid(i, j + 1, COLUMNAS))){
                    mapa[i*2 + 1][j*2 + 2] = 11;
                }*/
            }
        }

        int[] padres = new int[FILAS*COLUMNAS];
        for (int i = 0; i < padres.length; i++){
            padres[i] = -1;
        }

        inicio = System.currentTimeMillis();
        dfsMatriz(matrizAdyacencia, nodos, 0, padres);
        //dfsLista(listaAdyacencia, nodos, 0, padres);
        //bfsMatriz(matrizAdyacencia, nodos, 0, padres);
        //bfsLista(listaAdyacencia, nodos, 0, padres);
        fin = System.currentTimeMillis();
        tiempo = (fin - inicio);
        System.out.println("Tiempo total del algoritmo: " + tiempo + " milisegundos");


        for (int i = 0; i < padres.length; i++){
            if (padres[i] != -1){
                if (funcionfila(i, COLUMNAS) > funcionfila(padres[i], COLUMNAS)){
                    mapa[funcionfila(i, COLUMNAS)*2][funcioncolumna(i, COLUMNAS)*2 + 1] = 12;
                }
                if (funcionfila(i, COLUMNAS) < funcionfila(padres[i], COLUMNAS)){
                    mapa[funcionfila(i, COLUMNAS)*2 + 2][funcioncolumna(i, COLUMNAS)*2 + 1] = 12;
                }
                if (funcioncolumna(i, COLUMNAS) > funcioncolumna(padres[i], COLUMNAS)){
                    mapa[funcionfila(i, COLUMNAS)*2 + 1][funcioncolumna(i, COLUMNAS)*2] = 12;
                }
                if (funcioncolumna(i, COLUMNAS) < funcioncolumna(padres[i], COLUMNAS)){
                    mapa[funcionfila(i, COLUMNAS)*2 + 1][funcioncolumna(i, COLUMNAS)*2 + 2] = 12;
                }
            }
        }

        //Imprimir mapa de calor
        for (int i = 0; i < FILAS*2+1; i++){
            for (int j = 0; j < COLUMNAS*2+1; j++){
                if (mapa[i][j] == 0){
                    System.out.print("XX");
                }else if (mapa[i][j] == 10){
                    int id = funcionid(i/2, j/2, COLUMNAS);
                    if (id < 10){
                        System.out.print(" " + id);
                    }else{
                        System.out.print(id);
                    }
                } else if (mapa[i][j] == 11) {
                    System.out.print("  ");
                } else if (mapa[i][j] == 12) {
                    System.out.print("rr");
                }
            }
            System.out.println();
        }
    }
}

class Nodo{
    private int id;
    private boolean visitado;
    private int profundidad;

    public Nodo (int id){
        setId(id);
        setVisitado(false);
        setProfundidad(0);
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public boolean isVisitado() {
        return visitado;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public int getProfundidad() {
        return profundidad;
    }
}
