% Generate local regions
clc;
clear;
load('data_regions.mat');

[n,dim] = size(regions);

region_small_input = [];
region_small_target = [];
region_big_input = [];
region_big_target = [];

for i = 1 : n
    region_input = regions{i,2};
    region_target = regions{i,3};
    
    %[rdim,rn] = size(region_input);
    [rdim,rn] = size(unique(region_target','rows')');
    if rn <= 25 
        region_small_input = region_input;
        region_small_target = region_target;
        continue;
    end
    
    if rn >= 100
        region_big_input = region_input;
        region_big_target = region_target;
        continue;
    end
    if ~isempty(region_big_input) & ~isempty(region_small_input)
        break;
    end
end

save('data_region_big_small.mat','region_small_input','region_small_target','region_big_input','region_big_target');
