% Evaluate MLP 

clear;
load('data_main.mat');
load('data_regions.mat');
dist = 3;

% Global Parameters for regulation term
%global reg_center;
global reg_radius;
global reg_weight;

reg_radius = dist;
reg_weight = 1000;

[tm,tn] = size(input);

center_errors = zeros([1,tn]);
region_errors = cell([tn,1]);
data_size = zeros([tn,1]);
region_nets = cell([tn,1]);

for index = 1:tn
    data = input(:,index);
    center = regions{index,1};
    
    % Collect data around the center point
    round_input = regions{index,2};
    round_target = regions{index,3};
    
    [m,n] = size(round_input);
    
    data_size(index) = n;
    if n == 0
       continue; 
    end
    net = train_best_mlp(round_input, round_target,30);
    region_nets{index} = net;
    err = norm(net(data)-center);
    center_errors(index) = err;
    region_errors{index} = gsubtract(net(round_input),round_target);
end