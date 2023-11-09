package FluxoMaximo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorGrafoBipartido {

    public static void main(String[] args) {
        int numVertices = 60; // Altere o número de vértices conforme necessário

        List<List<Integer>> grafo = gerarGrafoAleatorioBipartido(numVertices);

        // Exibir o grafo gerado
        for (int i = 0; i < numVertices; i++) {
            System.out.print("Vértice " + i + ": ");
            for (int vizinho : grafo.get(i)) {
                System.out.print(vizinho + " ");
            }
            System.out.println();
        }


        String arquivo = "GrafoBipartido"+numVertices+".txt"; 
        int numArestas = numVertices*2;
        try {
            FileWriter fileWriter = new FileWriter(arquivo);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
          
            printWriter.println(numVertices+1+"  "+numArestas);
            int origem =0, destino=numVertices-1;
            printWriter.println(origem+"  "+destino);

           for(int i=0; i<numVertices; i++){
                for(int j=0; j<grafo.get(i).size(); j++){
                    printWriter.println("       "+i+"      "+grafo.get(i).get(j));
                }   
            } 
            printWriter.close();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        } 
    }

    public static List<List<Integer>> gerarGrafoAleatorioBipartido(int numVertices) {
        List<List<Integer>> grafo = new ArrayList<>();
        Random random = new Random();

        // Dividir os vértices em duas partições
        int metade = numVertices / 2;

        // Adicionar as listas de adjacência para as duas partições
        for (int i = 0; i < numVertices; i++) {
            grafo.add(new ArrayList<>());
        }

        // Conectar aleatoriamente os vértices de uma partição para a outra
        for (int i = 0; i < metade; i++) {
            for (int j = metade; j < numVertices; j++) {
                if (random.nextBoolean()) {
                    grafo.get(i).add(j);
                } else {
                    grafo.get(j).add(i);
                }
            }
        }

        return grafo;
    }
}
