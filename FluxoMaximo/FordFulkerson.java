/*
 * Ford-Fulkerson algorith in Java
 * Programiz: https://www.programiz.com/dsa/ford-fulkerson-algorithm
*/

package FluxoMaximo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

class FordFulkerson {
  static int V = 0;   //quantidade de vértices
  static int s=0, t=0; //origem e destino
  static int grafo[][]; //grafo formado

  // Using BFS as a searching algorithm
  boolean bfs(int Graph[][], int s, int t, int p[]) {
    boolean visited[] = new boolean[V];
    for (int i = 0; i < V; ++i)
      visited[i] = false;

    LinkedList<Integer> queue = new LinkedList<Integer>();
    queue.add(s);
    visited[s] = true;
    p[s] = -1;

    while (queue.size() != 0) {
      int u = queue.poll();

      for (int v = 0; v < V; v++) {
        if (visited[v] == false && Graph[u][v] > 0) {
          queue.add(v);
          p[v] = u;
          visited[v] = true;
        }
      }
    }    
    return (visited[t] == true);
  }

  // Applying fordfulkerson algorithm
  int fordFulkerson(int graph[][], int s, int t) {
    int u, v;
    int Graph[][] = new int[V][V];

    // cria-se uma cópia do grafo
    for (u = 0; u < V; u++)
      for (v = 0; v < V; v++)
        Graph[u][v] = graph[u][v];

    int p[] = new int[V]; // array que armazenará os pais de cada vértice no caminho aumentante

    int max_flow = 0; // armazena o fluxo máximo encontrado

    ArrayList<String> ways = new ArrayList<>();

    // Updating the residual calues of edges - Atualização das capacidades residuais
    while (bfs(Graph, s, t, p)) {
      int path_flow = Integer.MAX_VALUE;
      for (v = t; v != s; v = p[v]) {
        u = p[v];
        path_flow = Math.min(path_flow, Graph[u][v]);
      }

      String w = ""+t;
      for (v = t; v != s; v = p[v]) {
        u = p[v];
        Graph[u][v] -= path_flow;
        Graph[v][u] += path_flow;
        w += " - "+u;
      }
      ways.add(w);
      // Adding the path flows
      max_flow += path_flow;
    }
    
    System.out.println(s+" -> "+t);
    System.out.println("Quantidade de caminhos dijuntos: "+max_flow);
    System.out.println("Caminhos:");
    for(String x : ways){
      System.out.println(x);
    }
    return max_flow;
  }

  public static void read(String arquivo) {
    try {
      File arq = new File(arquivo);
      Scanner sc = new Scanner(arq);

      // Leitura da primeira linha do arquivo
      String data = sc.nextLine();
      String[] array = data.split("  ");

      // Definição do tamanho dos arrays
      int n = Integer.parseInt(array[0]);
      int m = Integer.parseInt(array[1]);
      V = n;
      grafo = new int[n][n];

      data = sc.nextLine();
      array = data.split("  ");

      // Definição do tamanho dos arrays
      s = Integer.parseInt(array[0]);
      t = Integer.parseInt(array[1]);
      

      // Leitura completa do arquivo
      while (sc.hasNextLine()) {
        data = sc.nextLine(); // Leitura por linha
        array = data.split("\\s+"); // Separa cada elemento da linha

        // Define qual é a aresta de origem e a aresta de destino
        int from, to;
        if (array.length == 2) {
          from = Integer.parseInt(array[0]);
          to = Integer.parseInt(array[1]);
        } else {
          from = Integer.parseInt(array[1]);
          to = Integer.parseInt(array[2]);
        }
        grafo[from][to] = 1;
      }

      /*Grafo
      for(int i=0; i<n; i++){
        for(int x=0; x<n; x++){
          System.out.print(grafo[i][x]+" ");
        }
        System.out.println();
      }*/

      sc.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws java.lang.Exception {
    FordFulkerson fm = new FordFulkerson();

    long startTime = System.currentTimeMillis();

    //read("GrafoCompleto60.txt");
    read("GrafoBipartido60.txt");
    fm.fordFulkerson(grafo, s, t); //s->origem | t->destino

    long endTime = System.currentTimeMillis();
    long executionTime = endTime - startTime;

    System.out.println("Tempo de execução: " + executionTime + " milissegundos");
  }
}