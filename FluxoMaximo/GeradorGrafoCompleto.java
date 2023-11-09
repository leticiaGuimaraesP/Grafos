package FluxoMaximo;

//javac FluxoMaximo/GeradorGrafoCompleto.java
//java FluxoMaximo.GeradorGrafoCompleto

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GeradorGrafoCompleto {
    public static void main(String[] args) throws java.lang.Exception {
        int n = 10;
        String arq = "GrafoCompleto"+n+".txt";
        GrafoCompleto(n, arq);
    }

    //Gerador de Grafo Completo e Simétrico
    public static void GrafoCompleto(int qtdV, String arquivo){
        int qtdA = qtdV*(qtdV-1);
        try {
            FileWriter fileWriter = new FileWriter(arquivo);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
          
            int n = qtdV+1;
            printWriter.println(n+"  "+qtdA);
            printWriter.println(1+"  "+qtdV);

           for(int i=1; i<=qtdV; i++){
                for(int j=1; j<=qtdV; j++){
                    if(j!=i){
                        printWriter.println("       "+i+"      "+j);
                    }
                }   
            } 
            printWriter.close();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        } 
    }
}
