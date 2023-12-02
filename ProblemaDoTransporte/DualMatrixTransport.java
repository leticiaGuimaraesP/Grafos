package ProblemaDoTransporte;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//javac ProblemaDoTransporte/DualMatrixTransport.java
//java ProblemaDoTransporte.DualMatrixTransport

class DualMatrixTransport {
    static int costMatrix[][];
    static int relativeCostMatrix[][];
    static int matrix[][];
    static ArrayList<Integer> offer = new ArrayList<Integer>();
    static ArrayList<Integer> demand = new ArrayList<Integer>();

    public static void read(String arquivo) {
        try {
            File arq = new File(arquivo);
            Scanner sc = new Scanner(arq);

            // Leitura da primeira linha do arquivo
            String data = sc.nextLine();
            String[] array = data.split("\\s+");

            // Definição do tamanho dos arrays
            int m = Integer.parseInt(array[0]);
            int n = Integer.parseInt(array[1]);
            int tam = m + n;

            for (int i = 0; i < tam; i++) {
                data = sc.nextLine();
                // Testa se é oferta ou demanda
                if (i < m) {
                    offer.add(Integer.parseInt(data));
                } else {
                    demand.add(Integer.parseInt(data));
                }

            }

            // m -> linha (oferta)
            // n -> coluna (linha)
            costMatrix = new int[m][n];
            relativeCostMatrix = new int[m + 1][n + 1];
            matrix = new int[m][n];

            for (int i = 0; i < m; i++) {
                data = sc.nextLine();
                String[] aux = data.split("\\s+");
                for (int x = 0; x < aux.length; x++) {
                    costMatrix[i][x] = Integer.parseInt(aux[x]);
                    matrix[i][x] = 0;
                }
            }

            // Matriz
            for (int i = 0; i < m; i++) {
                for (int x = 0; x < n; x++) {
                    System.out.print(costMatrix[i][x] + " ");
                }
                System.out.println();
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void findDualMatrixTransport() {
        int totalM = 0;
        for (int i = 0; i < offer.size(); i++) { // valor total oferta
            totalM += offer.get(i);
        }

        int auxM = 0;
        int auxDemand = 0, auxOffer = 0;

        // Identifica os valores de cada opção
        int i = 0;
        while (auxM < totalM) {
            for (int j = 0; j < demand.size(); j++) {

                if (demand.get(j) < offer.get(i)) {
                    // System.out.println("Demanda "+demand.get(j)+" < Oferta "+offer.get(i)+" ->
                    // direita");
                    matrix[i][j] = demand.get(j);
                    auxM += demand.get(j);

                    auxOffer = offer.get(i) - demand.get(j);
                    demand.set(j, 0);
                    offer.set(i, auxOffer);
                } else {
                    // System.out.println("Demanda "+demand.get(j)+" > Oferta "+offer.get(i)+" ->
                    // descer");
                    matrix[i][j] = offer.get(i);
                    auxM += offer.get(i);

                    auxDemand = demand.get(j) - offer.get(i);
                    demand.set(j, auxDemand);
                    offer.set(i, 0);
                    break;
                }
            }
            i++;

        }

        // Matriz
        System.out.println("Fluxo Matriz");
        for (i = 0; i < offer.size(); i++) {
            for (int x = 0; x < demand.size(); x++) {
                System.out.print(matrix[i][x] + " ");
            }
            System.out.println();
        }

        boolean recalcular = true;
        ;
        do {
            recalcular = formRelativeCostMatrix();
        } while (recalcular);

    }

    public static boolean formRelativeCostMatrix() {
        // Inicializa matriz de Custos Relativos
        for (int i = 0; i <= offer.size(); i++) {
            for (int x = 0; x <= demand.size(); x++) {
                relativeCostMatrix[i][x] = 0;
            }
        }
        int u = 0, v = 0;

        // Forma a Matriz de Custo relativo
        // Demanda x Oferta - calcula os valores para a linha da demanda e a coluna da
        // oferta
        for (int i = offer.size() - 1; i >= 0; i--) {
            for (int j = demand.size() - 1; j >= 0; j--) {
                if (matrix[i][j] != 0) {
                    v = relativeCostMatrix[offer.size()][j]; // m -> oferta -> linha = 2
                    u = relativeCostMatrix[i][demand.size()]; // n -> demanda -> coluna = 3

                    if (v != 0) {
                        u = costMatrix[i][j] - v;
                        relativeCostMatrix[i][demand.size()] = u;
                    } else if (u != 0) {
                        v = costMatrix[i][j] - u;
                        relativeCostMatrix[offer.size()][j] = v;
                    } else {
                        u = costMatrix[i][j] - v;
                        relativeCostMatrix[i][demand.size()] = u;
                    }
                } else {
                    relativeCostMatrix[i][j] = -1;

                }
            }

            int cont = 0;
            for (int a = 0; a < offer.size(); a++) {
                for (int x = 0; x < demand.size(); x++) {
                    if (matrix[a][x] == 0) {
                        // matrix[i][x] = -1;
                    } else {
                        cont++;
                    }
                }
            }

            int solBasica = offer.size() + demand.size() - 1;
            System.out.println(cont + " " + solBasica);
            if (cont != solBasica) {
                if (matrix[offer.size() - 1][demand.size() - 1] != 0) {
                    if (matrix[offer.size() - 1][demand.size() - 2] == 0) {
                        // matrix[offer.size()-1][demand.size()-2]=0;
                        relativeCostMatrix[offer.size() - 1][demand.size() - 2] = 0;
                    } else {
                        relativeCostMatrix[offer.size() - 1][demand.size() - 2] = 0;
                    }
                }
            }
        }

        // Valores não basicos - calcula e procura por números negativos
        boolean negativo = false;
        int posI = 0, posJ = 0;
        for (int i = 0; i < offer.size(); i++) {
            for (int j = 0; j < demand.size(); j++) {
                if (relativeCostMatrix[i][j] != 0) { // 0 -> base da matriz
                    v = relativeCostMatrix[offer.size()][j];
                    u = relativeCostMatrix[i][demand.size()];

                    int value = costMatrix[i][j] - (u + v);
                    if (value < 0) {
                        negativo = true;
                        posI = i;
                        posJ = j;
                    }
                    relativeCostMatrix[i][j] = value;
                }
            }
        }

        System.out.println("Custo Relativo");
        for (int i = 0; i <= offer.size(); i++) {
            for (int x = 0; x <= demand.size(); x++) {
                System.out.print(relativeCostMatrix[i][x] + " ");
            }
            System.out.println();
        }

        if (negativo) {
            boolean acabou = false;
            int quadrados[][] = new int[4][2];
            int min = 0;

            quadrados[0][0] = posI;
            quadrados[0][1] = posJ;

            for (int i = 0; i < offer.size() && !acabou; i++) { // 0, 1
                if (matrix[i][posJ] != 0) {
                    quadrados[1][0] = i;
                    quadrados[1][1] = posJ;

                    for (int j = 0; j < demand.size(); j++) { // 1, 2
                        if (matrix[i][j] != 0) {

                            if (matrix[posI][j] != 0) {
                                acabou = true;

                                quadrados[2][0] = i;
                                quadrados[2][1] = j;

                                quadrados[3][0] = posI;
                                quadrados[3][1] = j;

                                if (matrix[i][posJ] < matrix[posI][j]) {
                                    min = matrix[i][posJ];
                                } else {
                                    min = matrix[posI][j];
                                }

                            }
                        }
                    }
                }
            }

            // for(int i=0; i<4; i++){
            // for(int x=0; x<2; x++){

            // System.out.print(quadrados[i][x]+" ");
            // }
            // System.out.println();
            // }
            // System.out.println(min);

            for (int i = 0; i < 4; i++) {
                int aux1, aux2;
                aux1 = quadrados[i][0];
                aux2 = quadrados[i][1];
                if (i % 2 == 0) {
                    matrix[aux1][aux2] = matrix[aux1][aux2] + min;
                } else {
                    matrix[aux1][aux2] = matrix[aux1][aux2] - min;
                }
            }

            // Matriz
            System.out.println("Fluxo Matriz");
            for (int i = 0; i < offer.size(); i++) {
                for (int x = 0; x < demand.size(); x++) {
                    System.out.print(matrix[i][x] + " ");
                }
                System.out.println();
            }

        } else {
            System.out.println("Acabou");
        }
        return negativo;
    }

    public static void calculateTotalValue() {
        int value = 0;
        for (int i = 0; i < offer.size(); i++) {
            for (int j = 0; j < demand.size(); j++) {
                value += costMatrix[i][j] * matrix[i][j];
            }
        }
        System.out.println(value);
    }

    public static void main(String[] args) throws java.lang.Exception {
        long startTime = System.currentTimeMillis();

        // read("teste1.txt");
        read("teste2.txt");
        findDualMatrixTransport();
        calculateTotalValue();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Tempo de execucao: " + executionTime + " milissegundos");
    }
}