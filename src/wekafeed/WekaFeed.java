
package wekafeed;

import java.util.Random;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

public class WekaFeed extends AbstractClassifier{
  
  /* Node attributes */
  public Node[][] neuralNode;
  public static long seed = System.currentTimeMillis();
  public static Random rand = new Random(seed);
  
  
//==============================================================================
  WekaFeed(int inputCount, int hiddenLayerCount, int hiddenCount, 
          int outputCount){
    int layerCount = 0;  
    neuralNode = new Node[2+hiddenLayerCount][];
    
    //input Node
    neuralNode[layerCount] = new Node[inputCount];
    for(int j=0; j<inputCount; j++){
      neuralNode[layerCount][j] = new Node();
    }
    layerCount++;
    
    //hidden Node
    for(int i=0; i<hiddenLayerCount; i++){
      neuralNode[layerCount] = new Node[hiddenCount];
      for(int j=0; j<hiddenCount; j++){
        neuralNode[layerCount][j] = new Node();
      }
      layerCount++;
    }
    
    //output Node
    neuralNode[layerCount] = new Node[outputCount];
    for(int j=0; j<outputCount; j++){
      neuralNode[layerCount][j] = new Node();
    }
    layerCount++;
    
    connectNodes();
  }
//==============================================================================
  public void connectNodes(){
    int layerCount = neuralNode.length;
    int layerNodes;
    int nextLayerNodes;
    for(int i=0; i<layerCount-1; i++)
    {
      layerNodes = neuralNode[i].length;
      nextLayerNodes = neuralNode[i+1].length;
      for(int j=0; j<layerNodes; j++)
      {
        for(int k=0; k<nextLayerNodes; k++)
        {
          neuralNode[i][j].edges.put(neuralNode[i+1][k].id, rand.nextDouble());
        }
      }
    }
  }
//==============================================================================
  public void printAllEdge(){
    int layerCount = neuralNode.length;
    int layerNodes;
    for(int i=0; i<layerCount; i++)
    {
      System.out.println("Layer"+i+"============================");
      layerNodes = neuralNode[i].length;
      for(int j=0; j<layerNodes; j++)
      {
        System.out.println("Node"+neuralNode[i][j].id+"----------------------");
        for(int key: neuralNode[i][j].edges.keySet()){
          System.out.println("ID: "+key+" value: "+neuralNode[i][j].edges.get(key));
        }
      }
    }
  }
//==============================================================================
  public void printAllNode(){
    int layerCount = neuralNode.length;
    int layerNodes;
    for(int i=0; i<layerCount; i++)
    {
      System.out.println("Layer"+i+"========================"+getLayerSigma(i));
      layerNodes = neuralNode[i].length;
      for(int j=0; j<layerNodes; j++)
      {
        System.out.println(neuralNode[i][j].id+": "+neuralNode[i][j].value);
      }
    }
  }
//==============================================================================
  public double getLayerSigma(int layerIndex){
    int layerNodes = neuralNode[layerIndex].length;
    double sum = 0;
    for(int j=0; j<layerNodes; j++)
    {
      sum += neuralNode[layerIndex][j].value;
    }
    
    return sum;
  }
//==============================================================================
  public boolean assignInput(double[] initiator){
    if(neuralNode[0] != null)
    {
      if(neuralNode[0].length == initiator.length)
      {
        for(int i=0;i<neuralNode[0].length; i++)
        {
          neuralNode[0][i].value = initiator[i];
        }
      }
      else
      {
        System.out.println("Banyak nilai salah");
        return false;
      }
    }
    else
    {
      System.out.println("eror");
      return false;
    }
    
    return true;
  }
//==============================================================================
  public int[] searchNode(int searchId){
    int[] result = new int[2];
    result[0] = -1;
    result[1] = -1;
    
    for(int i=0; i<neuralNode.length; i++){
      for(int j=0; j<neuralNode[i].length; j++){
        if(neuralNode[i][j].id == searchId){
          result[0] = i;
          result[1] = j;
        }
      }
    }
    return result;
  }
//==============================================================================  
  public boolean assignPostEdgeWeight(int id, double[] initiator){
    int[] index = searchNode(id);
    int i = index[0];
    int j = index[1];
    int k = 0;
    
    if(initiator.length != neuralNode[i].length)
    {
      System.out.println("kurang panjang");
      return false;
    }
    
    for(int key: neuralNode[i][j].edges.keySet()){
      neuralNode[i][j].edges.put(key, initiator[k]);
      k++;
    }
    
    return true;
  }
//==============================================================================  
  public boolean assignPreEdgeWeight(int id, double[] initiator){
    int[] index = searchNode(id);
    int i = index[0];
    int j = index[1];
    int k = 0;
    int prevLayer = i-1;
    
    if(i<1)
    {
      System.out.println("oh tidak bisa");
      return false;
    }    

    if(initiator.length != neuralNode[prevLayer].length)
    {
      System.out.println("kurang panjang");
      return false;
    }
    
    for(j=0; j<neuralNode[prevLayer].length; j++){
      for(int key: neuralNode[prevLayer][j].edges.keySet()){
        if(key == id){
          neuralNode[prevLayer][j].edges.put(key, initiator[k]);
          k++;
        }
      }
    }
    
    return true;
  }
//==============================================================================  
  public boolean assignEdge(int idpre, int idpost, double initiator){
    int[] indexpre = searchNode(idpre);
    int ipre = indexpre[0];
    int jpre = indexpre[1];
    
    for(int key: neuralNode[ipre][jpre].edges.keySet()){
      if(key == idpost){
        neuralNode[ipre][jpre].edges.put(key, initiator);
        return true;
      }
    }
    
    
    return false;
  }
//==============================================================================
  @Override
  public void buildClassifier(Instances i) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
//==============================================================================
  public static void main(String[] args) {
    
    int inputCount = 4;
    int hiddenCount = 4;
    int outputCount = 4;
    
    loaddata load = new loaddata("C:\\Users\\AlbertusK95\\Documents\\NetBeansProjects\\Tubes2AI\\data\\Team.arff");
    System.out.println("Banyak atribut adalah " + loaddata.banyakatribut);
    System.out.println("Banyak kelas adalah " + loaddata.banyakkelas);
   
    inputCount=loaddata.banyakatribut;
    outputCount=loaddata.banyakkelas;
    
    
    WekaFeed weka = new WekaFeed(inputCount, 4, hiddenCount, outputCount);
    weka.assignInput(new double[]{1,2,3,4});
    weka.assignPostEdgeWeight(0, new double[]{4,5,6,7});
    weka.assignPreEdgeWeight(5, new double[]{10,11,12,13});
    weka.assignEdge(10, 15, 99);
    
    weka.printAllNode();
    System.out.println("");
    System.out.println("");
    weka.printAllEdge();
    
    
    // convert nominal to numeric for class
    

    
    // dataset preprocessing
    try {
    
        Normalization nm = new Normalization(load.train_data);
        Instances normalizedDataset = nm.normalize();
    
        System.out.println();
        System.out.println("Normalized data train");
        System.out.println(normalizedDataset);
        
    } catch (Exception e) {
        
        e.printStackTrace();
       
    }
    
  }

  
  
}
