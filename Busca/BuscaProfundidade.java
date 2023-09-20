package Busca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BuscaProfundidade {
    static int tempo = 0;
    static int vertice = 5;
    static Grafo grafo;
    static PropriedadesVertice[] infoV;
    static ArrayList<String> arestasArvore = new ArrayList<String>();
    static ArrayList<String> arestasVertice = new ArrayList<String>();

    public static void main(String[] Args){  
        Scanner leia = new Scanner(System.in);
        grafo = new Grafo();  
        
        System.out.println("Digite o nome do arquivo:");
        String arq = leia.nextLine();
        //String arq = "graph-test-100.txt";
        Grafo.read(arq);

        System.out.print("Digite um vertice:");
        String str = leia.nextLine();
        vertice = Integer.parseInt(str);

        int n = grafo.origem.length;
        ordernarDestino(n);
        //int m = grafo.destino.length;
        
        infoV = new PropriedadesVertice[n];
        for(int i=0; i<n-1; i++){
            infoV[i] = new PropriedadesVertice();
        }
    
        int v=1;
        while(v!=0){
            buscaEmProfundidade(v);
            v = faltaVertice(infoV);
        }

        for(int i=0; i<arestasVertice.size(); i++){
           System.out.println(arestasVertice.get(i));
        }
    }

    public static int faltaVertice(PropriedadesVertice[] array){
        boolean achou = false;
        int vertice = 0;
        for(int i=1; i<array.length-1 && !achou; i++){
            if(array[i].tempoDescoberta==0){
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

            int[] subArray = Arrays.copyOfRange(grafo.destino, inicio, fim);
            Arrays.sort(subArray); 
            
            for (int x = inicio, j = 0; x < fim; x++, j++) {
                //System.out.println(grafo.origem[i]+" "+subArray[j]);
                grafo.destino[x] = subArray[j];
            }
        }
        
    }

    public static void buscaEmProfundidade(int v){
        System.out.println("Vertice: "+v);
        
        tempo++;
        infoV[v].tempoDescoberta = tempo;

        int posInicial = grafo.origem[v];
        int posFinal = grafo.origem[v+1];

        
        for(int i=posInicial; i<posFinal; i++){
            int w = grafo.destino[i];
            //ARESTA ARVORE
            if(infoV[w].tempoDescoberta==0){
                //System.out.println("Arvore: "+v+" "+w);
                String aresta = "Aresta Arvore: ";
                aresta += String.valueOf(v) +" - "+ String.valueOf(w);
                arestasArvore.add(aresta);

                if(v==vertice){
                    arestasVertice.add(aresta);
                }

                //aresta de arvore
                infoV[w].pai = v;
                buscaEmProfundidade(w);
            }else{
                //ARESTA DE RETORNO
                if(infoV[w].tempoTermino==0){
                    //aresta de retorno                    
                    if(v==vertice){
                        String aresta = "Aresta de Retorno: ";
                        aresta += String.valueOf(v) +" - "+ String.valueOf(w);
                        arestasVertice.add(aresta);
                    }
                }else if(infoV[v].tempoDescoberta<infoV[w].tempoDescoberta){
                    //aresta de avanço
                    if(v==vertice){
                        String aresta = "Aresta de Avanco: ";
                        aresta += String.valueOf(v) +" - "+ String.valueOf(w);
                        arestasVertice.add(aresta);
                    }
                }else{
                    //aresta de cruzamento
                    if(v==vertice){
                        String aresta = "Aresta de Cruzamento: ";
                        aresta += String.valueOf(v) +" - "+ String.valueOf(w);
                        arestasVertice.add(aresta);
                    }
                }
            }
        }
        tempo++;
        infoV[v].tempoTermino = tempo;   
    }
}

class PropriedadesVertice{
    int tempoDescoberta, tempoTermino, pai;

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
