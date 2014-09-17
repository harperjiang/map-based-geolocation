% Evaluate performance in big region, generate data for both rb and mlp
clc;
clear;
load('data_regions.mat');

[tm,tn] = size(big_regions);

center_errors = zeros([tm,1]);
region_errors = cell([tm,2]);
data_size = zeros([tm,1]);
region_nets = cell([tm,2]);


for index = 1:tm
    disp(strcat(int2str(index),':',int2str(tm)));
    % Collect data around the center point
    round_input = big_regions{index,2};
    round_target = big_regions{index,3};
    
    [m,n] = size(round_input);
    data_size(index) = n;
    
    [rb_net,rb_err] = train_rb(round_input, round_target);
%     mlp_net = train_best_mlp(round_input, round_target, 1);
    region_nets{index,1} = rb_net;
%     region_nets{index,2} = mlp_net;
    
    region_errors{index,1} = rb_err;
%     region_errors{index,2} = gsubtract(mlp_net(round_input),round_target);
end