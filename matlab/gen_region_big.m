% Generate local regions
clc;
clear;
load('data_main.mat');

dist = 3;

[tm,tn] = size(input);

big_regions = {};
counter = 1;
for index = 1:tn
    data = input(:,index);
    center = target(:,index);
    
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
    
    [m,n] = size(round_input);
    
    if n >= 150
        big_regions{counter,1} = center;
        big_regions{counter,2} = round_input;
        big_regions{counter,3} = round_target;
        counter = counter+1;
    end
end