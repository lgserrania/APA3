/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apa3;

import java.util.LinkedList;
import java.util.List;

public class MatrizAdjacencia{

    private double[][] matriz;
    int numeroVertices;
    
    //#############################################################################

    public MatrizAdjacencia(int numeroDeVertices) {
        this.matriz = new double[numeroDeVertices][numeroDeVertices];
        this.numeroVertices = numeroDeVertices;
    }

    public double[][] getMatriz() {
        return matriz;
    }

    public int getNumeroDeVertices() {
        return this.matriz.length;
    }

    public void addAresta(int origem, int destino) {
        if (origem != destino) {
            this.matriz[origem][destino] = 1;
            this.matriz[destino][origem] = 1;
        }
    }

    public void addAresta(int origem, int destino, double peso) {
        if (origem != destino) {
            this.matriz[origem][destino] = peso;
            this.matriz[destino][origem] = peso;
        }
    }
    
    public void addArestaOrientada(int origem, int destino) {
        if (origem != destino) {
            this.matriz[origem][destino] = 1;
        }
    }
    
    public void addArestaOrientada(int origem, int destino, double peso) {
        if (origem != destino) {
            this.matriz[origem][destino] = peso;
        }
    }

    public void setAresta(int origem, int destino) {
        this.addAresta(origem, destino);
    }

    public void setAresta(int origem, int destino, double peso) {
        this.addAresta(origem, destino, peso);
    }

    public void removeAresta(int origem, int destino) {
        this.setAresta(origem, destino, 0);
    }

    public boolean isAdjacente(int origem, int destino) {
        return this.matriz[origem][destino] != 0;
    }

    public List getAdjacentes(int vertice) {
        List<ArestaSimples> adj = new LinkedList<>();
        for (int coluna = 0; coluna < this.matriz[vertice].length; coluna++) {
            if (isAdjacente(vertice, coluna)) {
                adj.add(new ArestaSimples(vertice, coluna, this.matriz[vertice][coluna]));
            }
        }
        return adj;
    }

    public double getPeso(int origem, int destino) {
        return this.matriz[origem][destino];
    }
    
    public int addVertice(){
        double[][] matriz2 = new double[this.numeroVertices + 1][this.numeroVertices + 1];
        for(int i = 0; i < this.numeroVertices; i++){
            for(int j = 0; j < this.numeroVertices; j++){
                matriz2[i][j] = this.matriz[i][j];
            }
        }
        this.matriz = matriz2;
        this.numeroVertices++;
        return this.numeroVertices;
    }
    
    private void imprimeMatriz(){
        for(int i = 0; i < this.numeroVertices; i++){
            for(int j = 0; j < this.numeroVertices; j++){
                System.out.print("[" + this.matriz[i][j] + "]");
            }
            System.out.println("");
        }
    }
    
    public void imprime(){
        imprimeMatriz();
    }
    
}
