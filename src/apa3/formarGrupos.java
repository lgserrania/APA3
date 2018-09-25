/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apa3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;




public class formarGrupos {
    
    private MatrizAdjacencia grafo; //Matriz de adjacência onde ficarão representadas as relações entre as pessoas
    private Scanner in;
    private int numeroPessoas; //Número total de pessoas
    private int numeroGrupos; //Número de grupos
    
    //Os grupos serão representados em um vetor de arraylist de inteiros
    //Fiz essa escolha devido que o número de grupos e membros por grupo é fixo
    private ArrayList<Integer>[] grupos; 
    private ArrayList<Integer> ordemInicial;
    
    private int[] tamanhoGrupos; //Tamanho dos grupos
    private double[] somaGrupos; //Soma dos pontos de relação em cada grupo
    private int[] grupoAlocado; //Esse vetor irá guardar em que grupo cada pessoa está alocada
    private int somaTotal = 0; //Soma total de todos os grupos
    private int maiorSoma = 0;
    private int semMudanca = 0;
    
    private int arranjo = 0;
    private int ordem[];
    private int contArranjos = 0;
    
    private String msgFinal = "";
    private int[] vetorSomas = new int[1000];
    
    private HSSFWorkbook workbook = new HSSFWorkbook();
    private HSSFSheet firstSheet = workbook.createSheet("Aba1");
    
    public formarGrupos(){
        
        try {
            carregaGrafo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(formarGrupos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        forcaBruta();
        
    }
    
    private void forcaBruta(){
        
        long inicio = System.currentTimeMillis();

        /*efetuaSomas();
        if(this.somaTotal > this.maiorSoma){
            this.maiorSoma = this.somaTotal;
            this.msgSomas();
        }*/
        this.ordem = new int[this.numeroPessoas];

        int []vet = new int[this.numeroPessoas];
        
        for(int i = 0; i < this.numeroPessoas; i++){
            vet[i] = i;
        }
        
        this.permuta(vet, 0);
        
        this.escreveSomas();

        System.out.println("Tempo do algoritmo: " + (System.currentTimeMillis() - inicio));
    }
    
    private void permuta(int []vet, int n){
        if(n == vet.length){
            arranjo++;
            this.testaGrupo();
        }else{
            for(int i = 0; i < vet.length; i++){
                boolean achou = false;
                for(int j = 0; j < n; j++){
                    if(this.ordem[j] == vet[i]) achou = true;
                }
                if(!achou){
                    this.ordem[n] = vet[i];
                    permuta(vet, n+1);
                }
            }
        }       
    }
    
    private void imprimeVet(){
        System.out.println();
        System.out.print("(" + this.arranjo + ") : ");
        for (int i=0; i < this.ordem.length; i++) System.out.print(this.ordem[i] + " ");
    }
    
    private void testaGrupo(){
        int ordemPessoa = 0;
        for(int i = 0; i < this.numeroGrupos; i++){
            this.somaGrupos[i] = 0;
            for(int j = 0; j < this.tamanhoGrupos[i]; j++){
                int numeroPessoa = this.ordem[ordemPessoa];
                this.grupos[i].set(j,numeroPessoa);
                this.grupoAlocado[numeroPessoa] = i;
                ordemPessoa++;
            }
        }
        this.efetuaSomas();
        if(this.somaTotal > this.maiorSoma){
            this.maiorSoma = this.somaTotal;
            this.msgSomas();
        }
    }
    
    private void hillClimb() throws FileNotFoundException, IOException{
        long inicio = System.currentTimeMillis();
        /*FileOutputStream fos = new FileOutputStream(new File("somas"));
        
        for(int a = 0; a < 1000; a++){
            HSSFRow row = firstSheet.createRow(a);
            this.maiorSoma = 0;*/
            for(int k = 0; k < 100; k++){
                efetuaSomas();
                this.semMudanca = 0;
                for(int i = 0; i < 10000000 && this.semMudanca < 10000; i++){                
                    int pessoaUm;
                    int pessoaDois;
                    int aux;
                    Random gerador = new Random();
                    pessoaUm = gerador.nextInt(this.numeroPessoas);
                    pessoaDois = gerador.nextInt(this.numeroPessoas);
                    while(this.grupoAlocado[pessoaUm] == this.grupoAlocado[pessoaDois]){
                        pessoaUm = gerador.nextInt(this.numeroPessoas);
                        pessoaDois = gerador.nextInt(this.numeroPessoas);
                    }
                    this.grupos[this.grupoAlocado[pessoaUm]].set(this.grupos[this.grupoAlocado[pessoaUm]].indexOf(pessoaUm), pessoaDois);
                    this.grupos[this.grupoAlocado[pessoaDois]].set(this.grupos[this.grupoAlocado[pessoaDois]].indexOf(pessoaDois), pessoaUm);
                    if(this.somaTotal < calculaSomaGrupo(this.grupoAlocado[pessoaUm], this.grupoAlocado[pessoaDois])){
                        aux = this.grupoAlocado[pessoaUm];
                        this.grupoAlocado[pessoaUm] = this.grupoAlocado[pessoaDois];
                        this.grupoAlocado[pessoaDois] = aux;
                        this.efetuaSomaUnica(this.grupoAlocado[pessoaUm], this.grupoAlocado[pessoaDois]);
                        this.semMudanca = 0;
                    }else{               
                        this.grupos[this.grupoAlocado[pessoaUm]].set(this.grupos[this.grupoAlocado[pessoaUm]].indexOf(pessoaDois), pessoaUm);
                        this.grupos[this.grupoAlocado[pessoaDois]].set(this.grupos[this.grupoAlocado[pessoaDois]].indexOf(pessoaUm), pessoaDois);
                        this.semMudanca++;
                    }
                }
                if(this.somaTotal > maiorSoma){
                    maiorSoma = this.somaTotal;
                    //msgSomas();
                }
                this.novoGrupo();
            }
           /* row.createCell(0).setCellValue(this.maiorSoma);
        }
        
        this.workbook.write(fos);
        fos.flush();
        fos.close();
        */
        //escreveSomas();
        //System.out.println("\n#####################################");
        System.out.println("Tempo do algoritmo: " + (System.currentTimeMillis() - inicio));
    }
    
    private void carregaGrafo() throws FileNotFoundException{
        String linha;
        String[] partes;
        int cont = 0;
        File file = new File("instancia.paa");
        this.in = new Scanner(file);
        linha = in.nextLine();
        partes = linha.split("\t");
        //this.numeroPessoas = Integer.parseInt(partes[1]);
        
        this.numeroPessoas = 15;
        
        this.ordemInicial = new ArrayList<>(this.numeroPessoas);
        this.grupoAlocado = new int[this.numeroPessoas];
        inicializaPessoas();
        linha = in.nextLine();
        partes = linha.split("\t");
        //this.numeroGrupos = Integer.parseInt(partes[1]);
        this.numeroGrupos = 2;
        this.grupos = new ArrayList[this.numeroGrupos];
        this.tamanhoGrupos = new int[this.numeroGrupos];
        this.somaGrupos = new double[this.numeroGrupos];   
        
//        for(int i = 0; i < this.numeroGrupos; i++){
//            linha = in.nextLine();
//            partes = linha.split("\t");
//            this.tamanhoGrupos[i] = Integer.parseInt(partes[1]);
//        }

        in.nextLine();
        in.nextLine();
        in.nextLine();
        in.nextLine();
        in.nextLine();
        in.nextLine();
        in.nextLine();
        
        this.tamanhoGrupos[0] = 7;
        this.tamanhoGrupos[1] = 8;
        //this.tamanhoGrupos[2] = 4;
        
        inicializaGrupos();
        grafo = new MatrizAdjacencia(this.numeroPessoas);
        
        for(int k = 0; k < this.numeroPessoas; k++){
            linha = in.nextLine();
            partes = linha.split("\t"); 
            for(int i = 0; i < this.numeroPessoas; i++){
                grafo.addArestaOrientada(cont, i, Integer.parseInt(partes[i]));
            }
            cont++;
        }
        
//        while(in.hasNextLine()){          
//            
//        }
    }
    
    private void inicializaGrupos(){
        int numeroPessoa = 0;
        for(int i = 0; i < this.numeroGrupos; i++){
            this.grupos[i] = new ArrayList(this.tamanhoGrupos[i]);
            this.somaGrupos[i] = 0;
            for(int j = 0; j < this.tamanhoGrupos[i]; j++){
                this.grupos[i].add(numeroPessoa);
                this.grupoAlocado[numeroPessoa] = i;
                numeroPessoa++;
            }
        }
    }
    
    private void novoGrupo(){
        Collections.shuffle(this.ordemInicial);
        int ordemPessoa = 0;
        for(int i = 0; i < this.numeroGrupos; i++){
            this.somaGrupos[i] = 0;
            for(int j = 0; j < this.tamanhoGrupos[i]; j++){
                int numeroPessoa = this.ordemInicial.get(ordemPessoa);
                this.grupos[i].set(j,numeroPessoa);
                this.grupoAlocado[numeroPessoa] = i;
                ordemPessoa++;
            }
        }
    }
    
    private void inicializaPessoas(){
        for(int i = 0; i < this.grupoAlocado.length; i++){
            this.grupoAlocado[i] = -1;
            this.ordemInicial.add(i);
        }      
    }
    
    private int calculaSomaGrupo(int grupo1, int grupo2){
        int somaGrupo = 0;
        int total = this.somaTotal;
        for(int j = 0; j < this.grupos[grupo1].size() - 1; j++){
            for(int k = j + 1; k < this.grupos[grupo1].size(); k++){
                somaGrupo += this.grafo.getPeso(this.grupos[grupo1].get(j), this.grupos[grupo1].get(k));
                somaGrupo += this.grafo.getPeso(this.grupos[grupo1].get(k), this.grupos[grupo1].get(j));
            }
        }
        total += somaGrupo - this.somaGrupos[grupo1];
        somaGrupo = 0;
        for(int j = 0; j < this.grupos[grupo2].size() - 1; j++){
            for(int k = j + 1; k < this.grupos[grupo2].size(); k++){
                somaGrupo += this.grafo.getPeso(this.grupos[grupo2].get(j), this.grupos[grupo2].get(k));
                somaGrupo += this.grafo.getPeso(this.grupos[grupo2].get(k), this.grupos[grupo2].get(j));
            }
        }
        total += somaGrupo - this.somaGrupos[grupo2];
        return total;
    }
    
    private void efetuaSomaUnica(int grupo1, int grupo2){
        int somaGrupo = 0;
        for(int j = 0; j < this.grupos[grupo1].size() - 1; j++){
            for(int k = j + 1; k < this.grupos[grupo1].size(); k++){
                int pessoa1 = this.grupos[grupo1].get(j);
                int pessoa2 = this.grupos[grupo1].get(k);
                somaGrupo += this.grafo.getPeso(pessoa1, pessoa2);
                somaGrupo += this.grafo.getPeso(pessoa2, pessoa1);
            }
        }
        this.somaGrupos[grupo1] = somaGrupo;
        somaGrupo = 0;
        for(int j = 0; j < this.grupos[grupo2].size() - 1; j++){
            for(int k = j + 1; k < this.grupos[grupo2].size(); k++){
                somaGrupo += this.grafo.getPeso(this.grupos[grupo2].get(j), this.grupos[grupo2].get(k));
                somaGrupo += this.grafo.getPeso(this.grupos[grupo2].get(k), this.grupos[grupo2].get(j));
            }
        }
        this.somaGrupos[grupo2] = somaGrupo;
        this.somarTotal();
    }
    
    private void efetuaSomas(){
        for(int i = 0; i < this.numeroGrupos; i++){
            int somaGrupo = 0;
            for(int j = 0; j < this.grupos[i].size() - 1; j++){
                for(int k = j + 1; k < this.grupos[i].size(); k++){
                    int pessoa1 = this.grupos[i].get(j);
                    int pessoa2 = this.grupos[i].get(k);
                    somaGrupo += this.grafo.getPeso(pessoa1, pessoa2);
                    somaGrupo += this.grafo.getPeso(pessoa2, pessoa1);
                }
            }
            this.somaGrupos[i] = somaGrupo;
        }
        this.somarTotal();
    }
    
    private void somarTotal(){
        this.somaTotal = 0;
        for(int i = 0; i < this.somaGrupos.length; i++){
            this.somaTotal += this.somaGrupos[i];
        }
    }
    
    private void msgSomas(){
        this.msgFinal = "";
        for(int i = 0; i < this.numeroGrupos; i++){
            this.msgFinal += "Soma do grupo " + i + ": " + this.somaGrupos[i] + "\n";
            //System.out.println("Soma do grupo " + i + ": " + this.somaGrupos[i]);
        }
        
        this.msgFinal += "-------------------------------------------------\n";
        //System.out.println("-------------------------------------------------");
        
        for(int i = 0; i < this.numeroGrupos; i++){
            this.msgFinal += "Grupo " + i;
            //System.out.print("Grupo " + i);
            this.msgFinal += " -> " + this.grupos[i].get(0);
            for(int j = 1; j < this.grupos[i].size(); j++){
                this.msgFinal += " - " + this.grupos[i].get(j);
                //System.out.print(" - " + this.grupos[i].get(j));
            }
            this.msgFinal += "\n";
            //System.out.println("");
        }
        
        this.msgFinal += "-------------------------------------------------\n";
        //System.out.println("-------------------------------------------------");
        this.msgFinal += "Soma Total: " + this.somaTotal + "\n";
        //System.out.print("Soma total: ");
        
        //System.out.println(this.somaTotal);
    }
    
    private void escreveSomas(){
        System.out.println(this.msgFinal);
    }
    
}
