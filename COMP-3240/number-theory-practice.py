def mod_and_div(x, n):

    remainder = x
    divisor = 0

    while remainder >= n:
        remainder -= n
        divisor += 1

    return remainder, divisor



def euclid(x, y):

    if y < x:
        tempX = x
        x = y
        y = tempX

    r = y % x

    while r != 0 :
        y = x
        x = r
        r = y % x

    return x



def extended_euclid(x, y):

    if y % x == 0:
        return 1, 0, x
    
    a, b, r = extended_euclid(y % x, x)

    return (b - ((y/x) * b)), b, r

print(mod_and_div(53663, 307))
