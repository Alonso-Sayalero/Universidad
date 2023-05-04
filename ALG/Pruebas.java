import java.util.ArrayList;

public class Pruebas {
    public static void funcion(int[] vector, int valor){
        if (valor == 4){
            return;
        }
        vector[1] = valor;
        valor = valor + 1;
        funcion(vector, valor);
    }
    public static void main(String[] args) {
        int[] vector = {0, 0, 0};
        funcion(vector, 1);

        for (int i = 0; i < vector.length; i++){
            System.out.print(vector[i]);
        }
        System.out.println();

        ArrayList<Eje> lista = new ArrayList<>();
        lista.add(new Eje(8));

        System.out.println(lista.contains(new Eje(8)));
    }
}
