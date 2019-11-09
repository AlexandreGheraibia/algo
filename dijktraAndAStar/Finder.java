import java.util.*; 
import java.util.stream.Collectors; 
import java.util.stream.Stream; 
import java.util.stream.IntStream; 
import java.util.Comparator;

class Arc{

  private Node n;
  private int d;
  
  Arc(Node n, int d){
    this.n=n;
    this.d=d;
  }
  
  public int getDistance(){
    return this.d;
  }
  
  public Node getNode(){
    return this.n;
  }
}

class Node{
   private int value=Integer.MAX_VALUE;
   private List<Arc>arcs=null;
   private int distance=Integer.MAX_VALUE;
   private boolean inP=false;
   private boolean inOpened=false;
   private boolean inClosed=false;
   private float costSS=0; //effectif cost since Start
   private float costUE=0; //Theorical cost until the end;
   private float heuCost=0; //heuristic cost;
   private Node parent=null;
   private int i=0;
   private int j=0;
   
   Node(String value){
        this.value=Integer.valueOf(value);
   }
   
   Node(int value){
        this.value=value;
   }
   
   public int getValue(){
       return this.value;
   }
   
   public void setIndex(int i, int j){
         this.i=i;
         this.j=j;
   }
   
   public int getI(){
         return this.i;
   }
   
   public int getJ(){
         return this.j;
   }
   
   public void setDistance(int distance){
     this.distance=distance;
   }
   
   public int getDistance(){
       return this.distance;
   }
   
   public void add(Node n){
   
       this.arcs.add(new Arc(n,Math.abs(n.getValue()-this.getValue())));
   }
   
   public List<Arc> getArcs(){
       return arcs;
   }
   
   public void setArcs(List<Arc> arcs){
     this.arcs=arcs;
   }
   
   public boolean isInP(){
       return inP;
   } 
   
   public void setInP(boolean inP){
       this.inP=inP;
   }
   
   public void generateHeuCost(){
       this.heuCost=this.costSS+this.costUE;
   }
   
   public void setCostUntilEnd(float costUE){
         this.costUE=costUE;
   }
   
   public float getCostUntilEnd(){
         return this.costUE;
   }
   
   public void updateCost(float costSS){
         this.costSS=costSS;
   }
   
   public void setOpenedList(){
         inOpened=true;
   }
   
   public void setClosedList(){
         inClosed=true;
   }
   
   public void setNotOpenedList(){
         inOpened=false;
   }
   
   public boolean isInOpenedList(){
         return inOpened;
   }
   
   public boolean isInClosedList(){
         return inClosed;
   }
   
   public float getHeuCost(){
       return heuCost;
   }
   
   public float getCost(){
       return this.costSS;
   }
   
   public void setParent(Node parent){
       this.parent=parent;
   }
   
   public Node getParent(){
       return this.parent;
   }
   
   public String toString(){
   String s=this.getHeuCost()+" "+this.getValue()+" ";
           if(this.parent!=null)
            s+=this.parent.getValue()+",";
        return s; 
   } 
}

class Graph{

    private int n;
    private Node[][] nodes;
    private List<Node> outP;
    private List<Node> P;
    private List<Node> openedList=new ArrayList<>();
    private List<Node> closedList=new ArrayList<>();
    
    public Graph(String maze){
        nodes=strToArray(maze);
    }
    
    public void  displayUE(){
      Arrays.stream(nodes).forEach(line->{
                  Arrays.stream(line).map(Node::getCostUntilEnd).forEach(e->System.out.print(e));
                  System.out.println();
      });
      System.out.println();
    }
     
    private Node[][] strToArray2(String maze){
      return Arrays.stream(maze.split("\n"))
                       .map(line->Arrays.stream(line.split("")).map(Node::new).toArray(Node[]::new))
                       .toArray(Node[][]::new);
    }
    
    private Node[][] strToArray(String maze){
      String[] lines=maze.split("\n");
      n=lines.length;
      Node[][] nodes=new Node[n][n];
      
      for(int i=0;i<n;i++){
          String[] col=lines[i].split("");
           for(int j=0;j<n;j++){
                 nodes[i][j]=new Node(col[j]);
                 nodes[i][j].setIndex(i,j);
           }
      }
    
      return nodes;
    }
     
    private void createArcs(int i, int j){
    
	   Node nCurr= nodes[i][j];
	   if((i==0||i==n-1)&&j!=0&&j!=n-1){
		   int dir=(i==0?1:-1);
		   nCurr.add(nodes[i+dir][j]);
		   nCurr.add(nodes[i][j+1]);
		   nCurr.add(nodes[i][j-1]);
	   }
	   else{
			if((j==0||j==n-1)&&i!=0&&i!=n-1){
			   int dir=(j==0?1:-1);
			   nCurr.add(nodes[i+1][j]);
			   nCurr.add(nodes[i][j+dir]); 
			   nCurr.add(nodes[i-1][j]);
			}
			else{
				if(i==0&&j==0){//top left corner
				   nCurr.add(nodes[i+1][j]);
				   nCurr.add(nodes[i][j+1]);
				}
				else{
					if(i==0&&j==n-1){//top righ corner
					  nCurr.add(nodes[i+1][j]);
					  nCurr.add(nodes[i][j-1]);
					 
					}
					else{
					  if(i==n-1&&j==n-1){//bottom right corner
						 nCurr.add(nodes[i-1][j]);
						 nCurr.add(nodes[i][j-1]);
					  }
					  else{
						 if(i==n-1&&j==0){//bottom left corner
						   nCurr.add(nodes[i][j+1]);
						   nCurr.add(nodes[i-1][j]);
						 }
						 else{
							nCurr.add(nodes[i+1][j]);
							nCurr.add(nodes[i][j+1]);
							nCurr.add(nodes[i][j-1]);
							nCurr.add(nodes[i-1][j]);
						 }
					 }
				  }
			  }
		  }
	  }
		  
	  nCurr.setCostUntilEnd((float)Math.floor(2*(n-1)-i-j));
      
	}
   
	public Node[][] getArray(){
		return nodes;
	}
   
	private Node nodeoutPLessFar(){
		 Node nf=outP.stream().filter(n->!n.isInP())
					  .sorted(Comparator.comparingInt(Node::getDistance))
					  .findFirst()
					  .orElse(null);
		 if(nf!=null){
			 outP.remove(nf);
		 }
		 return nf;
	}
   
	public int dijktra(){
	   nodes[0][0].setDistance(0);
	   P=new ArrayList<>();
	   outP=Arrays.stream(nodes).flatMap(line->Arrays.stream(line)).collect(Collectors.toList());
	   while(outP.size()>0){
		 Node a=nodeoutPLessFar();
		 if(a!=null){
		   a.setInP(true);
		   P.add(a);
		  if(a.getArcs()==null){
		   a.setArcs(new ArrayList<>());
		   createArcs(a.getI(),a.getJ());
		 }
		   a.getArcs().stream().filter(arc->!arc.getNode().isInP()).forEach(arc->{
				int min=Math.min(arc.getNode().getDistance(),a.getDistance()+arc.getDistance());
				if(min!=arc.getNode().getDistance()){
					  arc.getNode().setParent(a);
				}
			   arc.getNode().setDistance(min);                   
		   });
			
		 }
		 
	   }
		//chemin(nodes[n-1][n-1]);//------------------
		int d=nodes[n-1][n-1].getDistance();
		//System.out.println("dijktra sol:"+d);//------------------
		//System.out.println();//------------------
	   return d==Integer.MAX_VALUE?0:d;
	}
	
	private void insertOpenList(Node n){
        int min=0;
        int max=openedList.size()-1;
        int middle=(min+max)/2;
        
        while(min<max){
			if(openedList.get(middle).getHeuCost()>n.getHeuCost()){
			  max=middle-1;
			}
			else{
			   if(openedList.get(middle).getHeuCost()<n.getHeuCost()){
				  min=middle+1;
			  }
			  else{
				  min=middle;
				  max=middle;
			  }
			}
			middle=(min+max)/2;
        }
        
		if(openedList.size()>0){
		  if(n.getHeuCost()>=openedList.get(middle).getHeuCost()){
				 openedList.add(middle+1,n);
		  }
		  else{
			   openedList.add(middle,n);
		  }
		}
		else{
		openedList.add(n);
		}
    }
	
	private void addOpenL(Node n){
		if(!n.isInClosedList()){
			  if(!n.isInOpenedList()){
				  n.setOpenedList();
				//  System.out.println(n.getValue());//-----------------------
				  insertOpenList(n);
			  }   
		}    
	}

	private void addClosedL(Node n){
		if(!n.isInClosedList()){
			  if(n.isInOpenedList()){
				  n.setClosedList();
				  closedList.add(n);
				  if(openedList.size()>0){
					  openedList.remove(n);
				  }
			  }
		}
	}

	private Node getCurrElem(){
	   return closedList.size()>0?closedList.get(closedList.size() - 1):null;
	}

	private void displayOpenList(){
	   openedList.stream().forEach(System.out::print);
	   System.out.println();
	}
  
	private Node bestChoise(Node n){
	   if(openedList.size()>0){
			 List<Node> l=openedList.stream().filter(e->e.getHeuCost()==openedList.get(0).getHeuCost())
									.collect(Collectors.toList());
		  // System.out.println("child :");//-----------------------
		   return l.stream()
				   .sorted(Comparator.comparingDouble(Node::getCostUntilEnd).reversed())
				   .findFirst().orElse(l.get(0));
		}
	   return null;
	}

	public int pathAStar(Node start,Node end){
	   Node currNode;
	   addOpenL(start);
	   addClosedL(start);
	   Node bChoise=null;
	   currNode=getCurrElem();
	   currNode.setParent(null);
	   do{
		 //System.out.println("currNode "+currNode);//-----------------------
		 if(currNode.getArcs()==null){
		   currNode.setArcs(new ArrayList<>());
		   createArcs(currNode.getI(), currNode.getJ());
		 }
		 
		 for(int i=0;i<currNode.getArcs().size();i++){
			   Arc a= currNode.getArcs().get(i);
			   Node n=a.getNode();
			   if(n!=null&&!n.isInClosedList()){
				   if(!n.isInOpenedList()){
					   n.updateCost(a.getDistance()+currNode.getCost());
					   n.generateHeuCost();
					   n.setParent(currNode);
					   addOpenL(n); 
					}
					else{
						 float d=Math.min(n.getHeuCost(),a.getDistance()+currNode.getCost()+n.getCostUntilEnd());
						 if(d!=n.getHeuCost()){
							  n.updateCost(a.getDistance()+currNode.getCost());
							  n.generateHeuCost();
							  n.setParent(currNode);
						 }
						openedList.remove(n);
						insertOpenList(n);
					}
			   }
		 }
	   //  displayOpenList();//-----------------------
	   //  System.out.println();//--------------------------
		 bChoise=bestChoise(currNode);
		 if(bChoise!=null){
			  addClosedL(bChoise);
			  currNode=getCurrElem();
			  
		 }  
	   }while(bChoise!=null&&bChoise!=end);

	  if(bChoise==end){
		 // this.chemin(nodes[n-1][n-1]);//--------------------
		  return (int)bChoise.getCost();
	  }
	  return 0;
	}

	public void  display(){
	  Arrays.stream(nodes).forEach(line->{
				  Arrays.stream(line).map(Node::isInClosedList).forEach(e->System.out.print(e));
				  System.out.println();
	  });
	   System.out.println();
	}

	public void chemin(Node o){
	  String s="";
	  while(o!=null){
		  s=" "+o.getValue()+s;
		  o=o.getParent();
	  }
	  System.out.println("sol:"+s);
	  System.out.println();
	}

}

public class Finder {
    static int pathFinder(String maze) {
        if(maze.length()>1){
            Graph g=new Graph(maze);
            Node[][] nodes=g.getArray();
            int n=nodes.length;
            int outC=g.pathAStar(nodes[n-1][n-1],nodes[0][0]); 
            return outC;
        }
        return 0;
    }
    
	static int pathFinderB(String maze) {
		if(maze.length()>1){
			Graph g=new Graph(maze);
			return g.dijktra();
		}
		return 0;
	}
}
