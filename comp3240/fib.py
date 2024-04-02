def fib(n):
    if n==0:
        return(1)
    if n==1:
        return(1)
    last=1
    current=1
    for i in range(2, n+1):
        temp=last+current
        last=current
        current=temp
    return(current)

print(str(fib(6)))