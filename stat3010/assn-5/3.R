n = 200
x = 2.49
s = 0.674

z = qnorm(0.96)
error = z * (s/sqrt(n))
left = x- error
right = x + error

left
right

