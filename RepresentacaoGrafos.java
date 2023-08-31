import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RepresentacaoGrafos{
    /*Cada posição representa um vertice de origem e nela armazena-se a posição inicial, 
    referente ao array de destino, em que estão localizadas as arestas de destinos (os sucessores)*/
    private static int[] origem; //array que armazena ponteiros para o array destino
    private static int[] destino; //array que armazena os sucessores de cada vertice do grafo

    public static void read(String arquivo){
        try {
            File arq = new File(arquivo);
            Scanner sc = new Scanner(arq);

            //Leitura da primeira linha do arquivo
            String data = sc.nextLine();
            String[] array = data.split("  ");

            //Definição do tamanho dos arrays
            int n = Integer.parseInt(array[0]);
            int m = Integer.parseInt(array[1]);
            //System.out.println("n: "+ n + " m: " +m);
            origem = new int[n+2]; 
            destino = new int[m+1];

            //Inicializando o array de origem - a partir da posição 1
            origem[0] = 0;
            origem[1] = 1;
            origem[n+1] = m+1;

            int contOrigem = 1, contDestino = 1; //Contadores manipulação de cada array

            //Leitura completa do arquivo
            while (sc.hasNextLine()) {
                data = sc.nextLine(); //Leitura por linha
                array = data.split("\\s+"); //Separa cada elemento da linha
                
                //Define qual é a aresta de origem e a aresta de destino
                int arestaO, arestaD;
                if(array.length==2){
                    arestaO = Integer.parseInt(array[0]);
                    arestaD = Integer.parseInt(array[1]);
                }else{
                    arestaO = Integer.parseInt(array[1]);
                    arestaD = Integer.parseInt(array[2]);
                }
                
                if(arestaO==contOrigem){
                    destino[contDestino] = arestaD; //Armazena a aresta de destino lida no arquivo
                }else{
                    contOrigem++; //Atualiza o contador quando trocar de aresta de origem no arquivo
                    origem[contOrigem] = contDestino; //Armazena a posição (referente ao array de destino) da nova aresta de origem
                    destino[contDestino] = arestaD; //Armazena a aresta de destino lida no arquivo
                }
                contDestino++; //Passa para a proxima posição no array de destino
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void imprimir(){
        System.out.println("Origem(n)          Destino(m)");
        for(int i=1; i<origem.length-1; i++){
            for(int x=origem[i]; x<origem[i+1]; x++){
                System.out.println(i +" -> posicao: "+ origem[i]+"     "+destino[x]);
            }
        }
    }

    public static int maiorGrauSaida(){
        int maxOrigem=1; //posição do maior grau de saida
        int subMaxOrigem=origem[2]-origem[1];

        for(int i=2; i<origem.length-1; i++){
            int sub = origem[i+1]-origem[i];
            if(subMaxOrigem<sub){
                maxOrigem=i;
                subMaxOrigem = sub;
            }
        }
        return maxOrigem;
    }

    public static int maiorGrauEntrada(){
       int pos=1;
       int maxGrauEntrada = grauEntrada(pos);

       for(int i=2; i<origem.length-1; i++){
            int grauDeEntrada = grauEntrada(i);
            if(maxGrauEntrada<grauDeEntrada){
                pos=i;
                maxGrauEntrada=grauDeEntrada;
            }
       }
       return pos;
    }

    public static int grauSaida(int pos){
        int grau=0;
        grau = origem[pos+1]-origem[pos];
        return grau;
    }

    public static int grauEntrada(int vertice){
        int grau=0;
        for(int i=1; i<destino.length; i++){
            if(destino[i]==vertice){
                grau++;
            }
        }
        return grau;
    }

    public static int[] encontrarPredecessores(int vertice, int grau){
        int[] predecessores = new int[grau]; //array para armazenar os predecessores
        int cont=0; //contador para manipular array
        for(int i=1; i<destino.length; i++){ //loop para percorrer os vertices de destino
            if(destino[i]==vertice){ //porcura onde ha ocorrencia do vertice procurado (>grau de entrada)
                for(int x=1; x<origem.length-1; x++){ //loop para percorrer o array de origem
                    if(origem[x]<=i && origem[x+1]>i){ //realiza-se os testes para encontrar o vertice predecessor
                        predecessores[cont]=x;
                        cont++;
                        x=origem.length-1;
                    }
                }                
            }
        }
        return predecessores;
    }

    public static void main(String[] args){
        read("graph-test-100.txt");
        //imprimir();

        //(i) grau de saída e conjunto de sucessores para o vértice de maior valor de grau de saída
        int maxGrau = maiorGrauSaida();
        int grauDeSaida = grauSaida(maxGrau);

        System.out.println("Vertice com maior grau de saida: "+maxGrau);
        System.out.println("Grau de saida: "+ grauDeSaida);
        System.out.println("Conjunto de sucessores: ");
        for(int i=origem[maxGrau]; i<origem[maxGrau+1]; i++){
            System.out.print(destino[i]+" ");
        }
        System.out.println("\n");
        
        //(ii) grau de entrada e conjunto de predecessores para o vértice de maior valor de grau de entrada
        maxGrau = maiorGrauEntrada();
        int grauDeEntrada = grauEntrada(maxGrau);

        System.out.println("Vertice com maior grau de entrada: "+maxGrau);
        System.out.println("Grau de entrada: "+ grauDeEntrada);
        System.out.println("Conjunto de predecessores: ");
        int[] predecessores = encontrarPredecessores(maxGrau, grauDeEntrada);
        for(int i=0; i<predecessores.length; i++){
            System.out.print(predecessores[i]+" ");
        }
    }
}