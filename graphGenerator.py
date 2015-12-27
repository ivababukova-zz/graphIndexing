import sys
import random
from random import randint


def generate(numbOfn, numbOfe, numbOfnProp, numbOfeProp, graphId):

    print(numbOfn, numbOfe, numbOfnProp, numbOfeProp, graphId)
    
    newFileName = "#" + graphId + "-" + numbOfn + "-" + numbOfe + "-" + numbOfnProp + "-" + numbOfeProp + ".txt"
    a = open(newFileName, "w")
    a.write("t # " + graphId + "\n")
    for nid in range(0,int(numbOfn)):
        prop = randint(0,int(numbOfnProp) - 1)
        a.write("v "+ str(nid) + " " + str(prop) + "\n")

    for iedge in range(0,int(numbOfe)):
        srcN = randint(0,int(numbOfn) - 1)
        # we don't want to have an edge that is from and to the same node
        leftNumbers = range(0,srcN) + range(srcN + 1,int(numbOfn))
        dstN = random.choice(leftNumbers)
        prop = randint(0,int(numbOfeProp) - 1)
        a.write("e " + str(srcN) + " " + str(dstN) + " " + str(prop) + "\n")
    return


numbOfn = sys.argv[1]
numbOfe = sys.argv[2]
numbOfnProp = sys.argv[3]
numbOfeProp = sys.argv[4]
#numbOfGraphs = sys.argv[5]
graphId = sys.argv[5]
generate(numbOfn, numbOfe, numbOfnProp, numbOfeProp, graphId)



