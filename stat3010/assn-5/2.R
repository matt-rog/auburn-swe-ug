shear <- read.csv("hw5q2.csv", header = TRUE)

# 2a
lrm_a = lm(shear$y ~ shear$x1 + shear$x2, data = shear)
summary(lrm_a)

# 2b
lrm_b = lm(shear$y ~ shear$x1 + shear$x2 + (shear$x1*shear$x2), data = shear)
summary(lrm_b)

# 2c
lrm_c = lm(shear$y ~ shear$x1 + shear$x2 + (shear$x1*shear$x2) + c(shear$x1^2) + c(shear$x2^2),  data = shear)
summary(lrm_c)

