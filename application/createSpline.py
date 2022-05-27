import numpy as np
from scipy import interpolate
import sys
import json

X = np.asarray(sys.argv[1].split(','),dtype=float)
Y = np.asarray(sys.argv[2].split(','),dtype=float)
tck, u = interpolate.splprep([X, Y], k=3);
xnew, ynew = interpolate.splev(np.linspace(0, 1, 100), tck, der=0)
# result = "{ \"points\": [ "
for i in range(len(xnew)):
    print(str(xnew[i])+","+str(ynew[i]))
    # result = result + "{ \"x\": \""+str(xnew[i]) + "\", \"y\": \"" + str(ynew[i]) +"\" }, "

# result = result[:-1]
# result = result + "] }"
# f = open("result.txt", "w")
# f.write(result)
# f.close()
