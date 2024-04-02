std_dev <- 5
ns <- c(10, 50, 100, 1000)
ps <- c()
for (n in ns) {
  std_err = std_dev / sqrt(n)
  u <- 1.0 / std_err
  l <- -1*u
  p <- (pnorm(u) - pnorm(l))
  print(p)
  ps <- append(ps, p)
}

plot(ns, ps, type="b", xlab = "Sample Sizes (n)", ylab = "Probability of +/- 1 of Population Mean")

