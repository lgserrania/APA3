/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apa3;

public class ArestaSimples implements InterfaceAresta{

    private int origem;
    private int destino;
    private double peso;
    private String tipo = null;
    
    public ArestaSimples(){
        this.origem = -1;
        this.destino = -1;
        this.peso = 0;
        //System.out.println("Aresta sem origem e destino definidos criada!");
    }
    
    public ArestaSimples(int origem, int destino){
        this.origem = origem;
        this.destino = destino;
        this.peso = 0;
        //System.out.println("Aresta [" + origem + "] -> [" + destino + "] criada");
    }
    
    public ArestaSimples(int origem, int destino, double peso){
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
       // System.out.println("Aresta [" + origem + "] -> [" + destino + "] com peso " + peso + " criada");
    }
    
    @Override
    public void setOrigem(int origem) {
        this.origem = origem;
    }

    @Override
    public int getOrigem() {
        return this.origem;
    }

    @Override
    public void setDestino(int destino) {
        this.destino = destino;
    }

    @Override
    public int getDestino() {
        return this.destino;
    }

    @Override
    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public double getPeso() {
        return this.peso;
    }
    
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    
   public String getTipo(){
       if(this.tipo != null){
           return this.tipo;
       }else{
           System.out.println("Aresta não classificada!");
           return null;
       }
   }
   
   public double compareTo(ArestaSimples aresta){
       return this.peso - aresta.peso;
   }
}
