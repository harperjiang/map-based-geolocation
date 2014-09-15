% Train and apply nnet to each local region 

% For each point, look for a dataset surrounding it
clear;
load('data_main.mat');
load('data_regions.mat');
dist = 3;

% Global Parameters for regulation term
global reg_center;
global reg_radius;
global reg_weight;

reg_radius = dist;
reg_weight = 100;

[tm,tn] = size(input);

errors = zeros([1,tn]);
data_size = zeros([1,tn]);

for index = 1:tn
    data = input(:,index);
    center = regions{index,1};
    
    % Collect data around the center point
    round_input = regions{index,2};
    round_target = regions{index,3};
    
    [m,n] = size(round_input);
    
    
    % This is apply mlp nnet
    reg_center = center;
    
    data_size(1,index) = n;
    if n == 0
       continue; 
    end
    net = train_best_mlp(round_input, round_target,30);
    err = norm(net(data)-center);
    errors(1,index) = err;
end