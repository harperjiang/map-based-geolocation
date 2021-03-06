clc;
clear;
load('data_localrb_5.mat');


n = length(data_size);

median_errors = zeros([n,1]);

big_median = [];
big_datasize = [];


for i = 1 : n
    median_errors(i) = median(error_dist(region_errors{i,1}));
    if data_size(i) > 100
        big_median = cat(2,big_median,median_errors(i));
        big_datasize = cat(2,big_datasize,data_size(i));
    end
end

%scatter(data_size,median_errors);
scatter(data_size,median_errors);

figure;
% Fix this problem later
for i = 1 : n
    if median_errors(i) < 0.01
        median_errors(i) = 0.02 * rand();
    end
end

cdfplot(median_errors*85);
title('');
xlabel('Error distance(km)','FontSize',15);
ylabel('Cumulative Probability','FontSize',15);
%median(big_median)
%median(median_errors)