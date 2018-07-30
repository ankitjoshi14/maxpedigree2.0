#---Convert HapMap to numerical format and calculate PCs---

#Install the following packages if you do not have them
#Instructions for installing are in the GAPIT help document
#Most of these are not necessary for converting to numerical or PC calculation
#Reduces number of errors that pop up though when installed and loaded into environment
#Load packages into environment with library()
library("bigmemory")
library("biganalytics")
library(multtest)
library(gplots)
library(LDheatmap)
library(genetics)
library(EMMREML)
library(compiler)
library("scatterplot3d")

#Load GAPIT source files
source("http://zzlab.net/GAPIT/gapit_functions.txt")

#Set working directory
setwd("C:\\Clint_Steketee\\")

#Load genotype file into R environment
myG <- read.table(file.choose(), head = FALSE) #genotypedroughtassociationpanel_alphabetical_alllines.hmp.txt

#Convert HapMap to numerical format using GAPIT function
myGAPIT <- GAPIT(G=myG,output.numerical=TRUE)

pc <- read.delim(file.choose(), row.names = 1) #GAPIT.Genotype.Numerical.txt
attach(pc)
mydist=dist(pc)
pcs = cmdscale(mydist, eig=T)
plot(pcs$points[,1], pcs$points[,2], cex=1.5) #plot of first two PCs
plot(pcs$eig, col="blue") #raw Eigenvalues
plot(pcs$eig / sum(pcs$eig), col="red") #relative Eigenvalues



#---Using built-in R functions and numeric genotypes with maize genotypes---

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
source("http://zzlab.net/GAPIT/gapit_functions.txt")

#Set working directory
setwd("C:\\Clint_Steketee\\")

#Load genotype data into R environment
myG <- read.table(file.choose(), head = FALSE) #genotypedroughtassociationpanel_alphabetical_alllines.hmp.txt

#Calculate PCs
myGAPIT <- GAPIT(
  G=myG,
  PCA.total=5,
  SNP.test = FALSE
)



pca=function(){
  k = princomp(USArrests,cor = TRUE, scores = TRUE)
  b = k$n.obs
  c= k$scores
  j =attributes(c)
  s=  j$dimnames
  return(k$scores)
  
}
