% Generate local regions
clc;
clear;
load('data_main.mat');

dist = 3;

[tm,tn] = size(input);

regions = cell([tn 3]);

for index = 1:tn
    data = input(:,index);
    center = target(:,index);
    
    regions{index,1} = center;
    % Collect data around the center point
    round_input = data;
    round_target = center;
    
    for sindex = 1:tn
        if sindex == index
            continue;
        end
        
        current = target(:,sindex);
        
        if norm(current - center) <= dist
            round_input = cat(2, round_input, input(:,sindex));
            round_target = cat(2, round_target, target(:,sindex));
        end
    end
    
    regions{index,2} = round_input;
    regions{index,3} = round_target;
end