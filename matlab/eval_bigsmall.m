% Evaluate MLP
clc;
clear;
load('data_region_big_small.mat');

%region_big_target = regularize(region_big_target);
%region_small_target = regularize(region_small_target);

big_net = train_best_mlp(region_big_input, region_big_target, 30);
small_net = train_best_mlp(region_small_input, region_small_target, 30);

region_big_output = big_net(region_big_input);
region_small_output = small_net(region_small_input);

big_error = gsubtract(region_big_target, region_big_output);
small_error = gsubtract(region_small_target, region_small_output);

