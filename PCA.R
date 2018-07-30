
# convert SNP to nuumber fuction ("AACC_H__CA....", A/C) --> (1122000021....)
numericalization = function(snp,allie){ 
  # browser()
 # print("allie")
 # print(allie)
  alliesplited = unlist(strsplit(allie, split="/"))
  print(alliesplited[1])
  print(alliesplited[2])
  allie1 = if (!is.null(alliesplited[1])) alliesplited[1] else "z" 
  allie2 = if (!is.null(alliesplited[2])) alliesplited[2] else "z"
  
  if(allie1 < allie2){
    allie1 = 0
    allie2 = 2
  }else{
    allie1 =2 
    allie2 =0 
  }
  # snp[(snp != alliesplited[1]) <- 1
  #snp[(snp != alliesplited[2])] <- 1
  snp[(snp != alliesplited[1]) & (snp != alliesplited[2])] <- 1
  snp[snp == alliesplited[1]] <- allie1
  snp[snp == alliesplited[2]] <- allie2
  num = as.numeric(snp)
  class(num)
  return(as.numeric(snp))
}
#mat = matrix(data=NA ,nrows = nrows(snpname),ncol = ncol() )
#k = apply(mat,1,function(x) numericalization(x))





# this fuctions called from java fuction (list of <id, SNPs> , list{"A/C","A/T","C/T",....})
pca = function(data, alleles){
  print(length(data))
  print(length(alleles))
  alleles = unlist(alleles);
  print(class(alleles[1]))
  #return (class(data))
  for(index in 1:length(data)){
    listname = names(data[index]); # get id from list<id,SNP>
    data[listname] = c(strsplit(data[[listname]], "")); # split SNP AACC_H__CA... into char[] 
  }
  
  #return(data)
  z = as.data.frame(data,stringsAsFactors=FALSE)
  print(Sys.time())
  
  # doing numerilization for each row runs 42291 times takes 5 min to complte
  for(i in 1:nrow(z)) {
    z[i,] =  unlist(numericalization(z[i,],alleles[i]))
  }
  print(Sys.time())
  print("numbericaliztion done")
  print(z[1:20,1:3])
  mat = data.matrix(z)
  
  
#  print(z)
 # print(mat)
 # matt = t(mat)
  #print(rownames(matt));
  dataforpca = data.frame(mat)
  print("data for pca")
  print(dataforpca)
 # print(df)
   k <- prcomp(dataforpca);
   rot <- k$rotation ;
   print(rot[1:3,]);
   
   # returns table like structure having PCs as columns and Culitivar id as rows.
  return(data.frame(rot[,1:3]))

}

mysum <- function (x,param , b){
  x[x == param] <- b
  #print(x + ax)
  return (x)
  
}

pca2 = function(data , alleles){
  print(length(data))
  print(length(alleles))
  alleles = unlist(alleles);
  print(class(alleles[1]))
  #return (class(data))
  for(index in 1:length(data)){
    listname = names(data[index]); # get id from list<id,SNP>
    data[listname] = c(strsplit(data[[listname]], "")); # split SNP AACC_H__CA... into char[] 
  }
  print("data seprated")
  #return(data)
  a = as.data.frame(data,stringsAsFactors=FALSE)
  print("dataframe created")
  listofdash = rep("-",length(alleles))
  listofH = rep("H",length(alleles))
  print("listofdash created")
  allelessplited = strsplit(alleles, split="/")
  print("splited")
  print(length(allelessplited))
  minor = lapply(allelessplited, `[[`, 1)
  major = lapply(allelessplited, `[[`, 2)
  print("major and minor")
  incorrectmapping = which(unlist(minor) > unlist(major))
  
  for (index in incorrectmapping){
    temp = minor[index]
    minor[index] = major[index]
    major[index] = temp
  } 
  a_new <- sweep(a,1,listofdash,mysum,b = 1)
  a_new <- sweep(a_new,1,listofH,mysum,b = 1)
  a_new <- sweep(a_new,1,unlist(minor),mysum,b = 0)
  a_new <- sweep(a_new,1,unlist(major),mysum,b = 2)
  print("numbericaliztion done")
  print((a_new[1:20,1:3]))
  print("sweeping done")
  
  mat = data.matrix(a_new)
  
  print("matrix convertion done")
  
  dataframeforpca2 = data.frame(mat)
  print("data for pca2")
  print(dataframeforpca2)
  k <- prcomp(dataframeforpca2)
  print("PCA done")
  rot <- k$rotation
  print(rot[1:3,]);
  
  # returns table like structure having PCs as columns and Culitivar id as rows.
  return(data.frame(rot[,1:3]))
  #return(a_new)
}

#data = list(PI1000=l1,PI2000=l2,PI3000=l3, PI5000 = l4);
#alleles = list('A/C','T/G','C/T','A/C');
#pca2val = pca2(data,alleles)

#------ e.g --------#

#copy<- prcomp(USArrests,scale=T)
#it is just the same that: prcomp(iris[,1:4],scale=T) and prcomp(iris.stand)
#similar with princomp(): princomp(iris.stand, cor=T)

#summary(copy)
#This gives us the standard deviation of each component, and the proportion of variance explained by each component.
#The standard deviation is stored in (see 'str(pca)'):
#gip <-data.matrix(copy$rotation)


l1 = c("AGT-")
l2 = c("CT--")
l3 = c("H-H-")
l4 = c("-HC-")
datadump = list(PI1000=l1,PI2000=l2,PI3000=l3, PI5000 = l4);
allelesdump = list('A/C','G/T','C/T','A/C');
output = pca(datadump,allelesdump)
output2 = pca2(datadump,allelesdump)
#listone = fullist[1];
#name = names(listone);
#fullist[name] = (strsplit(fullist[[name]], ""))




#allies = c("A/C","G/T","T/C","A/C")
#l1 = c("A","G","T","-")
#l2 = c("C","T","_","-")
#l3 = c("H","_","H","-")
#l4 = c("_","H","C","-")
#snpname = c("snp1","snp2","snp3","snp4")
#sweep(bar, 2, seq_along(a), function(x,i) foo(x, a[i], b[i], c[i]), FALSE)
#a <- matrix(1:9,3)
#e <-matrix(c("A","C","T","A"),nrow = 2)
mmysum <- function (x,allie){
  print(allie)
  alliesplited = unlist(strsplit(allie, split="/"))
  allie1 = alliesplited[1]
  allie2 = alliesplited[2]
  print(allie1)
  print(allie2)
  if(allie1 < allie2){
    allie1 = 0
    allie2 = 2
  }else{
    allie1 =2 
    allie2 =0 
  }
  # snp[(snp != alliesplited[1]) <- 1
  #snp[(snp != alliesplited[2])] <- 1
  x[(x != alliesplited[1]) & (x != alliesplited[2])] <- 1
  x[x == alliesplited[1]] <- allie1
  x[x == alliesplited[2]] <- allie2
  #num = as.numeric(snp)
  #class(num)
  return(x)
  
}
#ak = c(100,200,300)
#ae = c("A/C","A/T")
#rmeans <- rowMeans(a)
#print(rmeans)
#a_new <- sweep(e,1,c(1,2),FUN = function(snp,i) mysum(snp,ae[i]))
#rowMeans(a_new)

#applyresult = apply(e,2,FUN = function(x,i) mysum(x,ae[i]))



