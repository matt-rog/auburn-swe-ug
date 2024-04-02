data(morley)
meas <- morley$Speed+299000 

n = length(vector)
x = mean(meas)
s = sd(meas)

z = qnorm(0.95)
error = z * (s/sqrt(n))

lower_bound = x - error
upper_bound = x + error

lower_bound
upper_bound

