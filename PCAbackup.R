
l1 = c("AGT_")
l2 = c("CT__")
l3 = c("H_H_")
l4 = c("_HC_")
fullist = list(list1=l1,list2=l2,list3=l3, list4 = l4);
#listone = fullist[1];
#name = names(listone);
#fullist[name] = (strsplit(fullist[[name]], ""))

for(index in 1:length(fullist)){
 listname = names(fullist[index]);
 fullist[listname] = c(strsplit(fullist[[listname]], ""));
}


allies = c("A/C","G/T","T/C","A/C")
#l1 = c("A","G","T","-")
#l2 = c("C","T","_","-")
#l3 = c("H","_","H","-")
#l4 = c("_","H","C","-")
snpname = c("snp1","snp2","snp3","snp4")
z = as.data.frame(fullist,row.names = snpname,stringsAsFactors=FALSE)




numericalization = function(snp,allie){
  # browser()
  
  alliesplited = unlist(strsplit(allie, split="/"))
  allie1 = alliesplited[1]
  allie2 = alliesplited[2]
  
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
for(i in 1:nrow(z)) {
  z[i,] =  unlist(numericalization(z[i,],allies[i]))
}

mat = data.matrix(z)
matt = t(mat)



pca = function(matt){
  k = 
    prcomp(matt)
  j =attributes(c)
  s=  j$dimnames
  return(k)

}

pca(matt)


a <- matrix(c("A","C","_","C","T","_","_","C","G"),3)

ak = c(100,200,300)
#rmeans <- rowMeans(a)
#print(rmeans)
#a_new <- sweep(a,2,c(1,2,3),FUN = function(x,i) mysum(x,ak[i]))
#applyresult = apply(a,2,mysum)
allergy = c("A/C","T/C","G/C")
class(allergy)
alliesplited = strsplit(allergy, split="/")
class(alliesplited)
list1 = lapply(alliesplited, `[[`, 1)
class(list1)
list2 = lapply(alliesplited, `[[`, 2)

 comp = which(unlist(list1) > unlist(list2))
class(comp)
for (index in comp){
  temp = list1[index]
  list1[index] = list2[index]
  list2[index] = temp
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
  
  #return(data)
  a = as.data.frame(data,stringsAsFactors=FALSE)
  listofdash = rep("_",length(alleles))
  listofH = rep("H",length(alleles))
  allelessplited = strsplit(alleles, split="/")
  minor = lapply(allelessplited, `[[`, 1)
  class(list1)
  major = lapply(allelessplited, `[[`, 2)
  browser()
  incorrectmapping = which(unlist(minor) > unlist(major))
  class(comp)
  for (index in incorrectmapping){
    temp = minor[index]
    minor[index] = major[index]
    major[index] = temp
  } 
  a_new <- sweep(a,1,listofdash,mysum,b = 0)
  a_new <- sweep(a_new,1,listofH,mysum,b = 0)
  a_new <- sweep(a_new,1,unlist(minor),mysum,b = 1)
  a_new <- sweep(a_new,1,unlist(major),mysum,b = 2)
    return(a_new)
}

l1 = c("AGT_")
l2 = c("CT__")
l3 = c("H_H_")
l4 = c("_HC_")
data = list(PI1000=l1,PI2000=l2,PI3000=l3, PI5000 = l4);
alleles = list('A/C','T/G','C/T','A/C');
pca2val = pca2(data,alleles)




clusters <- hclust(dist(iris[, 3:4]))
par("mar")
par(mar=c(1,1,1,1))
plot(clusters)
height <- clusters$height
order <- clusters$order
print(height)
print(order)
