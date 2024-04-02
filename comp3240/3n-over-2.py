import sys
sys.setrecursionlimit(2000)


def main(array):
    length = len(array)
    if(length == 1):
        return array
    # split array into first two
    f, l = splitArray(array)
    fElement = main(f)
    lElement = main(l)

    minCand = []
    maxCand = []
    if(fElement > lElement):
        maxCand.append(fElement)
        minCand.append(lElement)
    
    return main(minCand), main(maxCand)

def splitArray(array):
    length = len(array)
    if (length%2==0):
        firstHalf = array[:length//2]
        lastHalf = array[length//2:]
    else:
        firstHalf = array[:length-1]
        lastHalf = array[-1]
    return firstHalf, lastHalf

array = [59, 1, 7, 74, 12, 44, 3, 54, 0, 36, 2]

mi, ma = main(array)
print(str(mi) + "min")
print(str(ma) + "max")

