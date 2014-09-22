% Train a mlp multiple times to see the performance distribution
clear;
load('data_region_big.mat');

x = big_regions{2,2};
t = big_regions{2,3};

ite = 50;

me = zeros([1,ite]);

for index = 1 : ite
    [m,n] = size(x);
    
    [net,errors,perf] = train_mlp(x, t);
    me(index) = median(error_dist(errors));
end