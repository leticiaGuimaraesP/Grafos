package Busca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BuscaProfundidade {
    static int tempo = 0; //Inicializa o tempo global
    static int vertice = 0; //Vertice escolhido pelo usuario
    static Grafo grafo; //Representação do Grafo
    static PropriedadesVertice[] infoV; //Array com as informacoes relacionadas a busca por profundidade de cada vertice
    static ArrayList<String> arestasArvore = new ArrayList<String>(); //Array para armazenar as arestas de arvore
    static ArrayList<String> arestasVertice = new ArrayList<String>(); //Array para armazenar as arestas divergentes do vertice escolhido

    public static void main(String[] Args){  
        Scanner leia = new Scanner(System.in);
        grafo = new Grafo();  
        
        System.out.println("Digite o nome do arquivo:");
        String arq = leia.nextLine();
        //String arq = "graph-test-100.txt";
        Grafo.read(arq);

        System.out.print("Digite um vertice:");
        //String str = leia.nextLine();
        //vertice = Integer.parseInt(str);
        vertice = leia.nextInt();

        int n = grafo.origem.length; //armazena a quantidade de vertices
        ordernarDestino(n); //ordena de forma lexicográfica as arestas divergentes de cada vertice
        //int m = grafo.destino.length;
        
        //inicializa o vetor com as informacoes basicas da busca em profundidade
        infoV = new PropriedadesVertice[n];
        for(int i=0; i<n-1; i++){
            infoV[i] = new PropriedadesVertice();
        }
    
        //inicializa a busca em profundidade com o primeiro vertice
        int v=1; 
        while(v!=0){
            buscaEmProfundidade(v); //metodo recursivo 
            //verifica se existe algum vertice que nao foi explorado ainda, apos finalizar a recusrividade
            v = faltaVertice(infoV);
        }

        //Imprime todas as arestas de arvore do grafo
        System.out.println("ARESTAS DE ARVORE");
        for(int i=0; i<arestasArvore.size(); i++){
           System.out.println(arestasArvore.get(i));
        }

        //Imprime todas as arestada divergentes do vertice escolhido
        System.out.println("ARESTAS DIVERGENTES AO VERTICE " + vertice);
        for(int i=0; i<arestasVertice.size(); i++){
           System.out.println(arestasVertice.get(i));
        }
    }

    //Metodo para encontrar os vertice que ainda nao foram explorados, isso e, tempo de descoberta igual a 0
    public static int faltaVertice(PropriedadesVertice[] array){
        boolean achou = false;
        int vertice = 0;
        for(int i=1; i<array.length-1 && !achou; i++){
            //testa se o tempo de descoberta do vertice e 0
            if(array[i].getTempoDescoberta()==0){
                //Se encontrou, retorna o vertice e ablica a busca nele
                achou = true;
                vertice = i;
            }
        }
        return vertice;
    }

    //Ordena o vetor de destino, o que possibilita a ordem lexicográfica
    public static void ordernarDestino(int n){
        for(int i=1; i<n-1; i++){
            int inicio = grafo.origem[i];
            int fim =  grafo.origem[i+1];

            //A ordenacao nao é feita diretamente em todas as posicoes.
            //A ordencao e feita em partes, e feita em cada conjunto de arestas divergentes aos vertices
            int[] subArray = Arrays.copyOfRange(grafo.destino, inicio, fim);
            Arrays.sort(subArray); 
            
            for (int x = inicio, j = 0; x < fim; x++, j++) {
                //System.out.println(grafo.origem[i]+" "+subArray[j]);
                grafo.destino[x] = subArray[j];
            }
        }
        
    }

    public static void buscaEmProfundidade(int v){
        //System.out.println("Vertice: "+v);
        
        tempo++; //atualiza o tempo global
        infoV[v].setTempoDescoberta(tempo); //define o tempo de descoberta do vertice

        //define quais são as posições iniciais e finais do array de destino do vertice 
        int posInicial = grafo.origem[v];
        int posFinal = grafo.origem[v+1];

        //para todo vizinho do vertice
        for(int i=posInicial; i<posFinal; i++){
            int w = grafo.destino[i];
            
            if(infoV[w].getTempoDescoberta()==0){
                //aresta de arvore
                String aresta = "Aresta Arvore: ";
                aresta += String.valueOf(v) +" - "+ String.valueOf(w);
                arestasArvore.add(aresta);

                if(v==vertice){
                    arestasVertice.add(aresta);
                }

                infoV[w].setPai(v); //define o pai do vertice
                buscaEmProfundidade(w); //aplica a busca no vertice
            }else{
                if(infoV[w].gettempoTermino()==0){
                    //aresta de retorno                    
                    if(v==vertice){ //se o vertice analisado for o vertice escolhido anteriormente pelo usuário
                        String aresta = "Aresta de Retorno: ";
                        aresta += String.valueOf(v) +" - "+ String.valueOf(w);
                        arestasVertice.add(aresta); //armazena a aresta divergente
                    }
                }else if(infoV[v].getTempoDescoberta()<infoV[w].getTempoDescoberta()){
                    //aresta de avanço
                    if(v==vertice){ //se o vertice analisado for o vertice escolhido anteriormente pelo usuário
                        String aresta = "Aresta de Avanco: ";
                        aresta += String.valueOf(v) +" - "+ String.valueOf(w);
                        arestasVertice.add(aresta); //armazena a aresta divergente
                    }
                }else{
                    //aresta de cruzamento
                    if(v==vertice){ //se o vertice analisado for o vertice escolhido anteriormente pelo usuário
                        String aresta = "Aresta de Cruzamento: ";
                        aresta += String.valueOf(v) +" - "+ String.valueOf(w);
                        arestasVertice.add(aresta); //armazena a aresta divergente
                    }
                }
            }
        }
        tempo++;
        infoV[v].settempoTermino(tempo);   
    }
}

//Classe para armazenar as informações a respeito da busca em profundidade
class PropriedadesVertice{
    private int tempoDescoberta, tempoTermino, pai;

    PropriedadesVertice(){
        tempoDescoberta = 0;
        tempoTermino = 0;
        pai = 0;
    }

    public int getTempoDescoberta() {
        return tempoDescoberta;
    }
    public int gettempoTermino() {
        return tempoTermino;
    }
    public int getPai() {
        return pai;
    }
    public void setTempoDescoberta(int tempoDescoberta) {
        this.tempoDescoberta = tempoDescoberta;
    }
    public void settempoTermino(int tempoTermino) {
        this.tempoTermino = tempoTermino;
    }
    public void setPai(int pai) {
        this.pai = pai;
    }    
}
