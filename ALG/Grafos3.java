import java.util.ArrayList;
import java.util.Random;

public class Grafos3 {
        private static int funcionfila(int id, int longitud){
    return id / longitud;
}

        private static int funcioncolumna(int id, int longitud){
            return id % longitud;
        }

        private static int funcionid(int fila, int columna, int longitud){
            return fila * longitud + columna;
        }

        private static void dfsLista(ArrayList<Eje>[] lista, Nodo[] nodos, int id){
            nodos[id].setVisitado(true);
            nodos[id].setProfundidad(nodos[id].getProfundidad() + 1);
            for (int i = 0; i < lista[id].size(); i++){
                Eje eje = lista[id].get(i);
                int hijo = eje.getIdHijo();
                if (!nodos[hijo].isVisitado()){
                    eje.setProfundidad(nodos[id].getProfundidad());
                    lista[hijo].get(lista[hijo].indexOf(new Eje(id))).setProfundidad(nodos[id].getProfundidad());
                    nodos[hijo].setProfundidad(nodos[id].getProfundidad());
                    dfsLista(lista, nodos, hijo);
                }else if (nodos[hijo].isVisitado() && eje.getProfundidad() <= 0){
                    eje.setProfundidad(-50);
                    lista[hijo].get(lista[hijo].indexOf(new Eje(id))).setProfundidad(-50);
                }
            }
        }

        public static void main(String[] args) {
            final int FILAS = 5;
            final int COLUMNAS = 5;
            final long SEMILLA = 12;
            final double PROBABILIDAD = 0.5;

            //Creacion del generador de numeros random
            Random rd = new Random(SEMILLA);

            //Nodos
            Nodo[] nodos = new Nodo[FILAS*COLUMNAS];

            //Lista de adyacencia
            ArrayList<Eje>[] listaAdyacencia = new ArrayList[FILAS*COLUMNAS];
            for (int i = 0; i < FILAS*COLUMNAS; i++){
                listaAdyacencia[i] = new ArrayList<Eje>();
                nodos[i] = new Nodo(i);
            }

            //Generar laberinto
            for (int i = 0; i < FILAS; i++){
                for (int j = 0; j < COLUMNAS; j++){
                    double probabilidadGenerada = rd.nextDouble();
                    if (i > 0 && probabilidadGenerada < PROBABILIDAD){
                        listaAdyacencia[funcionid(i, j, COLUMNAS)].add(new Eje(funcionid(i - 1, j, COLUMNAS)));
                        listaAdyacencia[funcionid(i - 1, j, COLUMNAS)].add(new Eje(funcionid(i, j, COLUMNAS)));
                    }
                    if (j > 0 && probabilidadGenerada < PROBABILIDAD){
                        listaAdyacencia[funcionid(i, j, COLUMNAS)].add(new Eje(funcionid(i, j - 1, COLUMNAS)));
                        listaAdyacencia[funcionid(i, j - 1, COLUMNAS)].add(new Eje(funcionid(i, j, COLUMNAS)));
                    }
                }
            }

            for(int i = 0; i < nodos.length; i++){
                dfsLista(listaAdyacencia, nodos, i);
            }

            //Generar mapa de calor
            int[][] mapa = new int[FILAS*2+1][COLUMNAS*2+1];
            for (int i = 0; i < FILAS*2+1; i++){
                for (int j = 0; j < COLUMNAS*2+1; j++){
                    mapa[i][j] = -100;
                }
            }
            for (int i = 0; i < FILAS; i++){
                for (int j = 0; j < COLUMNAS; j++){
                    mapa[i*2 + 1][j*2 + 1] = nodos[funcionid(i, j, COLUMNAS)].getProfundidad();
                    if(i < FILAS - 1 && listaAdyacencia[funcionid(i, j, COLUMNAS)].contains(new Eje(funcionid(i + 1, j, COLUMNAS)))){
                        mapa[i*2 + 2][j*2 + 1] = listaAdyacencia[funcionid(i, j, COLUMNAS)].get(listaAdyacencia[funcionid(i, j, COLUMNAS)].indexOf(new Eje(funcionid(i + 1, j, COLUMNAS)))).getProfundidad();
                    }
                    if(j < COLUMNAS - 1 && listaAdyacencia[funcionid(i, j, COLUMNAS)].contains(new Eje(funcionid(i, j + 1, COLUMNAS)))){
                        mapa[i*2 + 1][j*2 + 2] = listaAdyacencia[funcionid(i, j, COLUMNAS)].get(listaAdyacencia[funcionid(i, j, COLUMNAS)].indexOf(new Eje(funcionid(i, j + 1, COLUMNAS)))).getProfundidad();
                    }
                }
            }

            //Imprimir mapa de calor
            for (int i = 0; i < FILAS*2+1; i++){
                for (int j = 0; j < COLUMNAS*2+1; j++){
                    if (mapa[i][j] == -100){
                        System.out.print("XX");
                    }else if (mapa[i][j] == -50){
                        System.out.print("cc");
                    }else {
                        int celda = mapa[i][j];
                        if (celda < 10){
                            System.out.print(" " + celda);
                        }else{
                            System.out.print(celda);
                        }
                    }
                }
                System.out.println();
            }
        }
}
