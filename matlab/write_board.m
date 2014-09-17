load('data_mlp_p60_regterm.mat');

n = length(center_errors);

median_errors = zeros([1,n]);

for i = 1:n
    median_errors(i) = median(error_dist(region_errors{i,1}));
end;
