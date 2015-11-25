allNodes = {}
class Node:
    #self.properties string of properties
    #self.edges  [nodeRef1, edgeProperties1, nodeRef2, edgeProperties2, nodeRef3, edgeProperties3]
    def getLength(self):
        return len(self.edges)/2
    def getNode(self, index):
        return self.edges[index*2], self.edges[index*2 + 1]
    def yieldChildren(self):
        for i in range(self.getLength()):
            yield self.getNode(i)
    def __init__(self, nodeNumber, properties):
        self.properties = properties
        self.edges = []
        self.visited = False
        self.nodeNumber = nodeNumber
        allNodes[nodeNumber] = self
    def addEdge(self, node1, edgeProperties):
        self.edges += [node1, edgeProperties]
        return self.getLength() - 1

with open("exampleGraph") as g:
    for line in g:
        line = line.rstrip("\n")
        line = line.split(" ")
        if line[0] == 't':
            pass
        if line[0] == 'v':
           nodeNumber = line[1]
           properties = line[2]
           Node(nodeNumber, properties)
        if line[0] == 'e':
           nodeA = allNodes[line[1]]
           nodeB = allNodes[line[2]]
           edgeProperties = line[3]
           nodeA.addEdge(nodeB, edgeProperties)
           nodeB.addEdge(nodeA, edgeProperties)

index = {}

def putToIndex(path):
    if path[::-1] < path:
        path = path[::-1]
    if path in index:
       index[path] += 1
    else:
       index[path] = 1
    

def pathsOfLength(nodes, length):
    for i in nodes:
        node = nodes[i]
        pathsOfLengthInner(node, length, node.properties, 1)

def pathsOfLengthInner(node, maxLength, partialPath, currentLength):
    if currentLength == maxLength:
        putToIndex(partialPath)
        return
    for child in node.yieldChildren():
        pathsOfLengthInner(child[0], maxLength, partialPath + child[1] + child[0].properties, currentLength + 1)

for i in range(1, 4):
    pathsOfLength(allNodes, i)

print(index)
