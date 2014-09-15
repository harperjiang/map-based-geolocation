% Train and apply nnet to each local region 

% For each point, look for a dataset surrounding it
clear;
load('data_main.mat');
load('data_regions.mat');

% Global Parameters for regulation term

[tm,tn] = size(input);

median_errors = zeros([1,tn]);
center_errors = zeros([1,tn]);
data_size = zeros([1,tn]);

for index = 1:tn
    data = input(:,index);
    center = regions{index,1};
    
    % Collect data around the center point
    round_input = regions{index,2};
    round_target = regions{index,3};
    
    [m,n] = size(round_input);
    
    if n == 0
        continue;
    end
    % Todo This is apply quadratic
    [beta, errs] = train_volterra(round_input,round_target);
    
    center_errors(1, index) = norm(errs(:,1));
    median_errors(1, index) = median(error_dist(errs));
    data_size(1, index) = n;
end