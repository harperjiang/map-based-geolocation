clear;
load('data_mlp_big_result.mat');

median_errors = zeros([length(center_errors) 1]);

for i = 1:length(center_errors) 
    median_errors(i) = median(error_dist(region_errors{i}));
end

% Separate blocks
