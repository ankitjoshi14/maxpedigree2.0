#---Using built-in R functions and numeric genotypes---

#Principal coordinates
pc <- read.delim(file.choose(), row.names = 1) #08_maize_chr8.txt
attach(pc)
mydist=dist(pc)
pcs = cmdscale(mydist, eig=T)
plot(pcs$points[,1], pcs$points[,2], cex=1.5) #plot of first two PCs
plot(pcs$eig, col="blue") #raw values
plot(pcs$eig / sum(pcs$eig), col="red") #relative values

#------------------------------------------------------------

#---Using GAPIT with HapMap formatted genotypes---

#Load GAPIT source files
source("D:\\data-genetics\\clint\\GAPIT.R")

#Set working directory
setwd("D:\\data-genetics\\clint")

#Load genotype data into R environment
myG <- read.table(file.choose(), head = FALSE) #genotypedroughtassociationpanel_alphabetical_alllines.hmp.txt
browser()
#Convert HapMap to numerical format using GAPIT function
myGAPIT <- GAPIT(G=myG,output.numerical=TRUE)

#Calculate PCs
myGAPIT <- GAPIT(
  G=myG,
  PCA.total=5,
  SNP.test = FALSE
)


set.seed(123)
m <- matrix(runif(100), nrow=10)
cl <- hclust(dist(m))
plot(cl)

halfway <- hclustToTree(cl)
jsonTree <- toJSON(halfway)



