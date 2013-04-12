//DataLoader.groovy
import groovy.time.*
import com.thinkaurelius.titan.core.TitanFactory; 

//Set configuration parameters here

conf = new org.apache.commons.configuration.BaseConfiguration()
conf.setProperty('storage.batch-loading','true')
conf.setProperty("storage.backend","hbase")
conf.setProperty("storage.tablename","Contracts")
conf.setProperty("storage.hostname","127.0.0.1")
conf.setProperty("storage.index.search.backend","elasticsearch")
conf.setProperty("storage.index.search.local-mode",true)
conf.setProperty("storage.index.search.client-only",false)
conf.setProperty("storage.index.search.directory", "/Users/isofault/Desktop/TITAN")

g = TitanFactory.open(conf)
g.createKeyIndex("name",Vertex.class);

BatchGraph bgraph = new BatchGraph(g, VertexIDType.STRING, 10000);
bgraph.setVertexIdKey("name");
bgraph.setLoadingFromScratch(false);

//Start timer

def timeStart = new Date()

x=1

def f = new File("/Users/isofault/Desktop/TITAN/triplets").splitEachLine( /\t/ ){
 line -> 
	    node1_name=line[0]
	    node2_name=line[1]
	    relType_name=line[2]
		
		//Check if vertex already exist in graph
		
		Vertex node1=bgraph.getVertex(node1_name)
		Vertex node2=bgraph.getVertex(node2_name)
		
		//If both vertices don't exist
		
		if (node1==null && node2==null) {
		
		//println "node1 -> NO node2 -> NO"
		
		node1 = bgraph.addVertex(node1_name);
		node1.setProperty("name", node1_name);
		
		node2 = bgraph.addVertex(node2_name);
		node2.setProperty("name", node2_name);
		
		bgraph.addEdge(null, node1, node2, relType_name);
			
		} 
		else if (node1==null && node2!=null) {
		
		//println "node1 -> NO node2 -> YES"
		
		node1 = bgraph.addVertex(node1_name);
		node1.setProperty("name", node1_name);
		
		bgraph.addEdge(null, node1, node2, relType_name);
		
		}
		else if (node1!=null && node2==null) {
		
		//println "node1 -> YES node2 -> NO"
		
		node2 = bgraph.addVertex(node2_name);
		node2.setProperty("name", node2_name);
		
		bgraph.addEdge(null, node1, node2, relType_name);
		
		}
		else if (node1!=null && node2!=null) {
		
		//println "node1 -> YES node2 -> YES"
		
		bgraph.addEdge(null, node1, node2, relType_name);
		
		}
		
		if(x % 10000 == 0) {
		//bgraph.stopTransaction(SUCCESS)
		println x
		}
		
		x=x+1;
		
}

g.shutdown()

//End timer

def timeStop = new Date()

//Calculate time taken

TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
println duration
